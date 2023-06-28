private static Set<String> handleDatasetAssociations(SbiObjects sbiObjects,JSONObject template,Session session,Map<Integer,JSONObject> dataSetMap,boolean initializer) throws JSONException {
  logger.debug("IN");
  Set<String> associationsMap=new HashSet<String>();
  try {
    JSONObject configuration=template.optJSONObject("configuration");
    if (configuration != null) {
      JSONArray associations=configuration.optJSONArray("associations");
      boolean isAssociative=associations != null && !associations.toString().equals("{}");
      if (isAssociative) {
        for (int k=0; k < associations.length(); k++) {
          logger.debug("Found " + associations.length() + " dataset associations in document with label "+ sbiObjects.getLabel());
          JSONObject association=(JSONObject)associations.get(k);
          JSONArray fields=association.optJSONArray("fields");
          if (fields != null) {
            for (int i=0; i < fields.length() - 1; i++) {
              IDataSetDAO dataSetDAO=DAOFactory.getDataSetDAO();
              dataSetDAO.setTenant(sbiObjects.getCommonInfo().getOrganization());
              JSONObject field1=fields.optJSONObject(i);
              JSONObject field2=fields.optJSONObject(i + 1);
              SbiCockpitAssociation sbiCockpitAssociation=new SbiCockpitAssociation();
              sbiCockpitAssociation.setBiobjId(sbiObjects.getBiobjId());
              String fromDataset=field1.getString("store");
              String fromColumn=field1.getString("column");
              if (fromDataset != null) {
                try {
                  Integer fromDatasetId=dataSetMap.entrySet().stream().filter(e -> {
                    try {
                      return e.getValue().getString("dsLabel").equals(fromDataset);
                    }
 catch (                    JSONException e1) {
                      logger.error(e1.getMessage(),e1);
                      return false;
                    }
                  }
).map(Map.Entry::getKey).findFirst().orElse(null);
                  sbiCockpitAssociation.setDsIdFrom(fromDatasetId);
                }
 catch (                SpagoBIDAOException e) {
                  logger.warn(e.getMessage(),e);
                }
                sbiCockpitAssociation.setColumnNameFrom(fromColumn);
              }
              String toDataset=field2.getString("store");
              String toColumn=field2.getString("column");
              if (toDataset != null) {
                try {
                  Integer toDatasetId=dataSetMap.entrySet().stream().filter(e -> {
                    try {
                      return e.getValue().getString("dsLabel").equals(toDataset);
                    }
 catch (                    JSONException e1) {
                      logger.error(e1.getMessage(),e1);
                      return false;
                    }
                  }
).map(Map.Entry::getKey).findFirst().orElse(null);
                  sbiCockpitAssociation.setDsIdTo(toDatasetId);
                }
 catch (                SpagoBIDAOException e) {
                  logger.warn(e.getMessage(),e);
                }
                sbiCockpitAssociation.setColumnNameTo(toColumn);
              }
              updateSbiCommonInfo4Insert(sbiObjects,sbiCockpitAssociation,initializer);
              sbiCockpitAssociation.getCommonInfo().setOrganization(sbiObjects.getCommonInfo().getOrganization());
              session.save(sbiCockpitAssociation);
              session.flush();
              logger.debug(String.format("Field [%s-%s] associated with field [%s-%s] in association",fromDataset,fromColumn,toDataset,toColumn));
            }
            String associationDescription=association.getString("description");
            associationsMap.add(associationDescription);
            logger.debug(String.format("Added association with description [%s] to associationMap",associationDescription));
          }
        }
      }
    }
  }
 catch (  HibernateException e) {
    logger.error(e.getMessage(),e);
  }
 finally {
  }
  logger.debug("OUT");
  return associationsMap;
}

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
            handleDatasetAssociations_extraction_1(sbiObjects,session,dataSetMap,initializer,fields);
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

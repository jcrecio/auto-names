private JSONArray putActions(IEngUserProfile profile,JSONArray datasetsJSONArray,String typeDocWizard) throws JSONException, EMFInternalError {
  Engine qbeEngine=null;
  try {
    qbeEngine=ExecuteAdHocUtility.getQbeEngine();
  }
 catch (  SpagoBIRuntimeException r) {
    logger.info("Engine not found. ",r);
  }
  Engine geoEngine=null;
  try {
    geoEngine=ExecuteAdHocUtility.getGeoreportEngine();
  }
 catch (  SpagoBIRuntimeException r) {
    logger.info("Engine not found. ",r);
  }
  JSONObject detailAction=new JSONObject();
  detailAction.put("name","detaildataset");
  detailAction.put("description","Dataset detail");
  JSONObject deleteAction=new JSONObject();
  deleteAction.put("name","delete");
  deleteAction.put("description","Delete dataset");
  JSONObject georeportAction=new JSONObject();
  georeportAction.put("name","georeport");
  georeportAction.put("description","Show Map");
  JSONObject qbeAction=new JSONObject();
  qbeAction.put("name","qbe");
  qbeAction.put("description","Show Qbe");
  JSONArray datasetsJSONReturn=new JSONArray();
  for (int i=0; i < datasetsJSONArray.length(); i++) {
    JSONArray actions=new JSONArray();
    JSONObject datasetJSON=datasetsJSONArray.getJSONObject(i);
    if (typeDocWizard == null) {
      actions.put(detailAction);
      if (((UserProfile)profile).getUserId().toString().equals(datasetJSON.get("owner"))) {
        actions.put(deleteAction);
      }
    }
    boolean isGeoDataset=false;
    if (geoEngine != null && (typeDocWizard == null || typeDocWizard.equalsIgnoreCase("GEO"))) {
      try {
        String meta=datasetJSON.getString("meta");
        isGeoDataset=ExecuteAdHocUtility.hasGeoHierarchy(meta);
      }
 catch (      Exception e) {
        logger.error("Error during check of Geo spatial column",e);
      }
      if (isGeoDataset)       actions.put(georeportAction);
    }
    String dsType=datasetJSON.optString(DataSetConstants.DS_TYPE_CD);
    if (dsType == null || !dsType.equals(DataSetFactory.FEDERATED_DS_TYPE)) {
      if (qbeEngine != null && (typeDocWizard == null || typeDocWizard.equalsIgnoreCase("REPORT"))) {
        if (profile.getFunctionalities().contains(SpagoBIConstants.BUILD_QBE_QUERIES_FUNCTIONALITY)) {
          actions.put(qbeAction);
        }
      }
    }
    datasetJSON.put("actions",actions);
    if (typeDocWizard != null && typeDocWizard.equalsIgnoreCase("GEO")) {
      if (geoEngine != null && isGeoDataset)       datasetsJSONReturn.put(datasetJSON);
    }
 else     datasetsJSONReturn.put(datasetJSON);
  }
  return datasetsJSONReturn;
}

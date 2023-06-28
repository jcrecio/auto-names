@POST @Path("/{label}/preview") @Produces(MediaType.APPLICATION_JSON) @UserConstraint(functionalities={SpagoBIConstants.SELF_SERVICE_DATASET_MANAGEMENT}) public String getDataStorePreview(@PathParam("label") String label,String body){
  try {
    Monitor timing=MonitorFactory.start("Knowage.DataSetResource.getDataStorePreview:parseInputs");
    String aggregations=null;
    String parameters=null;
    Map<String,Object> driversRuntimeMap=null;
    String likeSelections=null;
    int start=-1;
    int limit=-1;
    Set<String> columns=null;
    if (StringUtilities.isNotEmpty(body)) {
      JSONObject jsonBody=new JSONObject(body);
      if (jsonBody.has("start")) {
        start=jsonBody.getInt("start");
      }
      if (jsonBody.has("limit")) {
        limit=jsonBody.getInt("limit");
      }
      JSONArray jsonFilters=jsonBody.optJSONArray("filters");
      if (jsonFilters != null && jsonFilters.length() > 0) {
        JSONObject jsonLikeSelections=new JSONObject();
        for (int i=0; i < jsonFilters.length(); i++) {
          JSONObject jsonFilter=jsonFilters.getJSONObject(i);
          jsonLikeSelections.put(jsonFilter.getString("column"),jsonFilter.get("value"));
        }
        likeSelections=new JSONObject().put(label,jsonLikeSelections).toString();
      }
      String sortingColumn=null;
      String sortingType=null;
      if (jsonBody.has("sorting")) {
        JSONObject jsonSorting=jsonBody.optJSONObject("sorting");
        sortingColumn=jsonSorting.getString("column");
        sortingType=jsonSorting.getString("order");
      }
      JSONArray jsonMeasures=new JSONArray();
      JSONArray jsonCategories=new JSONArray();
      IDataSet dataSet=getDatasetManagementAPI().getDataSet(label);
      IMetaData metadata=dataSet.getMetadata();
      for (int i=0; i < metadata.getFieldCount(); i++) {
        IFieldMetaData fieldMetaData=metadata.getFieldMeta(i);
        JSONObject json=new JSONObject();
        String alias=fieldMetaData.getAlias();
        json.put("id",alias);
        json.put("alias",alias);
        json.put("columnName",alias);
        json.put("orderType",alias.equals(sortingColumn) ? sortingType : "");
        json.put("orderColumn",alias);
        json.put("funct","NONE");
        if ("ATTRIBUTE".equals(fieldMetaData.getFieldType())) {
          jsonCategories.put(json);
        }
 else {
          jsonMeasures.put(json);
        }
      }
      JSONObject jsonAggregations=new JSONObject();
      jsonAggregations.put("measures",jsonMeasures);
      jsonAggregations.put("categories",jsonCategories);
      aggregations=jsonAggregations.toString();
      JSONArray jsonPars=jsonBody.optJSONArray("pars");
      JSONObject jsonObject=DataSetUtilities.parametersJSONArray2JSONObject(dataSet,jsonPars);
      parameters=jsonObject.toString();
      JSONObject jsonDrivers=jsonBody.optJSONObject("drivers");
      driversRuntimeMap=DataSetUtilities.getDriversMap(jsonDrivers);
      columns=getDataStorePreview_extraction_2(label,columns,jsonBody);
    }
    timing.stop();
    return getDataStore(label,parameters,driversRuntimeMap,null,likeSelections,-1,aggregations,null,start,limit,columns,null);
  }
 catch (  JSONException e) {
    throw new SpagoBIRestServiceException(buildLocaleFromSession(),e);
  }
catch (  Exception e) {
    logger.error("Error while previewing dataset " + label,e);
    throw new SpagoBIRuntimeException("Error while previewing dataset " + label + ". "+ e.getMessage(),e);
  }
}

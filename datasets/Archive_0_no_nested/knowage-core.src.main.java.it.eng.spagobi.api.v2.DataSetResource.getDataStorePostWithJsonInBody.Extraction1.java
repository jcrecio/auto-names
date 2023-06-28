@POST @Path("/{label}/data") @Produces(MediaType.APPLICATION_JSON) @UserConstraint(functionalities={SpagoBIConstants.SELF_SERVICE_DATASET_MANAGEMENT}) public String getDataStorePostWithJsonInBody(@PathParam("label") String label,String body,@DefaultValue("-1") @QueryParam("limit") int maxRowCount,@DefaultValue("-1") @QueryParam("offset") int offset,@DefaultValue("-1") @QueryParam("size") int fetchSize,@QueryParam("nearRealtime") boolean isNearRealtime,@QueryParam("widgetName") String widgetName){
  try {
    Monitor timing=MonitorFactory.start("Knowage.DataSetResource.getDataStorePostWithJsonInBody:parseInputs");
    String parameters=null;
    Map<String,Object> driversRuntimeMap=null;
    String selections=null;
    String likeSelections=null;
    String aggregations=null;
    String summaryRow=null;
    String options=null;
    JSONArray jsonIndexes=null;
    if (StringUtilities.isNotEmpty(body)) {
      JSONObject jsonBody=new JSONObject(body);
      JSONObject jsonParameters=jsonBody.optJSONObject("parameters");
      parameters=jsonParameters != null ? jsonParameters.toString() : null;
      JSONObject jsonDrivers=jsonBody.optJSONObject("drivers");
      driversRuntimeMap=DataSetUtilities.getDriversMap(jsonDrivers);
      JSONObject jsonSelections=jsonBody.optJSONObject("selections");
      selections=jsonSelections != null ? jsonSelections.toString() : null;
      JSONObject jsonLikeSelections=jsonBody.optJSONObject("likeSelections");
      likeSelections=jsonLikeSelections != null ? jsonLikeSelections.toString() : null;
      JSONObject jsonAggregations=jsonBody.optJSONObject("aggregations");
      aggregations=jsonAggregations != null ? jsonAggregations.toString() : null;
      JSONObject jsonSummaryRow=jsonBody.optJSONObject("summaryRow");
      if (jsonSummaryRow != null) {
        summaryRow=jsonSummaryRow != null ? jsonSummaryRow.toString() : null;
      }
 else {
        JSONArray jsonSummaryRowArray=jsonBody.optJSONArray("summaryRow");
        summaryRow=jsonSummaryRowArray != null ? jsonSummaryRowArray.toString() : null;
      }
      JSONObject jsonOptions=jsonBody.optJSONObject("options");
      options=jsonOptions != null ? jsonOptions.toString() : null;
      jsonIndexes=jsonBody.optJSONArray("indexes");
    }
    Set<String> columns=null;
    columns=getDataStorePostWithJsonInBody_extraction_2(label,timing,jsonIndexes,columns);
    return getDataStore(label,parameters,driversRuntimeMap,selections,likeSelections,maxRowCount,aggregations,summaryRow,offset,fetchSize,isNearRealtime,options,columns,widgetName);
  }
 catch (  Exception e) {
    logger.error("Error loading dataset data from " + label,e);
    throw new SpagoBIRestServiceException(buildLocaleFromSession(),e);
  }
}

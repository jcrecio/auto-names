private JSONObject buildJsonParameters(JSONObject jsonParameters,HttpServletRequest req,String role,SessionContainer permanentSession,IParameterUseDAO parameterUseDAO,BIObject obj,DocumentRuntime dum) throws JSONException, EMFUserError {
  List<DocumentDriverRuntime> parameters=DocumentExecutionUtils.getParameters(obj,role,req.getLocale(),null,null,false,dum);
  for (  DocumentDriverRuntime objParameter : parameters) {
    Monitor checkingsParameterMonitor=MonitorFactory.start("Knowage.DocumentExecutionResource.buildJsonParameters.checkings");
    try {
      if (jsonParameters.isNull(objParameter.getId())) {
        if (objParameter.getDefaultValues() != null && objParameter.getDefaultValues().size() > 0) {
          buildJsonParameters_extraction_2(jsonParameters,objParameter);
        }
      }
    }
  finally {
      checkingsParameterMonitor.stop();
    }
    ParameterUse parameterUse=null;
    Monitor lovSingleMandatoryParameterMonitor=MonitorFactory.start("Knowage.DocumentExecutionResource.buildJsonParameters.singleLovMandatoryParameter");
    try {
      if (jsonParameters.isNull(objParameter.getId()) && objParameter.isMandatory()) {
        Integer paruseId=objParameter.getParameterUseId();
        parameterUse=parameterUseDAO.loadByUseID(paruseId);
        if ("lov".equalsIgnoreCase(parameterUse.getValueSelection()) && !objParameter.getSelectionType().equalsIgnoreCase(DocumentExecutionUtils.SELECTION_TYPE_TREE) && (objParameter.getLovDependencies() == null || objParameter.getLovDependencies().size() == 0)) {
          HashMap<String,Object> defaultValuesData=DocumentExecutionUtils.getLovDefaultValues(role,obj,objParameter.getDriver(),req);
          ArrayList<HashMap<String,Object>> defaultValues=(ArrayList<HashMap<String,Object>>)defaultValuesData.get(DocumentExecutionUtils.DEFAULT_VALUES);
          if (defaultValues != null && defaultValues.size() == 1 && !defaultValues.get(0).containsKey("error")) {
            jsonParameters.put(objParameter.getId(),defaultValues.get(0).get("value"));
            jsonParameters.put(objParameter.getId() + "_field_visible_description",defaultValues.get(0).get("value"));
          }
        }
      }
    }
  finally {
      lovSingleMandatoryParameterMonitor.stop();
    }
  }
  return jsonParameters;
}

private JSONObject buildJsonParameters(JSONObject jsonParameters,HttpServletRequest req,String role,SessionContainer permanentSession,IParameterUseDAO parameterUseDAO,BIObject obj,DocumentRuntime dum) throws JSONException, EMFUserError {
  List<DocumentDriverRuntime> parameters=DocumentExecutionUtils.getParameters(obj,role,req.getLocale(),null,null,false,dum);
  for (  DocumentDriverRuntime objParameter : parameters) {
    Monitor checkingsParameterMonitor=MonitorFactory.start("Knowage.DocumentExecutionResource.buildJsonParameters.checkings");
    try {
      if (jsonParameters.isNull(objParameter.getId())) {
        if (objParameter.getDefaultValues() != null && objParameter.getDefaultValues().size() > 0) {
          if (objParameter.getDefaultValues().size() == 1) {
            Object value;
            if (objParameter.getParType().equals("DATE") && objParameter.getDefaultValues().get(0).getValue().toString().contains("#")) {
              value=convertDate(objParameter.getDefaultValues().get(0).getValue().toString().split("#")[1],SingletonConfig.getInstance().getConfigValue("SPAGOBI.DATE-FORMAT-SERVER.format"),objParameter.getDefaultValues().get(0).getValue().toString().split("#")[0]);
            }
 else             if (objParameter.getParType().equals("DATE_RANGE") && objParameter.getDefaultValues().get(0).getValue().toString().contains("#")) {
              String dateRange=objParameter.getDefaultValues().get(0).getValue().toString().split("#")[0];
              String[] dateRangeArr=dateRange.split("_");
              String range="_" + dateRangeArr[dateRangeArr.length - 1];
              dateRange=dateRange.replace(range,"");
              value=convertDate(objParameter.getDefaultValues().get(0).getValue().toString().split("#")[1],SingletonConfig.getInstance().getConfigValue("SPAGOBI.DATE-FORMAT-SERVER.format"),dateRange);
              value=value + range;
            }
 else {
              value=objParameter.getDefaultValues().get(0).getValue();
            }
            jsonParameters.put(objParameter.getId(),value);
            jsonParameters.put(objParameter.getId() + "_field_visible_description",value);
          }
 else {
            ArrayList<String> paramValArr=new ArrayList<String>();
            String paramDescStr="";
            for (int i=0; i < objParameter.getDefaultValues().size(); i++) {
              paramValArr.add(objParameter.getDefaultValues().get(i).getValue().toString());
              paramDescStr=paramDescStr + objParameter.getDefaultValues().get(i).getValue().toString();
              if (i < objParameter.getDefaultValues().size() - 1) {
                paramDescStr=paramDescStr + ";";
              }
            }
            jsonParameters.put(objParameter.getId(),paramValArr);
            jsonParameters.put(objParameter.getId() + "_field_visible_description",paramDescStr);
          }
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

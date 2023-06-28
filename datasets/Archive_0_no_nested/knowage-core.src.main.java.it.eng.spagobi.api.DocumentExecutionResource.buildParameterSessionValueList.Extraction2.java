private DefaultValuesList buildParameterSessionValueList(String sessionParameterValue,String sessionParameterDescription,BIObjectParameter objParameter){
  logger.debug("IN");
  DefaultValuesList valueList=new DefaultValuesList();
  SimpleDateFormat serverDateFormat=new SimpleDateFormat(SingletonConfig.getInstance().getConfigValue("SPAGOBI.DATE-FORMAT-SERVER.format"));
  if (objParameter.getParameter().getType().equals("DATE")) {
    String valueDate=sessionParameterValue;
    String[] date=valueDate.split("#");
    if (date.length < 2) {
      throw new SpagoBIRuntimeException("Illegal format for Value List Date Type [" + valueDate + "+], unable to find symbol [#]");
    }
    SimpleDateFormat format=new SimpleDateFormat(date[1]);
    LovValue valueDef=new LovValue();
    try {
      Date d=format.parse(date[0]);
      String dateServerFormat=serverDateFormat.format(d);
      valueDef.setValue(dateServerFormat);
      valueDef.setDescription(sessionParameterDescription);
      valueList.add(valueDef);
      return valueList;
    }
 catch (    ParseException e) {
      logger.error("Error while building default Value List Date Type ",e);
      return null;
    }
  }
 else   if (objParameter.getParameter().getType().equals("DATE_RANGE")) {
    String valueDate=sessionParameterValue;
    String[] date=valueDate.split("#");
    SimpleDateFormat format=new SimpleDateFormat(date[1]);
    LovValue valueDef=new LovValue();
    try {
      String dateRange=date[0];
      String[] dateRangeArr=dateRange.split("_");
      String range=dateRangeArr[dateRangeArr.length - 1];
      dateRange=dateRange.replace("_" + range,"");
      Date d=format.parse(dateRange);
      String dateServerFormat=serverDateFormat.format(d);
      valueDef.setValue(dateServerFormat + "_" + range);
      valueDef.setDescription(sessionParameterDescription);
      valueList.add(valueDef);
      return valueList;
    }
 catch (    ParseException e) {
      logger.error("Error while building default Value List Date Type ",e);
      return null;
    }
  }
 else   if (objParameter.isMultivalue()) {
    logger.debug("Multivalue case");
    try {
      JSONArray valuesArray=new JSONArray(sessionParameterValue);
      StringTokenizer st=new StringTokenizer(sessionParameterDescription,";",false);
      ArrayList<String> values=new ArrayList<String>();
      ArrayList<String> descriptions=new ArrayList<String>();
      buildParameterSessionValueList_extraction_1(valueList,valuesArray,st,values,descriptions);
    }
 catch (    Exception e) {
      logger.error("Error in converting multivalue session values",e);
    }
  }
 else {
    logger.debug("NOT - multivalue case");
    try {
      String value=null;
      value=buildParameterSessionValueList_extraction_2(sessionParameterValue,value);
      LovValue valueDef=new LovValue();
      valueDef.setValue(value);
      valueDef.setDescription(sessionParameterDescription);
      valueList.add(valueDef);
    }
 catch (    Exception e) {
      logger.error("Error in converting single value session values",e);
    }
  }
  logger.debug("OUT");
  return valueList;
}

/** 
 * function used to get attribute parsed from i frame URL and insert the values into BIObject
 * @param object
 * @param currentConf
 */
public void fillBIObjectWithParameterValues(BIObject object,CurrentConfigurationDocComp currentConf){
  logger.debug("IN");
  if (currentConf == null)   return;
  List parametersBO=object.getDrivers();
  Map<String,Object> currentParameters=currentConf.getParameters();
  if (currentParameters != null) {
    if (parametersBO != null) {
      for (Iterator iterator=parametersBO.iterator(); iterator.hasNext(); ) {
        BIObjectParameter parAss=(BIObjectParameter)iterator.next();
        String urlName=parAss.getParameterUrlName();
        Object valueObj=currentParameters.get(urlName);
        if (valueObj instanceof List) {
          List val=(List)valueObj;
          parAss.setParameterValues(val);
        }
 else {
          if (valueObj != null) {
            String valueString=valueObj.toString();
            List values=(new ParametersDecoder()).getOriginalValues(valueString);
            if (values != null) {
              logger.debug("Put new values " + valueString + " to parameter "+ urlName);
              parAss.setParameterValues(values);
              currentParameters.remove(urlName);
            }
          }
        }
      }
    }
    if (parametersBO == null)     parametersBO=new ArrayList<BIObjectParameter>();
    for (Iterator iterator=currentParameters.keySet().iterator(); iterator.hasNext(); ) {
      String lab=(String)iterator.next();
      BIObjectParameter biObjPar=new BIObjectParameter();
      biObjPar.setParameterUrlName(lab);
      Object valueObj=currentParameters.get(lab);
      fillBIObjectWithParameterValues_extraction_2(object,biObjPar,valueObj);
    }
  }
  logger.debug("OUT");
}

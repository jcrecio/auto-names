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
        fillBIObjectWithParameterValues_extraction_1(currentParameters,parAss,urlName,valueObj);
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

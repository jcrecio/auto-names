public void setParameterValues(String userProvidedParametersStr,boolean transientMode){
  logger.debug("IN");
  if (userProvidedParametersStr != null) {
    ParameterValuesDecoder decoder=new ParameterValuesDecoder();
    List biparameters=object.getDrivers();
    if (biparameters == null) {
      logger.error("BIParameters list cannot be null!!!");
      return;
    }
    userProvidedParametersStr=JavaScript.unescape(userProvidedParametersStr);
    String[] userProvidedParameters=userProvidedParametersStr.split("&");
    for (int i=0; i < userProvidedParameters.length; i++) {
      String[] chunks=userProvidedParameters[i].split("=");
      if (chunks == null || chunks.length > 2) {
        logger.warn("User provided parameter [" + userProvidedParameters[i] + "] cannot be splitted in "+ "[parameter url name=parameter value] by '=' characters.");
        continue;
      }
      String parUrlName=chunks[0];
      if (parUrlName == null || parUrlName.trim().equals(""))       continue;
      BIObjectParameter biparameter=null;
      Iterator it=biparameters.iterator();
      biparameter=setParameterValues_extraction_1(parUrlName,biparameter,it);
      String parValue=setParameterValues_extraction_2(transientMode,decoder,chunks,parUrlName,biparameter);
    }
  }
  logger.debug("OUT");
}

public void applyViewpoint(String userProvidedParametersStr,boolean transientMode){
  logger.debug("IN");
  if (userProvidedParametersStr != null) {
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
      while (it.hasNext()) {
        BIObjectParameter temp=(BIObjectParameter)it.next();
        if (temp.getParameterUrlName().equals(parUrlName)) {
          biparameter=temp;
          break;
        }
      }
      if (biparameter == null) {
        logger.warn("No BIObjectParameter with url name = ['" + parUrlName + "'] was found.");
        continue;
      }
      String parValue="";
      if (chunks.length == 2) {
        parValue=chunks[1];
      }
      if (parValue != null && parValue.equalsIgnoreCase("NULL")) {
        biparameter.setParameterValues(null);
      }
 else {
        List parameterValues=new ArrayList();
        String[] values=parValue.split(";");
        for (int m=0; m < values.length; m++) {
          parameterValues.add(values[m]);
        }
        biparameter.setParameterValues(parameterValues);
      }
      biparameter.setTransientParmeters(transientMode);
    }
  }
  logger.debug("OUT");
}

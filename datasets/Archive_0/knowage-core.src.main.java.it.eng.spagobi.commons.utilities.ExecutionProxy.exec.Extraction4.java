/** 
 * Executes a document in background with the given profile.
 * @param profile             The user profile
 * @param modality            The execution modality (for auditing)
 * @param defaultOutputFormat The default output format (optional) , considered if the document has no output format parameter set
 * @return the byte[]
 */
public byte[] exec(IEngUserProfile profile,String modality,String defaultOutputFormat){
  logger.debug("IN");
  byte[] response=new byte[0];
  try {
    if (biObject == null)     return response;
    Engine eng=biObject.getEngine();
    if (!EngineUtilities.isExternal(eng)) {
      return exec_extraction_1(profile,response,eng);
    }
    String driverClassName=eng.getDriverName();
    IEngineDriver aEngineDriver=(IEngineDriver)Class.forName(driverClassName).newInstance();
    Map mapPars=aEngineDriver.getParameterMap(biObject,profile,"");
    if (defaultOutputFormat != null && !defaultOutputFormat.trim().equals("")) {
      List params=biObject.getDrivers();
      Iterator iterParams=params.iterator();
      boolean findOutPar=false;
      findOutPar=exec_extraction_3(iterParams,findOutPar);
      if (!findOutPar) {
        mapPars.put("outputType",defaultOutputFormat);
      }
    }
    adjustParametersForExecutionProxy(aEngineDriver,mapPars,modality);
    Locale locale=GeneralUtilities.getDefaultLocale();
    if (!mapPars.containsKey(SpagoBIConstants.SBI_COUNTRY)) {
      String country=locale.getCountry();
      mapPars.put(SpagoBIConstants.SBI_COUNTRY,country);
    }
    if (!mapPars.containsKey(SpagoBIConstants.SBI_LANGUAGE)) {
      String language=locale.getLanguage();
      mapPars.put(SpagoBIConstants.SBI_LANGUAGE,language);
    }
    if (SEND_MAIL_MODALITY.equals(modality) || EXPORT_MODALITY.equals(modality) || SpagoBIConstants.MASSIVE_EXPORT_MODALITY.equals(modality)) {
      mapPars.put(SsoServiceInterface.USER_ID,((UserProfile)profile).getUserUniqueIdentifier());
    }
    if (!mapPars.containsKey("SBI_EXECUTION_ID")) {
      UUIDGenerator uuidGen=UUIDGenerator.getInstance();
      UUID uuidObj=uuidGen.generateTimeBasedUUID();
      String executionId=uuidObj.toString();
      executionId=executionId.replaceAll("-","");
      mapPars.put("SBI_EXECUTION_ID",executionId);
    }
    AuditManager auditManager=AuditManager.getInstance();
    Integer auditId=auditManager.insertAudit(biObject,null,profile,"",modality != null ? modality : "");
    HttpMethod httpMethod=exec_extraction_4(eng,mapPars,auditId);
    HttpClient client=new HttpClient();
    int statusCode=client.executeMethod(httpMethod);
    logger.debug("statusCode=" + statusCode);
    response=httpMethod.getResponseBody();
    Header headContetType=httpMethod.getResponseHeader("Content-Type");
    if (headContetType != null) {
      returnedContentType=headContetType.getValue();
    }
 else {
      returnedContentType="application/octet-stream";
    }
    auditManager.updateAudit(auditId,null,new Long(GregorianCalendar.getInstance().getTimeInMillis()),"EXECUTION_PERFORMED",null,null);
    httpMethod.releaseConnection();
  }
 catch (  Exception e) {
    logger.error("Error while executing object ",e);
  }
  logger.debug("OUT");
  return response;
}

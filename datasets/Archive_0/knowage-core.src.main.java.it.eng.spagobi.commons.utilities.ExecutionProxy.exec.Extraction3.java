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
      if (eng.getClassName().equals("it.eng.spagobi.engines.kpi.SpagoBIKpiInternalEngine")) {
        SourceBean request=null;
        SourceBean resp=null;
        EMFErrorHandler errorHandler=null;
        try {
          request=new SourceBean("");
          resp=new SourceBean("");
        }
 catch (        SourceBeanException e1) {
          e1.printStackTrace();
        }
        RequestContainer reqContainer=new RequestContainer();
        return exec_extraction_2(profile,response,eng,request,resp,reqContainer);
      }
 else       if (eng.getClassName().equals("it.eng.spagobi.engines.chart.SpagoBIChartInternalEngine")) {
        SourceBean request=null;
        EMFErrorHandler errorHandler=null;
        try {
          request=new SourceBean("");
        }
 catch (        SourceBeanException e1) {
          e1.printStackTrace();
        }
        RequestContainer reqContainer=new RequestContainer();
        SpagoBIChartInternalEngine sbcie=new SpagoBIChartInternalEngine();
        File file=sbcie.executeChartCode(reqContainer,biObject,null,profile);
        InputStream is=new FileInputStream(file);
        long length=file.length();
        if (length > Integer.MAX_VALUE) {
          logger.error("file too large");
          return null;
        }
        byte[] bytes=new byte[(int)length];
        int offset=0;
        int numRead=0;
        while (offset < bytes.length && (numRead=is.read(bytes,offset,bytes.length - offset)) >= 0) {
          offset+=numRead;
        }
        if (offset < bytes.length) {
          logger.warn("Could not read all the file");
        }
        is.close();
        return bytes;
      }
 else {
        return response;
      }
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

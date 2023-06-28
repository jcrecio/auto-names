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
        ResponseContainer resContainer=new ResponseContainer();
        reqContainer.setServiceRequest(request);
        resContainer.setServiceResponse(resp);
        DefaultRequestContext defaultRequestContext=new DefaultRequestContext(reqContainer,resContainer);
        resContainer.setErrorHandler(new EMFErrorHandler());
        RequestContainer.setRequestContainer(reqContainer);
        ResponseContainer.setResponseContainer(resContainer);
        Locale locale=new Locale("it","IT","");
        SessionContainer session=new SessionContainer(true);
        reqContainer.setSessionContainer(session);
        SessionContainer permSession=session.getPermanentContainer();
        permSession.setAttribute(IEngUserProfile.ENG_USER_PROFILE,profile);
        errorHandler=defaultRequestContext.getErrorHandler();
        String className=eng.getClassName();
        logger.debug("Try instantiating class " + className + " for internal engine "+ eng.getName()+ "...");
        InternalEngineIFace internalEngine=null;
        try {
          if (className == null && className.trim().equals(""))           throw new ClassNotFoundException();
          internalEngine=(InternalEngineIFace)Class.forName(className).newInstance();
        }
 catch (        ClassNotFoundException cnfe) {
          logger.error("The class ['" + className + "'] for internal engine "+ eng.getName()+ " was not found.",cnfe);
          Vector params=new Vector();
          params.add(className);
          params.add(eng.getName());
          errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR,2001,params));
          return response;
        }
catch (        Exception e) {
          logger.error("Error while instantiating class " + className,e);
          errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR,100));
          return response;
        }
        try {
          reqContainer.setAttribute("scheduledExecution","true");
          internalEngine.execute(reqContainer,biObject,resp);
        }
 catch (        EMFUserError e) {
          logger.error("Error during engine execution",e);
          errorHandler.addError(e);
        }
catch (        Exception e) {
          logger.error("Error while engine execution",e);
          errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR,100));
        }
        return response;
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
      while (iterParams.hasNext()) {
        BIObjectParameter par=(BIObjectParameter)iterParams.next();
        String parUrlName=par.getParameterUrlName();
        List values=par.getParameterValues();
        logger.debug("processing biparameter with url name " + parUrlName);
        if (parUrlName.equalsIgnoreCase("outputType") && values != null && values.size() > 0) {
          findOutPar=true;
          break;
        }
      }
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

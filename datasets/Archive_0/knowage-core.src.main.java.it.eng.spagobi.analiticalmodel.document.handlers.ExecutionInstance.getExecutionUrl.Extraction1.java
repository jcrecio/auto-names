public String getExecutionUrl(Locale locale){
  logger.debug("IN");
  String url=null;
  Engine engine=this.getBIObject().getEngine();
  Domain engineType;
  try {
    engineType=DAOFactory.getDomainDAO().loadDomainById(engine.getEngineTypeId());
  }
 catch (  EMFUserError e) {
    throw new SpagoBIServiceException("Impossible to load engine type domain",e);
  }
  if ("EXT".equalsIgnoreCase(engineType.getValueCd())) {
    String driverClassName=engine.getDriverName();
    IEngineDriver aEngineDriver=null;
    try {
      aEngineDriver=(IEngineDriver)Class.forName(driverClassName).newInstance();
    }
 catch (    Exception e) {
      throw new SpagoBIServiceException("Cannot istantiate engine driver class: " + driverClassName,e);
    }
    Map mapPars=aEngineDriver.getParameterMap(object,userProfile,executionRole);
    addSystemParametersForExternalEngines(mapPars,locale);
    url=GeneralUtilities.getUrl(engine.getUrl(),mapPars);
  }
 else {
    StringBuffer buffer=new StringBuffer();
    buffer.append(GeneralUtilities.getSpagoBIProfileBaseUrl(((UserProfile)userProfile).getUserId().toString()));
    buffer.append("&PAGE=ExecuteBIObjectPage");
    buffer.append("&" + SpagoBIConstants.TITLE_VISIBLE + "=FALSE");
    buffer.append("&" + SpagoBIConstants.TOOLBAR_VISIBLE + "=FALSE");
    buffer.append("&" + ObjectsTreeConstants.OBJECT_LABEL + "="+ object.getLabel());
    buffer.append("&" + SpagoBIConstants.ROLE + "="+ executionRole);
    buffer.append("&" + SpagoBIConstants.RUN_ANYWAY + "=TRUE");
    buffer.append("&" + SpagoBIConstants.IGNORE_SUBOBJECTS_VIEWPOINTS_SNAPSHOTS + "=TRUE");
    buffer.append("&SBI_EXECUTION_ID=" + this.executionId);
    String kpiClassName=KpiDriver.class.getCanonicalName();
    if (engine.getClassName().equals(kpiClassName)) {
      Integer auditId=createAuditId();
      if (auditId != null) {
        buffer.append("&" + AuditManager.AUDIT_ID + "="+ auditId);
      }
    }
    UUIDGenerator uuidGen=UUIDGenerator.getInstance();
    UUID uuid=uuidGen.generateRandomBasedUUID();
    buffer.append("&" + LightNavigationConstants.LIGHT_NAVIGATOR_ID + "="+ uuid.toString());
    List parameters=object.getDrivers();
    if (parameters != null && parameters.size() > 0) {
      Iterator it=parameters.iterator();
      while (it.hasNext()) {
        getExecutionUrl_extraction_2(buffer,it);
      }
    }
    url=buffer.toString();
  }
  logger.debug("OUT: returning url = [" + url + "]");
  return url;
}

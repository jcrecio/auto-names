public String getExecutionUrl(BIObject obj,String executionModality,String role){
  logger.debug("IN");
  Monitor getExecutionUrlMonitor=MonitorFactory.start("Knowage.DocumentRuntime.getExecutionUrl");
  String url=null;
  Engine engine=obj.getEngine();
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
    Map mapPars=aEngineDriver.getParameterMap(obj,this.getUserProfile(),role);
    addSystemParametersForExternalEngines(mapPars,this.getLocale(),obj,executionModality,role);
    url=GeneralUtilities.getUrl(engine.getUrl(),mapPars);
  }
 else {
    StringBuffer buffer=new StringBuffer();
    buffer.append(GeneralUtilities.getSpagoBIProfileBaseUrl(((UserProfile)this.getUserProfile()).getUserId().toString()));
    buffer.append("&PAGE=ExecuteBIObjectPage");
    buffer.append("&" + SpagoBIConstants.TITLE_VISIBLE + "=FALSE");
    buffer.append("&" + SpagoBIConstants.TOOLBAR_VISIBLE + "=FALSE");
    UUID uuid=getExecutionUrl_extraction_1(obj,executionModality,role,engine,buffer);
    buffer.append("&" + LightNavigationConstants.LIGHT_NAVIGATOR_ID + "="+ uuid.toString());
    List parameters=obj.getDrivers();
    if (parameters != null && parameters.size() > 0) {
      Iterator it=parameters.iterator();
      while (it.hasNext()) {
        BIObjectParameter aParameter=(BIObjectParameter)it.next();
        List list=aParameter.getParameterValues();
        getExecutionUrl_extraction_2(buffer,aParameter,list);
      }
    }
    url=buffer.toString();
  }
  logger.debug("OUT: returning url = [" + url + "]");
  getExecutionUrlMonitor.stop();
  return url;
}

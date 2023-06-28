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
    getExecutionUrl_extraction_1(engine,buffer);
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

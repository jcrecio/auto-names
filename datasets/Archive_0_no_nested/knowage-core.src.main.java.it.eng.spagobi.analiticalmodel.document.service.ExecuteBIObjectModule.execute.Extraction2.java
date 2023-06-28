/** 
 * Based on the object type launches the right execution mechanism. For objects executed by an external engine instantiates the driver for execution, gets the execution call parameters map, adds in reponse the map of the parameters. For objects executed by an internal engine, instantiates the engine class and launches execution method.
 * @param instance The execution instance
 * @param subObj The SubObjectDetail subObject to be executed (in case it is not null)
 * @param response The response Source Bean
 */
private void execute(ExecutionInstance instance,SubObject subObj,String[] vpParameters,SourceBean response){
  logger.debug("IN");
  EMFErrorHandler errorHandler=getErrorHandler();
  BIObject obj=instance.getBIObject();
  Engine engine=obj.getEngine();
  Domain engineType=null;
  Domain compatibleBiobjType=null;
  try {
    engineType=DAOFactory.getDomainDAO().loadDomainById(engine.getEngineTypeId());
    compatibleBiobjType=DAOFactory.getDomainDAO().loadDomainById(engine.getBiobjTypeId());
  }
 catch (  EMFUserError error) {
    logger.error("Error retrieving document's engine information",error);
    errorHandler.addError(error);
    return;
  }
  String compatibleBiobjTypeCd=compatibleBiobjType.getValueCd();
  String biobjTypeCd=obj.getBiObjectTypeCode();
  if (!compatibleBiobjTypeCd.equalsIgnoreCase(biobjTypeCd)) {
    logger.warn("Engine cannot execute input document type: " + "the engine " + engine.getName() + " can execute '"+ compatibleBiobjTypeCd+ "' type documents "+ "while the input document is a '"+ biobjTypeCd+ "'.");
    Vector params=new Vector();
    params.add(engine.getName());
    params.add(compatibleBiobjTypeCd);
    params.add(biobjTypeCd);
    errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR,2002,params));
    return;
  }
  IEngUserProfile profile=getUserProfile();
  if (!canExecute(profile,obj))   return;
  String executionRole=instance.getExecutionRole();
  if ("EXT".equalsIgnoreCase(engineType.getValueCd())) {
    try {
      response.setAttribute(SpagoBIConstants.PUBLISHER_NAME,"ExecuteBIObjectPageExecution");
      String driverClassName=obj.getEngine().getDriverName();
      IEngineDriver aEngineDriver=(IEngineDriver)Class.forName(driverClassName).newInstance();
      Map mapPars=null;
      execute_extraction_1(subObj,vpParameters,response,obj,profile,executionRole,aEngineDriver);
    }
 catch (    Exception e) {
      logger.error("Error During object execution",e);
      errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR,100));
    }
  }
 else {
    String className=engine.getClassName();
    logger.debug("Try instantiating class " + className + " for internal engine "+ engine.getName()+ "...");
    InternalEngineIFace internalEngine=null;
    try {
      if (className == null && className.trim().equals(""))       throw new ClassNotFoundException();
      internalEngine=(InternalEngineIFace)Class.forName(className).newInstance();
    }
 catch (    ClassNotFoundException cnfe) {
      logger.error("The class ['" + className + "'] for internal engine "+ engine.getName()+ " was not found.",cnfe);
      Vector params=new Vector();
      params.add(className);
      params.add(engine.getName());
      errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR,2001,params));
      return;
    }
catch (    Exception e) {
      logger.error("Error while instantiating class " + className,e);
      errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR,100));
      return;
    }
    logger.debug("Class " + className + " instantiated successfully. Now engine's execution starts.");
    execute_extraction_2(subObj,response,errorHandler,obj,internalEngine);
  }
  logger.debug("OUT");
}

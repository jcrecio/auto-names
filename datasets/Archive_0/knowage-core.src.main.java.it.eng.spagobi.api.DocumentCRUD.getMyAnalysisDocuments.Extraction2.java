@GET @Path("/myAnalysisDocsList") @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8") public String getMyAnalysisDocuments(@Context HttpServletRequest req){
  logger.debug("IN");
  String user=req.getParameter(USER);
  String docType=req.getParameter(DOCUMENT_TYPE);
  logger.debug("Searching documents inside personal folder of user [" + user + "]");
  IEngUserProfile profile=this.getUserProfile();
  List userFunctionalties;
  LowFunctionality personalFolder=null;
  try {
    ILowFunctionalityDAO functionalitiesDAO=DAOFactory.getLowFunctionalityDAO();
    userFunctionalties=functionalitiesDAO.loadAllUserFunct();
    for (Iterator it=userFunctionalties.iterator(); it.hasNext(); ) {
      LowFunctionality funct=(LowFunctionality)it.next();
      if (UserUtilities.isPersonalFolder(funct,(UserProfile)profile)) {
        personalFolder=funct;
        break;
      }
    }
    List myObjects=new ArrayList();
    if (personalFolder != null) {
      Engine geoEngine=null;
      Engine cockpitEngine=null;
      Engine kpiEngine=null;
      try {
        geoEngine=ExecuteAdHocUtility.getGeoreportEngine();
      }
 catch (      SpagoBIRuntimeException r) {
        logger.info("Engine not found. ",r);
      }
      try {
        cockpitEngine=ExecuteAdHocUtility.getCockpitEngine();
      }
 catch (      SpagoBIRuntimeException r) {
        logger.info("Engine not found. ",r);
      }
      try {
        kpiEngine=ExecuteAdHocUtility.getKPIEngine();
      }
 catch (      SpagoBIRuntimeException r) {
        logger.info("Engine not found. ",r);
      }
      myObjects=getMyAnalysisDocuments_extraction_1(docType,profile,personalFolder,myObjects,geoEngine,cockpitEngine,kpiEngine);
      MessageBuilder m=new MessageBuilder();
      Locale locale=m.getLocale(req);
      JSONArray documentsJSON=(JSONArray)SerializerFactory.getSerializer("application/json").serialize(myObjects,locale);
      DocumentsJSONDecorator.decorateDocuments(documentsJSON,profile,personalFolder);
      JSONObject documentsResponseJSON=createJSONResponseDocuments(documentsJSON);
      return documentsResponseJSON.toString();
    }
  }
 catch (  EMFUserError e) {
    logger.error("Error in myAnalysisDocsList Service: " + e);
  }
catch (  SerializationException e) {
    logger.error("Serializing Error in myAnalysisDocsList Service: " + e);
  }
catch (  JSONException e) {
    logger.error("JSONException Error in myAnalysisDocsList Service: " + e);
  }
  logger.debug("OUT");
  return "{}";
}

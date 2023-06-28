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
      if ((docType == null) || (docType.equalsIgnoreCase("ALL"))) {
        List filteredMyObjects=new ArrayList();
        myObjects=DAOFactory.getBIObjectDAO().loadBIObjects(Integer.valueOf(personalFolder.getId()),profile,true);
        for (Iterator it=myObjects.iterator(); it.hasNext(); ) {
          BIObject biObject=(BIObject)it.next();
          String biObjectType=biObject.getBiObjectTypeCode();
          if ((geoEngine != null && biObject.getEngine().getId().equals(geoEngine.getId())) || (cockpitEngine != null && biObject.getEngine().getId().equals(cockpitEngine.getId())) || (kpiEngine != null && biObject.getEngine().getId().equals(kpiEngine.getId()))) {
            filteredMyObjects.add(biObject);
          }
        }
        myObjects=filteredMyObjects;
      }
 else       if (docType.equalsIgnoreCase("Map") && geoEngine != null) {
        myObjects=DAOFactory.getBIObjectDAO().loadBIObjects("MAP","REL",personalFolder.getPath());
      }
 else       if (docType.equalsIgnoreCase("Cockpit") && cockpitEngine != null) {
        List filteredMyObjects=new ArrayList();
        myObjects=DAOFactory.getBIObjectDAO().loadBIObjects("DOCUMENT_COMPOSITE","REL",personalFolder.getPath());
        for (Iterator it=myObjects.iterator(); it.hasNext(); ) {
          BIObject biObject=(BIObject)it.next();
          if (biObject.getEngine().getId().equals(cockpitEngine.getId())) {
            filteredMyObjects.add(biObject);
          }
        }
        myObjects=filteredMyObjects;
      }
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

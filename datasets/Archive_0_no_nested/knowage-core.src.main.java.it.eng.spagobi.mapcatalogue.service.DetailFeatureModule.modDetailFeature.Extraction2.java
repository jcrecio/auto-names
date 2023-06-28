/** 
 * Inserts/Modifies the detail of an map according to the user request.  When a map is modified, the <code>modifyMap</code> method is called; when a new map is added, the <code>insertMap</code>method is called. These two cases are  differentiated by the <code>mod</code> String input value .
 * @param request The request information contained in a SourceBean Object
 * @param mod A request string used to differentiate insert/modify operations
 * @param response The response SourceBean 
 * @throws EMFUserError If an exception occurs
 * @throws SourceBeanException If a SourceBean exception occurs
 */
private void modDetailFeature(SourceBean request,String mod,SourceBean response) throws EMFUserError, SourceBeanException {
  GeoFeature feature=recoverFeatureDetails(request);
  HashMap<String,String> logParam=new HashMap();
  logParam.put("FEAUTURE_NAME",feature.getName());
  RequestContainer requestContainer=this.getRequestContainer();
  ResponseContainer responseContainer=this.getResponseContainer();
  SessionContainer session=requestContainer.getSessionContainer();
  SessionContainer permanentSession=session.getPermanentContainer();
  IEngUserProfile profile=(IEngUserProfile)permanentSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
  try {
    ISbiGeoFeaturesDAO dao=DAOFactory.getSbiGeoFeaturesDAO();
    dao.setUserProfile(profile);
    if (feature.getName() == null) {
      response.setAttribute("mapObj",feature);
      response.setAttribute("modality",mod);
      return;
    }
    EMFErrorHandler errorHandler=getErrorHandler();
    Collection errors=errorHandler.getErrors();
    if (errors != null && errors.size() > 0) {
      Iterator iterator=errors.iterator();
      while (iterator.hasNext()) {
        Object error=iterator.next();
        if (error instanceof EMFValidationError) {
          response.setAttribute("featureObj",feature);
          response.setAttribute("modality",mod);
          if (mod.equalsIgnoreCase(AdmintoolsConstants.DETAIL_INS)) {
            AuditLogUtilities.updateAudit(getHttpRequest(),profile,"MAP_CATALOG_FEATURE.ADD",logParam,"KO");
          }
 else {
            AuditLogUtilities.updateAudit(getHttpRequest(),profile,"MAP_CATALOG_FEATURE.MODIFY",logParam,"KO");
          }
          return;
        }
      }
    }
    if (mod.equalsIgnoreCase(SpagoBIConstants.DETAIL_INS)) {
      if (dao.loadFeatureByName(feature.getName()) != null) {
        HashMap params=new HashMap();
        params.put(AdmintoolsConstants.PAGE,ListFeaturesModule.MODULE_PAGE);
        EMFUserError error=new EMFUserError(EMFErrorSeverity.ERROR,5018,new Vector(),params);
        getErrorHandler().addError(error);
        AuditLogUtilities.updateAudit(getHttpRequest(),profile,"MAP_CATALOG_FEATURE.ADD",logParam,"ERR");
        return;
      }
      dao.insertFeature(feature);
    }
 else {
      dao.modifyFeature(feature);
    }
  }
 catch (  EMFUserError e) {
    if (mod.equalsIgnoreCase(AdmintoolsConstants.DETAIL_INS)) {
      try {
        AuditLogUtilities.updateAudit(getHttpRequest(),profile,"MAP_CATALOG_FEATURE.ADD",logParam,"ERR");
      }
 catch (      Exception e1) {
        e1.printStackTrace();
      }
    }
 else {
      try {
        AuditLogUtilities.updateAudit(getHttpRequest(),profile,"MAP_CATALOG_FEATURE.MODIFY",logParam,"ERR");
      }
 catch (      Exception e1) {
        e1.printStackTrace();
      }
    }
    HashMap params=new HashMap();
    params.put(AdmintoolsConstants.PAGE,ListMapsModule.MODULE_PAGE);
    throw new EMFUserError(EMFErrorSeverity.ERROR,5016,new Vector(),params);
  }
catch (  Exception ex) {
    modDetailFeature_extraction_3(mod,logParam,profile);
    TracerSingleton.log(SpagoBIConstants.NAME_MODULE,TracerSingleton.MAJOR,"Cannot fill response container" + ex.getLocalizedMessage());
    throw new EMFUserError(EMFErrorSeverity.ERROR,100);
  }
  response.setAttribute("loopback","true");
  modDetailFeature_extraction_4(mod,logParam,profile);
}

/** 
 * Inserts/Modifies the detail of an engine according to the user request.  When an engine is modified, the <code>modifyEngine</code> method is called; when a new engine is added, the <code>insertEngine</code>method is called. These two cases are  differentiated by the <code>mod</code> String input value .
 * @param request The request information contained in a SourceBean Object
 * @param mod A request string used to differentiate insert/modify operations
 * @param response The response SourceBean 
 * @throws EMFUserError If an exception occurs
 * @throws SourceBeanException If a SourceBean exception occurs
 */
private void modDettaglioEngine(SourceBean request,String mod,SourceBean response) throws EMFUserError, SourceBeanException {
  Engine engine=recoverEngineDetails(request);
  HashMap<String,String> logParam=new HashMap();
  logParam.put("NAME",engine.getName());
  logParam.put("TYPE",engine.getEngineTypeId().toString());
  try {
    String engineTypeIdStr=(String)request.getAttribute("engineTypeId");
    Integer engineTypeId=new Integer(engineTypeIdStr);
    Domain engineType=DAOFactory.getDomainDAO().loadDomainById(engineTypeId);
    if ("EXT".equalsIgnoreCase(engineType.getValueCd()))     ValidationCoordinator.validate("PAGE","ExternalEngineDetailPage",this);
 else     ValidationCoordinator.validate("PAGE","InternalEngineDetailPage",this);
    EMFErrorHandler errorHandler=getErrorHandler();
    Collection errors=errorHandler.getErrors();
    if (errors != null && errors.size() > 0) {
      Iterator iterator=errors.iterator();
      while (iterator.hasNext()) {
        Object error=iterator.next();
        if (error instanceof EMFValidationError) {
          AuditLogUtilities.updateAudit(getHttpRequest(),profile,"ENGINE.MODIFY",logParam,"ERR");
          response.setAttribute("engineObj",engine);
          response.setAttribute("modality",mod);
          return;
        }
      }
    }
    RequestContainer reqCont=getRequestContainer();
    SessionContainer sessCont=reqCont.getSessionContainer();
    SessionContainer permSess=sessCont.getPermanentContainer();
    UserProfile profile=(UserProfile)this.getRequestContainer().getSessionContainer().getPermanentContainer().getAttribute(IEngUserProfile.ENG_USER_PROFILE);
    String userId=profile.getUserId().toString();
    IEngineDAO dao=DAOFactory.getEngineDAO();
    dao.setUserProfile(profile);
    if (mod.equalsIgnoreCase(AdmintoolsConstants.DETAIL_INS)) {
      dao.insertEngine(engine);
    }
 else {
      dao.modifyEngine(engine);
    }
  }
 catch (  EMFUserError e) {
    modDettaglioEngine_extraction_1(mod,logParam);
    HashMap params=new HashMap();
    params.put(AdmintoolsConstants.PAGE,ListEnginesModule.MODULE_PAGE);
    throw new EMFUserError(EMFErrorSeverity.ERROR,1012,new Vector(),params);
  }
catch (  Exception ex) {
    modDettaglioEngine_extraction_2(mod,logParam);
    SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE,"DetailEngineModule","modDetailEngine","Cannot fill response container",ex);
    throw new EMFUserError(EMFErrorSeverity.ERROR,100);
  }
  modDettaglioEngine_extraction_3(mod,logParam);
  response.setAttribute("loopback","true");
}

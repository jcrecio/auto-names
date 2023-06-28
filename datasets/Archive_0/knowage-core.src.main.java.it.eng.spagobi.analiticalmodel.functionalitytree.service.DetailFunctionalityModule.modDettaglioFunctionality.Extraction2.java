/** 
 * Inserts/Modifies the detail of a low functionality according to the user request. When a parameter use mode is modified, the <code>modifyLowFunctionality</code> method is called; when a new parameter use mode is added, the <code>insertLowFunctionality</code> method is called. These two cases are differentiated by the <code>mod</code> String input value .
 * @param request The request information contained in a SourceBean Object
 * @param mod A request string used to differentiate insert/modify operations
 * @param response The response SourceBean
 * @throws EMFUserError If an exception occurs
 * @throws SourceBeanException If a SourceBean exception occurs
 */
private void modDettaglioFunctionality(SourceBean request,String mod,SourceBean response) throws EMFUserError, SourceBeanException {
  HashMap<String,String> logParam=new HashMap();
  try {
    RequestContainer requestContainer=this.getRequestContainer();
    ResponseContainer responseContainer=this.getResponseContainer();
    session=requestContainer.getSessionContainer();
    SessionContainer permanentSession=session.getPermanentContainer();
    profile=(IEngUserProfile)permanentSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
    LowFunctionality lowFunct=recoverLowFunctionalityDetails(request,mod);
    logParam.put("Functionality_Name",lowFunct.getName());
    response.setAttribute(FUNCTIONALITY_OBJ,lowFunct);
    response.setAttribute(AdmintoolsConstants.MODALITY,mod);
    EMFErrorHandler errorHandler=getErrorHandler();
    Collection errors=errorHandler.getErrors();
    if (errors != null && errors.size() > 0) {
      Iterator iterator=errors.iterator();
      while (iterator.hasNext()) {
        Object error=iterator.next();
        if (error instanceof EMFValidationError) {
          modDettaglioFunctionality_extraction_1(response,logParam,lowFunct);
          return;
        }
      }
    }
    modDettaglioFunctionality_extraction_2(mod,logParam,lowFunct);
  }
 catch (  EMFUserError eex) {
    try {
      AuditLogUtilities.updateAudit(getHttpRequest(),profile,"FUNCTIONALITY.ADD/MODIFY",logParam,"ERR");
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    EMFErrorHandler errorHandler=getErrorHandler();
    errorHandler.addError(eex);
    return;
  }
catch (  Exception ex) {
    try {
      AuditLogUtilities.updateAudit(getHttpRequest(),profile,"FUNCTIONALITY.ADD/MODIFY",logParam,"ERR");
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE,"DetailFunctionalityModule","modDettaglioFunctionality","Cannot fill response container",ex);
    throw new EMFUserError(EMFErrorSeverity.ERROR,100);
  }
  response.setAttribute(AdmintoolsConstants.LOOPBACK,"true");
}

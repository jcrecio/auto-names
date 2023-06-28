/** 
 * Handles the final execution of the object
 * @param request The request SourceBean
 * @param response The response SourceBean
 */
private void executionHandler(SourceBean request,SourceBean response) throws Exception {
  logger.debug("IN");
  ExecutionInstance instance=getExecutionInstance();
  instance.refreshParametersValues(request,false);
  String pendingDelete=(String)request.getAttribute("PENDING_DELETE");
  HashMap paramsDescriptionMap=(HashMap)contextManager.get("PARAMS_DESCRIPTION_MAP");
  if (pendingDelete != null && !pendingDelete.trim().equals("")) {
    BIObject object=instance.getBIObject();
    List biparams=object.getDrivers();
    Iterator iterParams=biparams.iterator();
    while (iterParams.hasNext()) {
      BIObjectParameter biparam=(BIObjectParameter)iterParams.next();
      if (paramsDescriptionMap.get(biparam.getParameterUrlName()) != null)       paramsDescriptionMap.put(biparam.getParameterUrlName(),"");
    }
    response.setAttribute(SpagoBIConstants.PUBLISHER_NAME,"ExecuteBIObjectPageParameter");
    return;
  }
  Object lookupObjParId=request.getAttribute("LOOKUP_OBJ_PAR_ID");
  if (isLookupCall(request)) {
    BIObjectParameter lookupBIParameter=getLookedUpParameter(request);
    if (lookupBIParameter == null) {
      logger.error("The BIParameter with id = " + getLookedUpParameterId(request).toString() + " does not exist.");
      throw new EMFUserError(EMFErrorSeverity.ERROR,1041);
    }
    ModalitiesValue modVal=lookupBIParameter.getParameter().getModalityValue();
    String lookupType=(String)request.getAttribute("LOOKUP_TYPE");
    if (lookupType == null)     lookupType="LIST";
    if (lookupType.equalsIgnoreCase("CHECK_LIST")) {
      response.setAttribute("CHECKLIST","true");
      response.setAttribute(SpagoBIConstants.PUBLISHER_NAME,"ChecklistLookupPublisher");
    }
 else     if (lookupType.equalsIgnoreCase("LIST")) {
      response.setAttribute("LIST","true");
      response.setAttribute(SpagoBIConstants.PUBLISHER_NAME,"LookupPublisher");
    }
 else {
      response.setAttribute("LIST","true");
      response.setAttribute(SpagoBIConstants.PUBLISHER_NAME,"LookupPublisher");
    }
    response.setAttribute("mod_val_id",modVal.getId().toString());
    response.setAttribute("LOOKUP_PARAMETER_NAME",lookupBIParameter.getParameterUrlName());
    response.setAttribute("LOOKUP_PARAMETER_ID",lookupBIParameter.getId().toString());
    String correlatedParuseId=(String)request.getAttribute("correlatedParuseIdForObjParWithId_" + lookupObjParId);
    if (correlatedParuseId != null && !correlatedParuseId.equals(""))     response.setAttribute("correlated_paruse_id",correlatedParuseId);
    return;
  }
  List errors=instance.getParametersErrors();
  if (isRefreshCorrelationCall(request)) {
    if (errors.size() > 0) {
      Iterator errorsIt=errors.iterator();
      while (errorsIt.hasNext()) {
        EMFUserError error=(EMFUserError)errorsIt.next();
        if (error instanceof EMFValidationError)         continue;
 else         errorHandler.addError(error);
      }
    }
    response.setAttribute(SpagoBIConstants.PUBLISHER_NAME,"ExecuteBIObjectPageParameter");
    return;
  }
  Iterator errorsIt=errors.iterator();
  while (errorsIt.hasNext()) {
    errorHandler.addError((EMFUserError)errorsIt.next());
  }
  if (!errorHandler.isOKBySeverity(EMFErrorSeverity.ERROR)) {
    response.setAttribute(SpagoBIConstants.PUBLISHER_NAME,"ExecuteBIObjectPageParameter");
    return;
  }
  execute(instance,null,null,response);
  logger.debug("OUT");
}

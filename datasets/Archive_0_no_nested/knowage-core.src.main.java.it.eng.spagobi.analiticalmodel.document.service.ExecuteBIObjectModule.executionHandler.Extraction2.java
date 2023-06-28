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
    BIObjectParameter lookupBIParameter=executionHandler_extraction_1(request,response);
    response.setAttribute("LOOKUP_PARAMETER_NAME",lookupBIParameter.getParameterUrlName());
    response.setAttribute("LOOKUP_PARAMETER_ID",lookupBIParameter.getId().toString());
    String correlatedParuseId=(String)request.getAttribute("correlatedParuseIdForObjParWithId_" + lookupObjParId);
    if (correlatedParuseId != null && !correlatedParuseId.equals(""))     response.setAttribute("correlated_paruse_id",correlatedParuseId);
    return;
  }
  List errors=instance.getParametersErrors();
  executionHandler_extraction_2(request,response,instance,errors);
}

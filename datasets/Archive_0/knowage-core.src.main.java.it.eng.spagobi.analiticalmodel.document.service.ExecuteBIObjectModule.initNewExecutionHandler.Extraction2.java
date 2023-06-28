/** 
 * Starts a new execution
 * @param request The Spago Request SourceBean
 * @param response The Spago Response SourceBean
 */
private void initNewExecutionHandler(SourceBean request,SourceBean response) throws Exception {
  logger.debug("IN");
  IEngUserProfile profile=getUserProfile();
  BIObject obj=getRequiredBIObject(request);
  List subObjects=getSubObjectsList(obj,profile);
  List snapshots=getSnapshotList(obj);
  List viewpoints=getViewpointList(obj);
  Snapshot snapshot=getRequiredSnapshot(request,snapshots);
  SubObject subObj=getRequiredSubObject(request,subObjects);
  String userProvidedParametersStr=(String)request.getAttribute(ObjectsTreeConstants.PARAMETERS);
  logger.debug("Used defined parameters: [" + userProvidedParametersStr + "]");
  String modality=(String)request.getAttribute(ObjectsTreeConstants.MODALITY);
  if (modality == null)   modality=SpagoBIConstants.NORMAL_EXECUTION_MODALITY;
  logger.debug("Execution modality: [" + modality + "]");
  Integer id=obj.getId();
  logger.debug("BIObject id = " + id);
  boolean canSee=ObjectsAccessVerifier.canSee(obj,profile);
  if (!canSee) {
    logger.error("Object with label = '" + obj.getLabel() + "' cannot be executed by the user!!");
    Vector v=new Vector();
    v.add(obj.getLabel());
    throw new EMFUserError(EMFErrorSeverity.ERROR,"1075",v,null);
  }
  List correctRoles=getCorrectRolesForExecution(profile,id);
  if (correctRoles == null || correctRoles.size() == 0) {
    logger.warn("Object cannot be executed by no role of the user");
    throw new EMFUserError(EMFErrorSeverity.ERROR,1006);
  }
  String role=(String)request.getAttribute(SpagoBIConstants.ROLE);
  if (role != null && !correctRoles.contains(role)) {
    logger.warn("Role [" + role + "] is not a correct role for execution");
    Vector v=new Vector();
    v.add(role);
    throw new EMFUserError(EMFErrorSeverity.ERROR,1078,v,null);
  }
  if (role == null) {
    if (snapshot != null || subObj != null) {
      role=(String)correctRoles.get(0);
    }
 else {
      if (correctRoles.size() > 1) {
        response.setAttribute(SpagoBIConstants.PUBLISHER_NAME,"ExecuteBIObjectSelectRole");
        response.setAttribute("roles",correctRoles);
        response.setAttribute(ObjectsTreeConstants.OBJECT_ID,id);
        logger.debug("more than one correct roles for execution, redirect to the role selection page");
        return;
      }
 else {
        role=(String)correctRoles.get(0);
      }
    }
  }
  ExecutionInstance instance=createExecutionInstance(id,role,profile,request,modality);
  initNewExecutionHandler_extraction_1(request,response,subObjects,snapshots,viewpoints,snapshot,subObj,userProvidedParametersStr,instance);
}

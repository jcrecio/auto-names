/** 
 * Deletes a BI Object chosen by user. If the folder id is specified, it deletes only the instance of the object in that folder. If the folder id is not specified: if the user is an administrator the object is deleted from all the folders, else it is deleted from the folder on which the user is a developer.
 * @param request The request SourceBean
 * @param mod A request string used to differentiate delete operation
 * @param response The response SourceBean
 * @throws EMFUserError If an Exception occurs
 * @throws SourceBeanException If a SourceBean Exception occurs
 */
private void delDetailObject(SourceBean request,String mod,SourceBean response,IEngUserProfile profile) throws EMFUserError, SourceBeanException {
  BIObject obj=null;
  HashMap<String,String> logParam=new HashMap();
  try {
    String idObjStr=(String)request.getAttribute(ObjectsTreeConstants.OBJECT_ID);
    Integer idObj=new Integer(idObjStr);
    IBIObjectDAO objdao=biobjDAO;
    obj=objdao.loadBIObjectById(idObj);
    if (obj != null) {
      logParam.put("Document_name",obj.getName());
      logParam.put("Document_label",obj.getLabel());
      if (obj.getId() != null)       logParam.put("Document_id",obj.getId().toString());
    }
    String idFunctStr=(String)request.getAttribute(ObjectsTreeConstants.FUNCT_ID);
    if (idFunctStr != null) {
      Integer idFunct=new Integer(idFunctStr);
      if (profile.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_ADMIN)) {
        objdao.eraseBIObject(obj,idFunct);
      }
 else {
        if (ObjectsAccessVerifier.canDev(obj.getStateCode(),idFunct,profile)) {
          objdao.eraseBIObject(obj,idFunct);
        }
      }
    }
 else {
      if (profile.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_ADMIN)) {
        if (initialPath != null && !initialPath.trim().equals("")) {
          List funcsId=obj.getFunctionalities();
          for (Iterator it=funcsId.iterator(); it.hasNext(); ) {
            Integer idFunct=(Integer)it.next();
            LowFunctionality folder=DAOFactory.getLowFunctionalityDAO().loadLowFunctionalityByID(idFunct,false);
            String folderPath=folder.getPath();
            if (folderPath.equalsIgnoreCase(initialPath) || folderPath.startsWith(initialPath + "/")) {
              objdao.eraseBIObject(obj,idFunct);
            }
          }
        }
 else {
          objdao.eraseBIObject(obj,null);
        }
      }
 else {
        List funcsId=obj.getFunctionalities();
        for (Iterator it=funcsId.iterator(); it.hasNext(); ) {
          Integer idFunct=(Integer)it.next();
          if (ObjectsAccessVerifier.canDev(obj.getStateCode(),idFunct,profile)) {
            objdao.eraseBIObject(obj,idFunct);
          }
        }
      }
    }
  }
 catch (  Exception ex) {
    logger.error("Cannot erase object",ex);
    try {
      AuditLogUtilities.updateAudit(getHttpRequest(),profile,"DOCUMENT.DELETE",logParam,"ERR");
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    throw new EMFUserError(EMFErrorSeverity.ERROR,100);
  }
  response.setAttribute("loopback","true");
  try {
    AuditLogUtilities.updateAudit(getHttpRequest(),profile,"DOCUMENT.DELETE",logParam,"OK");
  }
 catch (  Exception e) {
    e.printStackTrace();
  }
}

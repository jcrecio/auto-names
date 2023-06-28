public ListIFace getList(SourceBean request,SourceBean response) throws Exception {
  logger.debug("IN");
  RequestContainer requestContainer=this.getRequestContainer();
  SessionContainer sessionContainer=requestContainer.getSessionContainer();
  SessionContainer permanentSession=sessionContainer.getPermanentContainer();
  profile=(IEngUserProfile)permanentSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
  String currentFieldOrder=(request.getAttribute("FIELD_ORDER") == null || ((String)request.getAttribute("FIELD_ORDER")).equals("")) ? "" : (String)request.getAttribute("FIELD_ORDER");
  if (currentFieldOrder.equals("")) {
    currentFieldOrder="label";
    request.delAttribute("FIELD_ORDER");
    request.setAttribute("FIELD_ORDER",currentFieldOrder);
  }
  String currentTypOrder=(request.getAttribute("TYPE_ORDER") == null || ((String)request.getAttribute("TYPE_ORDER")).equals("")) ? "" : (String)request.getAttribute("TYPE_ORDER");
  if (currentTypOrder.equals("")) {
    currentTypOrder=" ASC";
    request.delAttribute("TYPE_ORDER");
    request.setAttribute("TYPE_ORDER",currentTypOrder);
  }
  String modality=ChannelUtilities.getPreferenceValue(requestContainer,BIObjectsModule.MODALITY,BIObjectsModule.ENTIRE_TREE);
  if (modality != null && modality.equalsIgnoreCase(BIObjectsModule.FILTER_TREE)) {
    initialPath=(String)ChannelUtilities.getPreferenceValue(requestContainer,TreeObjectsModule.PATH_SUBTREE,"");
  }
  String objIdStr=(String)sessionContainer.getAttribute("SUBJECT_ID");
  Integer objId=null;
  if (objIdStr != null)   objId=new Integer(objIdStr);
  response.setAttribute("SUBJECT_ID",objIdStr);
  PaginatorIFace paginator=new GenericPaginator();
  IBIObjectDAO objDAO=DAOFactory.getBIObjectDAO();
  List objectsList=null;
  if (initialPath != null && !initialPath.trim().equals("")) {
    objectsList=objDAO.loadAllBIObjectsFromInitialPath(initialPath,currentFieldOrder + " " + currentTypOrder);
  }
 else {
    objectsList=objDAO.loadAllBIObjects(currentFieldOrder + " " + currentTypOrder);
  }
  String checked=(String)request.getAttribute("checked");
  if (checked == null)   checked=(String)request.getAttribute("optChecked");
  if (checked == null) {
    checked="true";
  }
  request.delAttribute("optChecked");
  request.setAttribute("optChecked",checked);
  response.setAttribute("optChecked",(String)request.getAttribute("optChecked"));
  if (checked.equals("true")) {
    for (Iterator it=objectsList.iterator(); it.hasNext(); ) {
      BIObject obj=(BIObject)it.next();
      if (objId != null && obj.getId().equals(objId))       continue;
      if (isCheckedObject(obj.getId().toString())) {
        SourceBean rowSB=null;
        if (profile.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_ADMIN)) {
          rowSB=makeAdminListRow(obj);
        }
 else         if (!profile.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_ADMIN) && profile.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_DEV)) {
          rowSB=makeDevListRow(obj);
        }
        if (rowSB != null)         paginator.addRow(rowSB);
      }
    }
  }
 else   getList_extraction_2(objId,paginator,objectsList,checked);
  ListIFace list=new GenericList();
  list.setPaginator(paginator);
  logger.debug("OUT");
  return list;
}

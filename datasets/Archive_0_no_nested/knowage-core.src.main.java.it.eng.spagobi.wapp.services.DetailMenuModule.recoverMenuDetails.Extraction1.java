private Menu recoverMenuDetails(SourceBean request,String mod) throws EMFUserError, SourceBeanException {
  String name=(String)request.getAttribute("name");
  name=name.trim();
  String description=(String)request.getAttribute("description");
  description=description.trim();
  List attrsList=request.getAttributeAsList(DetailMenuModule.ROLES);
  Role[] roles=new Role[attrsList.size()];
  for (int i=0; i < roles.length; i++) {
    String idRoleStr=(String)attrsList.get(i);
    roles[i]=DAOFactory.getRoleDAO().loadByID(new Integer(idRoleStr));
  }
  Menu menu=null;
  if (mod.equalsIgnoreCase(AdmintoolsConstants.DETAIL_INS)) {
    String idParent=(String)request.getAttribute(DetailMenuModule.PARENT_ID);
    menu=new Menu();
    menu.setHasChildren(false);
    if (idParent != null)     menu.setParentId(Integer.valueOf(idParent));
 else     menu.setParentId(null);
  }
 else   if (mod.equalsIgnoreCase(AdmintoolsConstants.DETAIL_MOD)) {
    String idMenu=(String)request.getAttribute(DetailMenuModule.MENU_ID);
    menu=DAOFactory.getMenuDAO().loadMenuByID(Integer.valueOf(idMenu));
  }
  menu.setName(name);
  menu.setDescr(description);
  menu.setRoles(roles);
  HashMap<String,String> logParam=new HashMap();
  logParam.put("NAME",name);
  SessionContainer permSess=getRequestContainer().getSessionContainer().getPermanentContainer();
  IEngUserProfile profile=(IEngUserProfile)permSess.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
  if (name.equalsIgnoreCase("")) {
    try {
      AuditLogUtilities.updateAudit(getHttpRequest(),profile,"MENU.ADD/MODIFY",logParam,"OK");
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
    throw new EMFUserError(EMFErrorSeverity.ERROR,"10003",messageBundle);
  }
  String nodeContent=(String)request.getAttribute("nodeContent");
  if ("nodeDocument".equals(nodeContent)) {
    String objectId=(String)request.getAttribute(DetailMenuModule.MENU_OBJ);
    menu.setObjId(Integer.valueOf(objectId));
    String objParameters=(String)request.getAttribute("objParameters");
    if (objParameters != null && !objParameters.trim().equals("")) {
      menu.setObjParameters(objParameters);
    }
 else {
      menu.setObjParameters(null);
    }
    String subobjectName=(String)request.getAttribute("subobjectName");
    recoverMenuDetails_extraction_2(request,menu,subobjectName);
  }
 else   if ("nodeStaticPage".equals(nodeContent)) {
    menu.setExternalApplicationUrl(null);
    menu.setObjId(null);
    menu.setSubObjName(null);
    menu.setObjParameters(null);
    menu.setSnapshotName(null);
    menu.setSnapshotHistory(null);
    menu.setFunctionality(null);
    menu.setInitialPath(null);
    menu.setHideToolbar(false);
    menu.setHideSliders(false);
    String staticPage=(String)request.getAttribute("staticpage");
    menu.setStaticPage(staticPage);
  }
 else   if ("nodeFunctionality".equals(nodeContent)) {
    menu.setObjId(null);
    menu.setSubObjName(null);
    menu.setObjParameters(null);
    menu.setSnapshotName(null);
    menu.setSnapshotHistory(null);
    menu.setStaticPage(null);
    menu.setExternalApplicationUrl(null);
    menu.setHideToolbar(false);
    menu.setHideSliders(false);
    String functionality=(String)request.getAttribute("functionality");
    menu.setFunctionality(functionality);
    if (functionality.equals(SpagoBIConstants.DOCUMENT_BROWSER_USER)) {
      String initialPath=(String)request.getAttribute("initialPath");
      menu.setInitialPath(initialPath);
    }
 else {
      menu.setInitialPath(null);
    }
  }
 else   if ("nodeExternalApp".equals(nodeContent)) {
    String url=(String)request.getAttribute(DetailMenuModule.EXT_APP_URL);
    menu.setExternalApplicationUrl(url);
    menu.setObjId(null);
    menu.setSubObjName(null);
    menu.setObjParameters(null);
    menu.setSnapshotName(null);
    menu.setSnapshotHistory(null);
    menu.setStaticPage(null);
    menu.setFunctionality(null);
    menu.setInitialPath(null);
    menu.setHideToolbar(false);
    menu.setHideSliders(false);
  }
 else {
    menu.setObjId(null);
    menu.setSubObjName(null);
    menu.setObjParameters(null);
    menu.setSnapshotName(null);
    menu.setSnapshotHistory(null);
    menu.setStaticPage(null);
    menu.setExternalApplicationUrl(null);
    menu.setFunctionality(null);
    menu.setInitialPath(null);
    menu.setHideToolbar(false);
    menu.setHideSliders(false);
  }
  String viewIconsB=(String)request.getAttribute("viewicons");
  if (viewIconsB != null)   menu.setViewIcons(Boolean.valueOf(viewIconsB).booleanValue());
 else   menu.setViewIcons(false);
  return menu;
}

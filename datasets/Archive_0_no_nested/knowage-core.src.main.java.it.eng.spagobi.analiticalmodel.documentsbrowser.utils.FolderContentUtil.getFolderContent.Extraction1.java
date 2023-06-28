public JSONObject getFolderContent(LowFunctionality folder,SourceBean request,SourceBean response,HttpServletRequest httpRequest,SessionContainer sessCont) throws Exception {
  List functionalities;
  List objects;
  boolean isHome=false;
  if (folder == null) {
    Config documentBrowserHomeConfig=DAOFactory.getSbiConfigDAO().loadConfigParametersByLabel("SPAGOBI.DOCUMENTBROWSER.HOME");
    if (documentBrowserHomeConfig != null) {
      if (documentBrowserHomeConfig.isActive()) {
        String folderLabel=documentBrowserHomeConfig.getValueCheck();
        if (!StringUtils.isEmpty(folderLabel)) {
          folder=DAOFactory.getLowFunctionalityDAO().loadLowFunctionalityByCode(folderLabel,false);
        }
      }
    }
  }
  if (folder == null || String.valueOf(folder.getId()).equalsIgnoreCase(ROOT_NODE_ID)) {
    folder=DAOFactory.getLowFunctionalityDAO().loadRootLowFunctionality(false);
  }
  SessionContainer permCont=sessCont.getPermanentContainer();
  IEngUserProfile profile=(IEngUserProfile)permCont.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
  if (UserUtilities.isAdministrator(profile)) {
    isHome=UserUtilities.isAPersonalFolder(folder);
  }
 else {
    isHome=UserUtilities.isPersonalFolder(folder,(UserProfile)profile);
  }
  List allSubDocuments=null;
  Config documentBrowserRecursiveConfig=DAOFactory.getSbiConfigDAO().loadConfigParametersByLabel("SPAGOBI.DOCUMENTBROWSER.RECURSIVE");
  if (documentBrowserRecursiveConfig.isActive()) {
    String propertyValue=documentBrowserRecursiveConfig.getValueCheck();
    if ((!StringUtils.isEmpty(propertyValue)) && (propertyValue.equalsIgnoreCase("true"))) {
      allSubDocuments=getAllSubDocuments(String.valueOf(folder.getId()),profile,isHome);
    }
  }
  objects=getFolderContent_extraction_2(folder,isHome,profile,allSubDocuments);
  MessageBuilder m=new MessageBuilder();
  Locale locale=m.getLocale(httpRequest);
  JSONArray documentsJSON=(JSONArray)SerializerFactory.getSerializer("application/json").serialize(objects,locale);
  DocumentsJSONDecorator.decorateDocuments(documentsJSON,profile,folder);
  JSONObject documentsResponseJSON=createJSONResponseDocuments(documentsJSON);
  functionalities=DAOFactory.getLowFunctionalityDAO().loadUserFunctionalities(folder.getId(),false,profile);
  JSONArray foldersJSON=(JSONArray)SerializerFactory.getSerializer("application/json").serialize(functionalities,locale);
  JSONObject exportAction=new JSONObject();
  exportAction.put("name","export");
  exportAction.put("description","Export");
  JSONObject scheduleAction=new JSONObject();
  scheduleAction.put("name","schedule");
  scheduleAction.put("description","Schedule");
  JSONObject foldersResponseJSON;
  Config documentBrowserFlatConfig=DAOFactory.getSbiConfigDAO().loadConfigParametersByLabel("SPAGOBI.DOCUMENTBROWSER.FLAT");
  if (documentBrowserFlatConfig.isActive()) {
    String propertyValue=documentBrowserFlatConfig.getValueCheck();
    if ((!StringUtils.isEmpty(propertyValue)) && (propertyValue.equalsIgnoreCase("true"))) {
      foldersJSON=new JSONArray();
    }
  }
  foldersResponseJSON=createJSONResponseFolders(foldersJSON);
  JSONObject canAddResponseJSON=null;
  return createJSONResponse(foldersResponseJSON,documentsResponseJSON,canAddResponseJSON);
}

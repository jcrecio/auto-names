private void addItemForJSTree(StringBuffer htmlStream,LowFunctionality folder,BIObject obj,boolean isRoot,boolean isInitialPath,boolean isDefaultForNew){
  logger.debug("IN");
  String nameLabel=folder.getName();
  String name=msgBuilder.getMessage(nameLabel,"messages",httpRequest);
  name=StringUtils.escapeForHtml(name);
  String codeType=folder.getCodType();
  Integer id=folder.getId();
  Integer parentId=null;
  if (isInitialPath)   parentId=new Integer(dTreeRootId);
 else   parentId=folder.getParentId();
  if (codeType.equalsIgnoreCase(SpagoBIConstants.LOW_FUNCTIONALITY_TYPE_CODE)) {
    if (isRoot) {
      htmlStream.append("	treeFunctIns.add(" + id + ", "+ dTreeRootId+ ",'"+ name+ "', '', '', '', '', '', 'true');\n");
    }
 else {
      if (codeType.equalsIgnoreCase(SpagoBIConstants.LOW_FUNCTIONALITY_TYPE_CODE)) {
        String imgFolder=urlBuilder.getResourceLinkByTheme(httpRequest,"/img/treefolder.gif",currTheme);
        String imgFolderOp=urlBuilder.getResourceLinkByTheme(httpRequest,"/img/treefolderopen.gif",currTheme);
        addItemForJSTree_extraction_1(htmlStream,obj,isDefaultForNew,name,id,parentId,imgFolder,imgFolderOp);
      }
    }
  }
  if (codeType.equalsIgnoreCase(SpagoBIConstants.USER_FUNCTIONALITY_TYPE_CODE)) {
    if (!privateFolderCreated) {
      privateFolderCreated=true;
      htmlStream.append("	treeFunctIns.add(" + dMyFolderRootId + ", "+ dTreeRootId+ ",'"+ "Personal Folders"+ "', '', '', '', '', '', false);\n");
    }
    String imgFolder=urlBuilder.getResourceLinkByTheme(httpRequest,"/img/treefolderuser.gif",currTheme);
    String imgFolderOp=urlBuilder.getResourceLinkByTheme(httpRequest,"/img/treefolderopenuser.gif",currTheme);
    addItemForJSTree_extraction_2(htmlStream,obj,name,id,parentId,imgFolder,imgFolderOp);
  }
  logger.debug("OUT");
}

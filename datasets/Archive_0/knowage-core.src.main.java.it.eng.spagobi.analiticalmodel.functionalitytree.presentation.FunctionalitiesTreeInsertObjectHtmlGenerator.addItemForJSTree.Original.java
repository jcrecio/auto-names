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
        try {
          if (profile.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_ADMIN) || ObjectsAccessVerifier.canDev(id,profile)) {
            boolean checked=false;
            if (obj != null) {
              List funcs=obj.getFunctionalities();
              if (funcs.contains(id) || isDefaultForNew) {
                checked=true;
              }
            }
            htmlStream.append("	treeFunctIns.add(" + id + ", "+ parentId+ ",'"+ name+ "', '', '', '', '"+ imgFolder+ "', '"+ imgFolderOp+ "', '', '', '"+ ObjectsTreeConstants.FUNCT_ID+ "', '"+ id+ "',"+ checked+ ");\n");
          }
 else           if (ObjectsAccessVerifier.canExec(id,profile)) {
            htmlStream.append("	treeFunctIns.add(" + id + ", "+ parentId+ ",'"+ name+ "', '', '', '', '"+ imgFolder+ "', '"+ imgFolderOp+ "', '', '', '', '',false);\n");
          }
        }
 catch (        Exception ex) {
          logger.error("Error in adding items " + ex.getMessage());
        }
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
    try {
      if (profile.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_ADMIN) || ObjectsAccessVerifier.canDev(id,profile)) {
        boolean checked=false;
        if (obj != null) {
          List funcs=obj.getFunctionalities();
          if (funcs.contains(id))           checked=true;
        }
        htmlStream.append("	treeFunctIns.add(" + id + ", "+ dMyFolderRootId+ ",'"+ name+ "', '', '', '', '"+ imgFolder+ "', '"+ imgFolderOp+ "', '', '', '"+ ObjectsTreeConstants.FUNCT_ID+ "', '"+ id+ "',"+ checked+ ");\n");
      }
 else       if (ObjectsAccessVerifier.canExec(id,profile)) {
        htmlStream.append("	treeFunctIns.add(" + id + ", "+ parentId+ ",'"+ name+ "', '', '', '', '"+ imgFolder+ "', '"+ imgFolderOp+ "', '', '', '', '',false);\n");
      }
    }
 catch (    Exception ex) {
      logger.error("Error in adding items " + ex.getMessage());
    }
  }
  logger.debug("OUT");
}

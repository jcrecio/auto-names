private void addItemForJSTree(StringBuffer htmlStream,LowFunctionality folder,boolean isRoot,boolean isInitialPath){
  String nameLabel=folder.getName();
  String name=msgBuilder.getMessage(nameLabel,"messages",httpRequest);
  String codeType=folder.getCodType();
  Integer idFolder=folder.getId();
  Integer parentId=null;
  if (isInitialPath)   parentId=new Integer(dTreeRootId);
 else   parentId=folder.getParentId();
  if (isRoot) {
    htmlStream.append("	treeCMS.add(" + idFolder + ", "+ dTreeRootId+ ",'"+ name+ "', '', '', '', '', '', 'true');\n");
  }
 else {
    if (codeType.equalsIgnoreCase(SpagoBIConstants.LOW_FUNCTIONALITY_TYPE_CODE)) {
      addItemForJSTree_extraction_1(htmlStream,folder,name,idFolder,parentId);
    }
  }
}

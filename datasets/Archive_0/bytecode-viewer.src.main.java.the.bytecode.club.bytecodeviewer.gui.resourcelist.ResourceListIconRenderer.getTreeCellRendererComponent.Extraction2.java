@Override public Component getTreeCellRendererComponent(JTree tree,Object value,boolean sel,boolean expanded,boolean leaf,int row,boolean hasFocus){
  Component ret=super.getTreeCellRendererComponent(tree,value,selected,expanded,leaf,row,hasFocus);
  if (value instanceof ResourceTreeNode) {
    if (iconCache.containsKey(value)) {
      setIcon(iconCache.get(value));
      return ret;
    }
    ResourceTreeNode node=(ResourceTreeNode)value;
    String nameOG=node.toString();
    String name=nameOG.toLowerCase();
    String onlyName=FilenameUtils.getName(name);
    boolean iconSet=false;
    ResourceType knownResourceType=onlyName.contains(":") ? null : ResourceType.extensionMap.get(FilenameUtils.getExtension(onlyName).toLowerCase());
    if (knownResourceType != null && (node.getParent() == node.getRoot() || node.getChildCount() == 0)) {
      cacheNodeIcon(node,knownResourceType.getIcon());
      iconSet=true;
    }
 else     if (nameOG.equals("Decoded Resources") && node.getChildCount() > 0) {
      cacheNodeIcon(node,IconResources.decodedIcon);
      iconSet=true;
    }
 else     if (node.getChildCount() == 0 && (nameOG.equals("README") || nameOG.equals("LICENSE") || nameOG.equals("NOTICE"))) {
      cacheNodeIcon(node,IconResources.textIcon);
      iconSet=true;
    }
    getTreeCellRendererComponent_extraction_1(node,iconSet,knownResourceType);
  }
  return ret;
}

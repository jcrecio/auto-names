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
    if (node.getChildCount() > 0) {
      List<TreeNode> nodes=new ArrayList<>();
      List<TreeNode> totalNodes=new ArrayList<>();
      nodes.add(node);
      totalNodes.add(node);
      boolean isJava=false;
      boolean finished=false;
      while (!finished) {
        if (nodes.isEmpty())         finished=true;
 else {
          isJava=getTreeCellRendererComponent_extraction_2(nodes,totalNodes,isJava);
        }
      }
      if (!iconSet) {
        if (isJava)         cacheNodeIcon(node,IconResources.packagesIcon);
 else         cacheNodeIcon(node,IconResources.folderIcon);
      }
    }
 else     if (knownResourceType == null && !iconSet)     cacheNodeIcon(node,IconResources.unknownFileIcon);
  }
  return ret;
}

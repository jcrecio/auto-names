private void buildTree(ResourceContainer container,ResourceTreeNode root){
  if (!container.resourceClasses.isEmpty()) {
    for (    String name : container.resourceClasses.keySet()) {
      final String[] spl=name.split("/");
      if (spl.length < 2) {
        root.add(new ResourceTreeNode(name + ".class"));
      }
 else {
        ResourceTreeNode parent=root;
        buildTree_extraction_1(spl,parent);
      }
    }
  }
  if (!container.resourceFiles.isEmpty()) {
    for (    final Entry<String,byte[]> entry : container.resourceFiles.entrySet()) {
      String name=entry.getKey();
      final String[] spl=name.split("/");
      if (spl.length < 2) {
        root.add(new ResourceTreeNode(name));
      }
 else {
        ResourceTreeNode parent=root;
        buildTree_extraction_2(spl,parent);
      }
    }
  }
}

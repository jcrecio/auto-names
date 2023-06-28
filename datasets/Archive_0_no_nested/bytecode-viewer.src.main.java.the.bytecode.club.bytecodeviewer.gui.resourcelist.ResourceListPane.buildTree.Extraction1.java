private void buildTree(ResourceContainer container,ResourceTreeNode root){
  if (!container.resourceClasses.isEmpty()) {
    for (    String name : container.resourceClasses.keySet()) {
      final String[] spl=name.split("/");
      if (spl.length < 2) {
        root.add(new ResourceTreeNode(name + ".class"));
      }
 else {
        ResourceTreeNode parent=root;
        for (int i1=0; i1 < spl.length; i1++) {
          String s=spl[i1];
          if (i1 == spl.length - 1)           s+=".class";
          ResourceTreeNode child=null;
          for (int i=0; i < parent.getChildCount(); i++) {
            if (((ResourceTreeNode)parent.getChildAt(i)).getUserObject().equals(s)) {
              child=(ResourceTreeNode)parent.getChildAt(i);
              break;
            }
          }
          if (child == null) {
            child=new ResourceTreeNode(s);
            parent.add(child);
          }
          parent=child;
        }
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

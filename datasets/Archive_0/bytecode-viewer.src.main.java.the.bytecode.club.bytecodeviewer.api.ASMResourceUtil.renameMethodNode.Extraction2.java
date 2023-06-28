public static void renameMethodNode(String originalParentName,String originalMethodName,String originalMethodDesc,String newParent,String newName,String newDesc){
  for (  ClassNode c : BytecodeViewer.getLoadedClasses()) {
    for (    Object o : c.methods.toArray()) {
      renameMethodNode_extraction_1(originalParentName,originalMethodName,originalMethodDesc,newParent,newName,newDesc,c,o);
    }
  }
}

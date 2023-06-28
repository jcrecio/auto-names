public static void renameMethodNode(String originalParentName,String originalMethodName,String originalMethodDesc,String newParent,String newName,String newDesc){
  for (  ClassNode c : BytecodeViewer.getLoadedClasses()) {
    for (    Object o : c.methods.toArray()) {
      MethodNode m=(MethodNode)o;
      for (      AbstractInsnNode i : m.instructions.toArray()) {
        renameMethodNode_extraction_2(originalParentName,originalMethodName,originalMethodDesc,newParent,newName,newDesc,i);
      }
      if (m.signature != null) {
        if (newName != null)         m.signature=m.signature.replace(originalMethodName,newName);
        if (newParent != null)         m.signature=m.signature.replace(originalParentName,newParent);
      }
      if (m.name.equals(originalMethodName) && m.desc.equals(originalMethodDesc) && c.name.equals(originalParentName)) {
        if (newName != null)         m.name=newName;
        if (newDesc != null)         m.desc=newDesc;
      }
    }
  }
}

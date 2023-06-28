public static void renameMethodNode(String originalParentName,String originalMethodName,String originalMethodDesc,String newParent,String newName,String newDesc){
  for (  ClassNode c : BytecodeViewer.getLoadedClasses()) {
    for (    Object o : c.methods.toArray()) {
      MethodNode m=(MethodNode)o;
      for (      AbstractInsnNode i : m.instructions.toArray()) {
        if (i instanceof MethodInsnNode) {
          MethodInsnNode mi=(MethodInsnNode)i;
          if (mi.owner.equals(originalParentName) && mi.name.equals(originalMethodName) && mi.desc.equals(originalMethodDesc)) {
            if (newParent != null)             mi.owner=newParent;
            if (newName != null)             mi.name=newName;
            if (newDesc != null)             mi.desc=newDesc;
          }
        }
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

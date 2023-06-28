public void scanClassNode(ClassNode classNode){
  for (  Object o : classNode.fields.toArray()) {
    FieldNode f=(FieldNode)o;
    Object v=f.value;
    if (v instanceof String) {
      String s=(String)v;
      if (contains) {
        if (s.contains(originalLDC))         f.value=((String)f.value).replaceAll(originalLDC,newLDC);
      }
 else {
        if (s.equals(originalLDC))         f.value=newLDC;
      }
    }
    if (v instanceof String[]) {
      for (int i=0; i < ((String[])v).length; i++) {
        scanClassNode_extraction_2(classNode,f,v,i);
      }
    }
  }
  for (  Object o : classNode.methods.toArray()) {
    MethodNode m=(MethodNode)o;
    InsnList iList=m.instructions;
    for (    AbstractInsnNode a : iList.toArray()) {
      if (a instanceof LdcInsnNode) {
        scanClassNode_extraction_3(classNode,m,a);
      }
    }
  }
}

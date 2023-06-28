public void scanClassNode(ClassNode classNode){
  for (  Object o : classNode.fields.toArray()) {
    FieldNode f=(FieldNode)o;
    Object v=f.value;
    if (v instanceof String) {
      scanClassNode_extraction_1(f,v);
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

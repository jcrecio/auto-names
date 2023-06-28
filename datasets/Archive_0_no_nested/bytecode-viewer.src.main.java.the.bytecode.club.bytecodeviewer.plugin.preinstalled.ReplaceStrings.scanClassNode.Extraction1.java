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
        String s=((String[])v)[i];
        if (contains) {
          if (s.contains(originalLDC)) {
            f.value=((String[])f.value)[i].replaceAll(originalLDC,newLDC);
            String ugh=s.replaceAll("\\n","\\\\n").replaceAll("\\r","\\\\r");
            frame.appendText(classNode.name + "." + f.name+ ""+ f.desc+ " -> \""+ ugh+ "\" replaced with \""+ s.replaceAll(originalLDC,newLDC)+ "\"");
          }
        }
 else {
          if (s.equals(originalLDC)) {
            ((String[])f.value)[i]=newLDC;
            String ugh=s.replaceAll("\\n","\\\\n").replaceAll("\\r","\\\\r");
            frame.appendText(classNode.name + "." + f.name+ ""+ f.desc+ " -> \""+ ugh+ "\" replaced with \""+ newLDC+ "\"");
          }
        }
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

@Override public void execute(List<ClassNode> classNodeList){
  PluginConsole frame=new PluginConsole("Show All Strings");
  StringBuilder sb=new StringBuilder();
  for (  ClassNode classNode : classNodeList) {
    for (    Object o : classNode.fields.toArray()) {
      FieldNode f=(FieldNode)o;
      Object v=f.value;
      execute_extraction_1(sb,classNode,f,v);
    }
    for (    Object o : classNode.methods.toArray()) {
      MethodNode m=(MethodNode)o;
      InsnList iList=m.instructions;
      for (      AbstractInsnNode a : iList.toArray()) {
        if (a instanceof LdcInsnNode) {
          execute_extraction_2(sb,classNode,m,a);
        }
      }
    }
  }
  frame.setText(sb.toString());
  frame.setVisible(true);
}

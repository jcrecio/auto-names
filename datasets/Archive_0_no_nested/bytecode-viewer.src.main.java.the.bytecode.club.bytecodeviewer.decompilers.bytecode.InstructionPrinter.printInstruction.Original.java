public String printInstruction(AbstractInsnNode ain){
  String line="";
  if (ain instanceof VarInsnNode) {
    line=printVarInsnNode((VarInsnNode)ain);
  }
 else   if (ain instanceof IntInsnNode) {
    line=printIntInsnNode((IntInsnNode)ain);
  }
 else   if (ain instanceof FieldInsnNode) {
    line=printFieldInsnNode((FieldInsnNode)ain);
  }
 else   if (ain instanceof MethodInsnNode) {
    line=printMethodInsnNode((MethodInsnNode)ain);
  }
 else   if (ain instanceof LdcInsnNode) {
    line=printLdcInsnNode((LdcInsnNode)ain);
  }
 else   if (ain instanceof InsnNode) {
    line=printInsnNode((InsnNode)ain);
  }
 else   if (ain instanceof JumpInsnNode) {
    line=printJumpInsnNode((JumpInsnNode)ain);
  }
 else   if (ain instanceof LineNumberNode) {
    line=printLineNumberNode();
  }
 else   if (ain instanceof LabelNode) {
    if (firstLabel && BytecodeViewer.viewer.appendBracketsToLabels.isSelected())     info.add("}");
    line=printLabelnode((LabelNode)ain);
    if (BytecodeViewer.viewer.appendBracketsToLabels.isSelected()) {
      if (!firstLabel)       firstLabel=true;
      line+=" {";
    }
  }
 else   if (ain instanceof TypeInsnNode) {
    line=printTypeInsnNode((TypeInsnNode)ain);
  }
 else   if (ain instanceof FrameNode) {
    line=printFrameNode((FrameNode)ain);
  }
 else   if (ain instanceof IincInsnNode) {
    line=printIincInsnNode((IincInsnNode)ain);
  }
 else   if (ain instanceof TableSwitchInsnNode) {
    line=printTableSwitchInsnNode((TableSwitchInsnNode)ain);
  }
 else   if (ain instanceof LookupSwitchInsnNode) {
    line=printLookupSwitchInsnNode((LookupSwitchInsnNode)ain);
  }
 else   if (ain instanceof InvokeDynamicInsnNode) {
    line=printInvokeDynamicInsNode((InvokeDynamicInsnNode)ain);
  }
 else   if (ain instanceof MultiANewArrayInsnNode) {
    line=printMultiANewArrayInsNode((MultiANewArrayInsnNode)ain);
  }
 else {
    line+="UNADDED OPCODE: " + nameOpcode(ain.getOpcode()) + " "+ ain;
  }
  return line;
}

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
      printInstruction_extraction_1();
      line+=" {";
    }
  }
 else   if (ain instanceof TypeInsnNode) {
    line=printTypeInsnNode((TypeInsnNode)ain);
  }
 else   line=printInstruction_extraction_2(ain,line);
  return line;
}

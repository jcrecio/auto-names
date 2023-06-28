public static PrefixedStringBuilder decompile(PrefixedStringBuilder sb,MethodNode m,ClassNode cn){
  String class_;
  if (cn.name.contains("/")) {
    class_=cn.name.substring(cn.name.lastIndexOf("/") + 1);
  }
 else {
    class_=cn.name;
  }
  String s=getAccessString(m.access);
  sb.append("     ");
  sb.append(s);
  if (s.length() > 0)   sb.append(" ");
  if (m.name.equals("<init>")) {
    sb.append(class_);
  }
 else   if (!m.name.equals("<clinit>")) {
    sb.append(m.name);
  }
  TypeAndName[] args=decompile_extraction_1(sb,m);
  int amountOfThrows=m.exceptions.size();
  if (amountOfThrows > 0) {
    sb.append(" throws ");
    sb.append(m.exceptions.get(0));
    for (int i=1; i < amountOfThrows; i++) {
      sb.append(", ");
      sb.append(m.exceptions.get(i));
    }
  }
  if (s.contains("abstract")) {
    sb.append(" {}");
    sb.append(" //");
    sb.append(m.desc);
    sb.append(nl);
  }
 else {
    InstructionPrinter insnPrinter=decompile_extraction_2(sb,m,args);
    for (    String insn : insnPrinter.createPrint()) {
      sb.append("         ");
      sb.append(insn);
      sb.append(nl);
    }
    sb.append("     }" + nl);
  }
  return sb;
}

protected void toString(StringBuilder buf){
  if (parent == null) {
    buf.append('$');
  }
 else {
    parent.toString(buf);
    if (fieldName == null) {
      buf.append(".null");
    }
 else     if (fieldName instanceof Integer) {
      buf.append('[');
      buf.append(((Integer)fieldName).intValue());
      buf.append(']');
    }
 else {
      buf.append('.');
      String fieldName=this.fieldName.toString();
      boolean special=false;
      special=toString_extraction_1(fieldName,special);
      if (special) {
        for (int i=0; i < fieldName.length(); ++i) {
          char ch=fieldName.charAt(i);
          toString_extraction_2(buf,ch);
        }
      }
 else {
        buf.append(fieldName);
      }
    }
  }
}

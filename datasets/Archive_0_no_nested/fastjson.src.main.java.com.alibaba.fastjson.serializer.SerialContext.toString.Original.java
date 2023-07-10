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
      for (int i=0; i < fieldName.length(); ++i) {
        char ch=fieldName.charAt(i);
        if ((ch >= '0' && ch <= '9') || (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z')|| ch > 128) {
          continue;
        }
        special=true;
        break;
      }
      if (special) {
        for (int i=0; i < fieldName.length(); ++i) {
          char ch=fieldName.charAt(i);
          if (ch == '\\') {
            buf.append('\\');
            buf.append('\\');
            buf.append('\\');
          }
 else           if ((ch >= '0' && ch <= '9') || (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z')|| ch > 128) {
            buf.append(ch);
            continue;
          }
 else           if (ch == '\"') {
            buf.append('\\');
            buf.append('\\');
            buf.append('\\');
          }
 else {
            buf.append('\\');
            buf.append('\\');
          }
          buf.append(ch);
        }
      }
 else {
        buf.append(fieldName);
      }
    }
  }
}

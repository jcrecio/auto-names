public void write(List<String> list){
  if (list.isEmpty()) {
    write("[]");
    return;
  }
  int offset=count;
  final int initOffset=offset;
  for (int i=0, list_size=list.size(); i < list_size; ++i) {
    String text=list.get(i);
    boolean hasSpecial=false;
    if (text == null) {
      hasSpecial=true;
    }
 else {
      for (int j=0, len=text.length(); j < len; ++j) {
        char ch=text.charAt(j);
        if (hasSpecial=(ch < ' ' || ch > '~' || ch == '"' || ch == '\\')) {
          break;
        }
      }
    }
    if (hasSpecial) {
      count=initOffset;
      write('[');
      for (int j=0; j < list.size(); ++j) {
        text=list.get(j);
        if (j != 0) {
          write(',');
        }
        if (text == null) {
          write("null");
        }
 else {
          writeStringWithDoubleQuote(text,(char)0);
        }
      }
      write(']');
      return;
    }
    int newcount=offset + text.length() + 3;
    if (i == list.size() - 1) {
      newcount++;
    }
    if (newcount > buf.length) {
      count=offset;
      expandCapacity(newcount);
    }
    if (i == 0) {
      buf[offset++]='[';
    }
 else {
      buf[offset++]=',';
    }
    buf[offset++]='"';
    text.getChars(0,text.length(),buf,offset);
    offset+=text.length();
    buf[offset++]='"';
  }
  buf[offset++]=']';
  count=offset;
}

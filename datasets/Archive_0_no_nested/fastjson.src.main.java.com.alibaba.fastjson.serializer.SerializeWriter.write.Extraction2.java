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
      text=write_extraction_1(list,text);
      write(']');
      return;
    }
    int newcount=offset + text.length() + 3;
    offset=write_extraction_2(list,offset,i,newcount);
    text.getChars(0,text.length(),buf,offset);
    offset+=text.length();
    buf[offset++]='"';
  }
  buf[offset++]=']';
  count=offset;
}

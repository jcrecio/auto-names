protected void writeStringWithSingleQuote(String text){
  if (text == null) {
    int newcount=count + 4;
    if (newcount > buf.length) {
      expandCapacity(newcount);
    }
    "null".getChars(0,4,buf,count);
    count=newcount;
    return;
  }
  int len=text.length();
  int newcount=count + len + 2;
  if (newcount > buf.length) {
    if (writer != null) {
      write('\'');
      for (int i=0; i < text.length(); ++i) {
        char ch=text.charAt(i);
        if (ch <= 13 || ch == '\\' || ch == '\'' || (ch == '/' && isEnabled(SerializerFeature.WriteSlashAsSpecial))) {
          write('\\');
          write(replaceChars[(int)ch]);
        }
 else {
          write(ch);
        }
      }
      write('\'');
      return;
    }
    expandCapacity(newcount);
  }
  int start=count + 1;
  writeStringWithSingleQuote_extraction_2(text,len,newcount,start);
}

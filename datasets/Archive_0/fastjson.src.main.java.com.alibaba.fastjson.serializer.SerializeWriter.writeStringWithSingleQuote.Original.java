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
  int end=start + len;
  buf[count]='\'';
  text.getChars(0,len,buf,start);
  count=newcount;
  int specialCount=0;
  int lastSpecialIndex=-1;
  char lastSpecial='\0';
  for (int i=start; i < end; ++i) {
    char ch=buf[i];
    if (ch <= 13 || ch == '\\' || ch == '\'' || (ch == '/' && isEnabled(SerializerFeature.WriteSlashAsSpecial))) {
      specialCount++;
      lastSpecialIndex=i;
      lastSpecial=ch;
    }
  }
  newcount+=specialCount;
  if (newcount > buf.length) {
    expandCapacity(newcount);
  }
  count=newcount;
  if (specialCount == 1) {
    System.arraycopy(buf,lastSpecialIndex + 1,buf,lastSpecialIndex + 2,end - lastSpecialIndex - 1);
    buf[lastSpecialIndex]='\\';
    buf[++lastSpecialIndex]=replaceChars[(int)lastSpecial];
  }
 else   if (specialCount > 1) {
    System.arraycopy(buf,lastSpecialIndex + 1,buf,lastSpecialIndex + 2,end - lastSpecialIndex - 1);
    buf[lastSpecialIndex]='\\';
    buf[++lastSpecialIndex]=replaceChars[(int)lastSpecial];
    end++;
    for (int i=lastSpecialIndex - 2; i >= start; --i) {
      char ch=buf[i];
      if (ch <= 13 || ch == '\\' || ch == '\'' || (ch == '/' && isEnabled(SerializerFeature.WriteSlashAsSpecial))) {
        System.arraycopy(buf,i + 1,buf,i + 2,end - i - 1);
        buf[i]='\\';
        buf[i + 1]=replaceChars[(int)ch];
        end++;
      }
    }
  }
  buf[count - 1]='\'';
}

private void scanStringSingleQuote(){
  np=bp;
  hasSpecial=false;
  char chLocal;
  for (; ; ) {
    chLocal=next();
    if (chLocal == '\'') {
      break;
    }
    if (chLocal == EOI) {
      if (!isEOF()) {
        putChar((char)EOI);
        continue;
      }
      throw new JSONException("unclosed single-quote string");
    }
    if (chLocal == '\\') {
      if (!hasSpecial) {
        hasSpecial=true;
        if (sp > sbuf.length) {
          char[] newsbuf=new char[sp * 2];
          System.arraycopy(sbuf,0,newsbuf,0,sbuf.length);
          sbuf=newsbuf;
        }
        this.copyTo(np + 1,sp,sbuf);
      }
      chLocal=next();
      scanStringSingleQuote_extraction_2(chLocal);
      continue;
    }
    if (!hasSpecial) {
      sp++;
      continue;
    }
    if (sp == sbuf.length) {
      putChar(chLocal);
    }
 else {
      sbuf[sp++]=chLocal;
    }
  }
  token=LITERAL_STRING;
  this.next();
}

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
switch (chLocal) {
case '0':        putChar('\0');
      break;
case '1':    putChar('\1');
  break;
case '2':putChar('\2');
break;
case '3':putChar('\3');
break;
case '4':putChar('\4');
break;
case '5':putChar('\5');
break;
case '6':putChar('\6');
break;
case '7':putChar('\7');
break;
case 'b':putChar('\b');
break;
case 't':putChar('\t');
break;
case 'n':putChar('\n');
break;
case 'v':putChar('\u000B');
break;
case 'f':case 'F':putChar('\f');
break;
case 'r':putChar('\r');
break;
case '"':putChar('"');
break;
case '\'':putChar('\'');
break;
case '/':putChar('/');
break;
case '\\':putChar('\\');
break;
case 'x':char x1=next();
char x2=next();
boolean hex1=(x1 >= '0' && x1 <= '9') || (x1 >= 'a' && x1 <= 'f') || (x1 >= 'A' && x1 <= 'F');
boolean hex2=(x2 >= '0' && x2 <= '9') || (x2 >= 'a' && x2 <= 'f') || (x2 >= 'A' && x2 <= 'F');
if (!hex1 || !hex2) {
throw new JSONException("invalid escape character \\x" + x1 + x2);
}
putChar((char)(digits[x1] * 16 + digits[x2]));
break;
case 'u':putChar((char)Integer.parseInt(new String(new char[]{next(),next(),next(),next()}),16));
break;
default:this.ch=chLocal;
throw new JSONException("unclosed single-quote string");
}
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

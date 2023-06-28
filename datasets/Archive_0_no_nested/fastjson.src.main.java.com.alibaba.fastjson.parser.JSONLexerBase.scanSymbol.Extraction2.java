public final String scanSymbol(final SymbolTable symbolTable,final char quote){
  int hash=0;
  np=bp;
  sp=0;
  boolean hasSpecial=false;
  char chLocal;
  for (; ; ) {
    chLocal=next();
    if (chLocal == quote) {
      break;
    }
    if (chLocal == EOI) {
      throw new JSONException("unclosed.str");
    }
    if (chLocal == '\\') {
      hasSpecial=scanSymbol_extraction_1(hasSpecial);
      chLocal=next();
switch (chLocal) {
case '0':        hash=31 * hash + (int)chLocal;
      putChar('\0');
    break;
case '1':  hash=31 * hash + (int)chLocal;
putChar('\1');
break;
case '2':hash=31 * hash + (int)chLocal;
putChar('\2');
break;
case '3':hash=31 * hash + (int)chLocal;
putChar('\3');
break;
case '4':hash=31 * hash + (int)chLocal;
putChar('\4');
break;
case '5':hash=31 * hash + (int)chLocal;
putChar('\5');
break;
case '6':hash=31 * hash + (int)chLocal;
putChar('\6');
break;
case '7':hash=31 * hash + (int)chLocal;
putChar('\7');
break;
case 'b':hash=31 * hash + (int)'\b';
putChar('\b');
break;
case 't':hash=31 * hash + (int)'\t';
putChar('\t');
break;
case 'n':hash=31 * hash + (int)'\n';
putChar('\n');
break;
case 'v':hash=31 * hash + (int)'\u000B';
putChar('\u000B');
break;
case 'f':case 'F':hash=31 * hash + (int)'\f';
putChar('\f');
break;
case 'r':hash=31 * hash + (int)'\r';
putChar('\r');
break;
case '"':hash=31 * hash + (int)'"';
putChar('"');
break;
case '\'':hash=31 * hash + (int)'\'';
putChar('\'');
break;
case '/':hash=31 * hash + (int)'/';
putChar('/');
break;
case '\\':hash=31 * hash + (int)'\\';
putChar('\\');
break;
case 'x':char x1=ch=next();
char x2=ch=next();
int x_val=digits[x1] * 16 + digits[x2];
char x_char=(char)x_val;
hash=31 * hash + (int)x_char;
putChar(x_char);
break;
case 'u':char c1=chLocal=next();
char c2=chLocal=next();
char c3=chLocal=next();
char c4=chLocal=next();
int val=Integer.parseInt(new String(new char[]{c1,c2,c3,c4}),16);
hash=31 * hash + val;
putChar((char)val);
break;
default:this.ch=chLocal;
throw new JSONException("unclosed.str.lit");
}
continue;
}
hash=31 * hash + chLocal;
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
String value=scanSymbol_extraction_2(symbolTable,hash,hasSpecial);
sp=0;
this.next();
return value;
}

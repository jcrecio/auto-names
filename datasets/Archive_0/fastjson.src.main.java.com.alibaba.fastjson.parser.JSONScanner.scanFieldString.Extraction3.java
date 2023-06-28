public String scanFieldString(char[] fieldName){
  matchStat=UNKNOWN;
  int startPos=this.bp;
  char startChar=this.ch;
  for (; ; ) {
    if (!charArrayCompare(text,bp,fieldName)) {
      if (isWhitespace(ch)) {
        next();
        while (isWhitespace(ch)) {
          next();
        }
        continue;
      }
      matchStat=NOT_MATCH_NAME;
      return stringDefaultValue();
    }
 else {
      break;
    }
  }
  int index=bp + fieldName.length;
  int spaceCount=0;
  char ch=charAt(index++);
  return scanFieldString_extraction_1(fieldName,startPos,startChar,index,spaceCount,ch);
}

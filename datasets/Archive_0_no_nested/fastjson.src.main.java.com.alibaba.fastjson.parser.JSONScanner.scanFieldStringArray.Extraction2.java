public String[] scanFieldStringArray(char[] fieldName,int argTypesCount,SymbolTable typeSymbolTable){
  int startPos=bp;
  char starChar=ch;
  while (isWhitespace(ch)) {
    next();
  }
  int offset;
  char ch;
  if (fieldName != null) {
    matchStat=UNKNOWN;
    if (!charArrayCompare(fieldName)) {
      matchStat=NOT_MATCH_NAME;
      return null;
    }
    offset=bp + fieldName.length;
    ch=text.charAt(offset++);
    while (isWhitespace(ch)) {
      ch=text.charAt(offset++);
    }
    if (ch == ':') {
      ch=text.charAt(offset++);
    }
 else {
      matchStat=NOT_MATCH;
      return null;
    }
    while (isWhitespace(ch)) {
      ch=text.charAt(offset++);
    }
  }
 else {
    offset=bp + 1;
    ch=this.ch;
  }
  if (ch == '[') {
    bp=offset;
    this.ch=text.charAt(bp);
  }
 else   return scanFieldStringArray_extraction_1(ch);
  String[] types=argTypesCount >= 0 ? new String[argTypesCount] : new String[4];
  int typeIndex=0;
  return scanFieldStringArray_extraction_2(typeSymbolTable,startPos,starChar,types,typeIndex);
}

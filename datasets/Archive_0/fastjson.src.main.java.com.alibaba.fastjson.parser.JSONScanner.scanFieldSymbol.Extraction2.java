public long scanFieldSymbol(char[] fieldName){
  matchStat=UNKNOWN;
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
      return 0;
    }
 else {
      break;
    }
  }
  int index=bp + fieldName.length;
  int spaceCount=0;
  char ch=charAt(index++);
  return scanFieldSymbol_extraction_1(index,spaceCount,ch);
}

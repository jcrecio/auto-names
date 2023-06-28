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
  if (ch != '"') {
    while (isWhitespace(ch)) {
      ch=charAt(index++);
      spaceCount++;
    }
    if (ch != '"') {
      matchStat=NOT_MATCH;
      return 0;
    }
  }
  long hash=fnv1a_64_magic_hashcode;
  for (; ; ) {
    ch=charAt(index++);
    if (ch == '\"') {
      bp=index;
      this.ch=ch=charAt(bp);
      break;
    }
 else     if (index > len) {
      matchStat=NOT_MATCH;
      return 0;
    }
    hash^=ch;
    hash*=fnv1a_64_magic_prime;
  }
  return scanFieldSymbol_extraction_2(ch,hash);
}

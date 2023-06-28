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
  if (ch != '"') {
    while (isWhitespace(ch)) {
      spaceCount++;
      ch=charAt(index++);
    }
    if (ch != '"') {
      matchStat=NOT_MATCH;
      return stringDefaultValue();
    }
  }
  final String strVal;
{
    int startIndex=index;
    int endIndex=indexOf('"',startIndex);
    if (endIndex == -1) {
      throw new JSONException("unclosed str");
    }
    String stringVal=subString(startIndex,endIndex - startIndex);
    if (stringVal.indexOf('\\') != -1) {
      for (; ; ) {
        int slashCount=0;
        for (int i=endIndex - 1; i >= 0; --i) {
          if (charAt(i) == '\\') {
            slashCount++;
          }
 else {
            break;
          }
        }
        if (slashCount % 2 == 0) {
          break;
        }
        endIndex=indexOf('"',endIndex + 1);
      }
      int chars_len=endIndex - (bp + fieldName.length + 1+ spaceCount);
      char[] chars=sub_chars(bp + fieldName.length + 1+ spaceCount,chars_len);
      stringVal=readString(chars,chars_len);
    }
    if ((this.features & Feature.TrimStringFieldValue.mask) != 0) {
      stringVal=stringVal.trim();
    }
    ch=charAt(endIndex + 1);
    for (; ; ) {
      if (ch == ',' || ch == '}') {
        bp=endIndex + 1;
        this.ch=ch;
        strVal=stringVal;
        break;
      }
 else       if (isWhitespace(ch)) {
        endIndex++;
        ch=charAt(endIndex + 1);
      }
 else {
        matchStat=NOT_MATCH;
        return stringDefaultValue();
      }
    }
  }
  return scanFieldString_extraction_3(startPos,startChar,ch,strVal);
}

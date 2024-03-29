public long scanFieldLong(char[] fieldName){
  matchStat=UNKNOWN;
  if (!charArrayCompare(fieldName)) {
    matchStat=NOT_MATCH_NAME;
    return 0;
  }
  int offset=fieldName.length;
  char chLocal=charAt(bp + (offset++));
  boolean negative=false;
  if (chLocal == '-') {
    chLocal=charAt(bp + (offset++));
    negative=true;
  }
  long value;
  if (chLocal >= '0' && chLocal <= '9') {
    value=chLocal - '0';
    for (; ; ) {
      chLocal=charAt(bp + (offset++));
      if (chLocal >= '0' && chLocal <= '9') {
        value=value * 10 + (chLocal - '0');
      }
 else       if (chLocal == '.') {
        matchStat=NOT_MATCH;
        return 0;
      }
 else {
        break;
      }
    }
    boolean valid=offset - fieldName.length < 21 && (value >= 0 || (value == -9223372036854775808L && negative));
    if (!valid) {
      matchStat=NOT_MATCH;
      return 0;
    }
  }
 else {
    matchStat=NOT_MATCH;
    return 0;
  }
  return scanFieldLong_extraction_2(offset,chLocal,negative,value);
}

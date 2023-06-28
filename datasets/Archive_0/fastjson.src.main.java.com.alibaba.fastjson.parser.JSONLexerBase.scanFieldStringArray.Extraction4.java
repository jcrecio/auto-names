@SuppressWarnings("unchecked") public Collection<String> scanFieldStringArray(char[] fieldName,Class<?> type){
  matchStat=UNKNOWN;
  if (!charArrayCompare(fieldName)) {
    matchStat=NOT_MATCH_NAME;
    return null;
  }
  Collection<String> list=newCollectionByType(type);
  int offset=fieldName.length;
  char chLocal=charAt(bp + (offset++));
  if (chLocal != '[') {
    matchStat=NOT_MATCH;
    return null;
  }
  return scanFieldStringArray_extraction_1(list,offset);
}

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
  chLocal=charAt(bp + (offset++));
  for (; ; ) {
    if (chLocal == '"') {
      int startIndex=bp + offset;
      int endIndex=indexOf('"',startIndex);
      scanFieldStringArray_extraction_2(endIndex);
      int startIndex2=bp + offset;
      String stringVal=subString(startIndex2,endIndex - startIndex2);
      if (stringVal.indexOf('\\') != -1) {
        endIndex=scanFieldStringArray_extraction_3(endIndex);
        int chars_len=endIndex - (bp + offset);
        char[] chars=sub_chars(bp + offset,chars_len);
        stringVal=readString(chars,chars_len);
      }
      offset+=(endIndex - (bp + offset) + 1);
      chLocal=charAt(bp + (offset++));
      list.add(stringVal);
    }
 else     if (chLocal == 'n' && charAt(bp + offset) == 'u' && charAt(bp + offset + 1) == 'l' && charAt(bp + offset + 2) == 'l') {
      offset+=3;
      chLocal=charAt(bp + (offset++));
      list.add(null);
    }
 else     if (chLocal == ']' && list.size() == 0) {
      chLocal=charAt(bp + (offset++));
      break;
    }
 else {
      throw new JSONException("illega str");
    }
    if (chLocal == ',') {
      chLocal=charAt(bp + (offset++));
      continue;
    }
    if (chLocal == ']') {
      chLocal=charAt(bp + (offset++));
      break;
    }
    matchStat=NOT_MATCH;
    return null;
  }
  return scanFieldStringArray_extraction_4(list,offset,chLocal);
}

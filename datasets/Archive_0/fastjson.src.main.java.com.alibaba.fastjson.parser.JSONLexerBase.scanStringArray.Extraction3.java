public void scanStringArray(Collection<String> list,char seperator){
  matchStat=UNKNOWN;
  int offset=0;
  char chLocal=charAt(bp + (offset++));
  if (chLocal == 'n' && charAt(bp + offset) == 'u' && charAt(bp + offset + 1) == 'l' && charAt(bp + offset + 2) == 'l' && charAt(bp + offset + 3) == seperator) {
    bp+=5;
    ch=charAt(bp);
    matchStat=VALUE_NULL;
    return;
  }
  if (chLocal != '[') {
    matchStat=NOT_MATCH;
    return;
  }
  chLocal=charAt(bp + (offset++));
  for (; ; ) {
    if (chLocal == 'n' && charAt(bp + offset) == 'u' && charAt(bp + offset + 1) == 'l' && charAt(bp + offset + 2) == 'l') {
      offset+=3;
      chLocal=charAt(bp + (offset++));
      list.add(null);
    }
 else     if (chLocal == ']' && list.size() == 0) {
      chLocal=charAt(bp + (offset++));
      break;
    }
 else     if (chLocal != '"') {
      matchStat=NOT_MATCH;
      return;
    }
 else {
      int startIndex=bp + offset;
      int endIndex=indexOf('"',startIndex);
      scanStringArray_extraction_2(endIndex);
      String stringVal=subString(bp + offset,endIndex - startIndex);
      if (stringVal.indexOf('\\') != -1) {
        endIndex=scanStringArray_extraction_3(endIndex);
        int chars_len=endIndex - startIndex;
        char[] chars=sub_chars(bp + offset,chars_len);
        stringVal=readString(chars,chars_len);
      }
      offset+=(endIndex - (bp + offset) + 1);
      chLocal=charAt(bp + (offset++));
      list.add(stringVal);
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
    return;
  }
  scanStringArray_extraction_4(seperator,offset,chLocal);
}

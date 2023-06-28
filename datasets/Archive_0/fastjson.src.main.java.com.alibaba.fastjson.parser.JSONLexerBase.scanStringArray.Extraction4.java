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
  scanStringArray_extraction_1(list,seperator,offset);
}

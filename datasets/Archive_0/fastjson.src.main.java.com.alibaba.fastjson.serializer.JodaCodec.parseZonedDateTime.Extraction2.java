protected DateTime parseZonedDateTime(String text,DateTimeFormatter formatter){
  if (formatter == null) {
    if (text.length() == 19) {
      char c4=text.charAt(4);
      char c7=text.charAt(7);
      char c10=text.charAt(10);
      char c13=text.charAt(13);
      char c16=text.charAt(16);
      formatter=parseZonedDateTime_extraction_1(text,formatter,c4,c7,c10,c13,c16);
    }
    if (text.length() >= 17) {
      char c4=text.charAt(4);
      if (c4 == '年') {
        if (text.charAt(text.length() - 1) == '秒') {
          formatter=formatter_dt19_cn_1;
        }
 else {
          formatter=formatter_dt19_cn;
        }
      }
 else       if (c4 == '년') {
        formatter=formatter_dt19_kr;
      }
    }
  }
  return formatter == null ? DateTime.parse(text) : DateTime.parse(text,formatter);
}

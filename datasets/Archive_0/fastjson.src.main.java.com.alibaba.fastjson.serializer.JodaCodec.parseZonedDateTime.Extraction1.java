protected DateTime parseZonedDateTime(String text,DateTimeFormatter formatter){
  if (formatter == null) {
    if (text.length() == 19) {
      char c4=text.charAt(4);
      char c7=text.charAt(7);
      char c10=text.charAt(10);
      char c13=text.charAt(13);
      char c16=text.charAt(16);
      if (c13 == ':' && c16 == ':') {
        if (c4 == '-' && c7 == '-') {
          if (c10 == 'T') {
            formatter=formatter_iso8601;
          }
 else           if (c10 == ' ') {
            formatter=defaultFormatter;
          }
        }
 else         if (c4 == '/' && c7 == '/') {
          formatter=formatter_dt19_tw;
        }
 else {
          char c0=text.charAt(0);
          char c1=text.charAt(1);
          char c2=text.charAt(2);
          char c3=text.charAt(3);
          char c5=text.charAt(5);
          formatter=parseZonedDateTime_extraction_2(formatter,c4,c0,c1,c2,c3,c5);
        }
      }
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

protected LocalDate parseLocalDate(String text,String format,DateTimeFormatter formatter){
  if (formatter == null) {
    if (text.length() == 8) {
      formatter=formatter_d8;
    }
    if (text.length() == 10) {
      char c4=text.charAt(4);
      char c7=text.charAt(7);
      if (c4 == '/' && c7 == '/') {
        formatter=formatter_d10_tw;
      }
      char c0=text.charAt(0);
      formatter=parseLocalDate_extraction_2(text,formatter,c4,c0);
    }
    if (text.length() >= 9) {
      char c4=text.charAt(4);
      if (c4 == '年') {
        formatter=formatter_d10_cn;
      }
 else       if (c4 == '년') {
        formatter=formatter_d10_kr;
      }
    }
    boolean digit=true;
    for (int i=0; i < text.length(); ++i) {
      char ch=text.charAt(i);
      if (ch < '0' || ch > '9') {
        digit=false;
        break;
      }
    }
    if (digit && text.length() > 8 && text.length() < 19) {
      long epochMillis=Long.parseLong(text);
      return new LocalDateTime(epochMillis,DateTimeZone.forTimeZone(JSON.defaultTimeZone)).toLocalDate();
    }
  }
  return formatter == null ? LocalDate.parse(text) : LocalDate.parse(text,formatter);
}

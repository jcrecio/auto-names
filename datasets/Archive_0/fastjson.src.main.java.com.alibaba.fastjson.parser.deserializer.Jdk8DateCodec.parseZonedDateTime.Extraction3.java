protected ZonedDateTime parseZonedDateTime(String text,DateTimeFormatter formatter){
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
      formatter=parseZonedDateTime_extraction_3(text,formatter,c4);
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
      return ZonedDateTime.ofInstant(Instant.ofEpochMilli(epochMillis),JSON.defaultTimeZone.toZoneId());
    }
  }
  return formatter == null ? ZonedDateTime.parse(text) : ZonedDateTime.parse(text,formatter);
}

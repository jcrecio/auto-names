protected LocalDate parseLocalDate(String text,String format,DateTimeFormatter formatter){
  if (formatter == null) {
    if (text.length() == 8) {
      formatter=formatter_d8;
    }
    formatter=parseLocalDate_extraction_1(text,formatter);
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
      return LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMillis),JSON.defaultTimeZone.toZoneId()).toLocalDate();
    }
  }
  return formatter == null ? LocalDate.parse(text) : LocalDate.parse(text,formatter);
}

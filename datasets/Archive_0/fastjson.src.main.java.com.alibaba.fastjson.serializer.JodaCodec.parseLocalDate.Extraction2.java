protected LocalDate parseLocalDate(String text,String format,DateTimeFormatter formatter){
  if (formatter == null) {
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
      return new LocalDateTime(epochMillis,DateTimeZone.forTimeZone(JSON.defaultTimeZone)).toLocalDate();
    }
  }
  return formatter == null ? LocalDate.parse(text) : LocalDate.parse(text,formatter);
}

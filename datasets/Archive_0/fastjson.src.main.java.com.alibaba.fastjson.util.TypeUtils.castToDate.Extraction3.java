public static Date castToDate(Object value,String format){
  if (value == null) {
    return null;
  }
  if (value instanceof Date) {
    return (Date)value;
  }
  if (value instanceof Calendar) {
    return ((Calendar)value).getTime();
  }
  long longValue=-1;
  if (value instanceof BigDecimal) {
    longValue=longValue((BigDecimal)value);
    return new Date(longValue);
  }
  if (value instanceof Number) {
    longValue=((Number)value).longValue();
    if ("unixtime".equals(format)) {
      longValue*=1000;
    }
    return new Date(longValue);
  }
  if (value instanceof String) {
    String strVal=(String)value;
    JSONScanner dateLexer=new JSONScanner(strVal);
    try {
      if (dateLexer.scanISO8601DateIfMatch(false)) {
        Calendar calendar=dateLexer.getCalendar();
        return calendar.getTime();
      }
    }
  finally {
      dateLexer.close();
    }
    if (strVal.startsWith("/Date(") && strVal.endsWith(")/")) {
      strVal=strVal.substring(6,strVal.length() - 2);
    }
    if (strVal.indexOf('-') > 0 || strVal.indexOf('+') > 0 || format != null) {
      format=castToDate_extraction_2(format,strVal);
      SimpleDateFormat dateFormat=new SimpleDateFormat(format,JSON.defaultLocale);
      dateFormat.setTimeZone(JSON.defaultTimeZone);
      try {
        return dateFormat.parse(strVal);
      }
 catch (      ParseException e) {
        throw new JSONException("can not cast to Date, value : " + strVal);
      }
    }
    if (strVal.length() == 0) {
      return null;
    }
    longValue=Long.parseLong(strVal);
  }
  if (longValue == -1) {
    return castToDate_extraction_3(value);
  }
  return new Date(longValue);
}

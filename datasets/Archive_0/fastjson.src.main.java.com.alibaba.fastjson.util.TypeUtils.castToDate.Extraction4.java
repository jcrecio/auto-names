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
  return castToDate_extraction_1(value,format,longValue);
}

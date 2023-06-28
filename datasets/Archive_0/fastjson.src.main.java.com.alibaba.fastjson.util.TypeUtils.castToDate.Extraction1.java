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
      if (format == null) {
        final int len=strVal.length();
        if (len == JSON.DEFFAULT_DATE_FORMAT.length() || (len == 22 && JSON.DEFFAULT_DATE_FORMAT.equals("yyyyMMddHHmmssSSSZ"))) {
          format=JSON.DEFFAULT_DATE_FORMAT;
        }
 else         if (len == 10) {
          format="yyyy-MM-dd";
        }
 else         if (len == "yyyy-MM-dd HH:mm:ss".length()) {
          format="yyyy-MM-dd HH:mm:ss";
        }
 else         if (len == 29 && strVal.charAt(26) == ':' && strVal.charAt(28) == '0') {
          format="yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
        }
 else         if (len == 23 && strVal.charAt(19) == ',') {
          format="yyyy-MM-dd HH:mm:ss,SSS";
        }
 else {
          format="yyyy-MM-dd HH:mm:ss.SSS";
        }
      }
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
    Class<?> clazz=value.getClass();
    if ("oracle.sql.TIMESTAMP".equals(clazz.getName())) {
      if (oracleTimestampMethod == null && !oracleTimestampMethodInited) {
        try {
          oracleTimestampMethod=clazz.getMethod("toJdbc");
        }
 catch (        NoSuchMethodException e) {
        }
 finally {
          oracleTimestampMethodInited=true;
        }
      }
      Object result;
      try {
        result=oracleTimestampMethod.invoke(value);
      }
 catch (      Exception e) {
        throw new JSONException("can not cast oracle.sql.TIMESTAMP to Date",e);
      }
      return (Date)result;
    }
    if ("oracle.sql.DATE".equals(clazz.getName())) {
      return castToDate_extraction_4(value,clazz);
    }
    throw new JSONException("can not cast to Date, value : " + value);
  }
  return new Date(longValue);
}

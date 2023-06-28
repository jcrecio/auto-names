@Override @SuppressWarnings("unchecked") public <T>T deserialze(DefaultJSONParser parser,Type clazz,Object fieldName,String format,int features){
  JSONLexer lexer=parser.lexer;
  Object val;
  if (lexer.token() == JSONToken.LITERAL_INT) {
    long millis=lexer.longValue();
    lexer.nextToken(JSONToken.COMMA);
    if ("unixtime".equals(format)) {
      millis*=1000;
    }
    val=millis;
  }
 else   if (lexer.token() == JSONToken.LITERAL_STRING) {
    String strVal=lexer.stringVal();
    if (format != null) {
      if ("yyyy-MM-dd HH:mm:ss.SSSSSSSSS".equals(format) && clazz instanceof Class && ((Class)clazz).getName().equals("java.sql.Timestamp")) {
        return (T)TypeUtils.castToTimestamp(strVal);
      }
      SimpleDateFormat simpleDateFormat=null;
      try {
        simpleDateFormat=new SimpleDateFormat(format,parser.lexer.getLocale());
      }
 catch (      IllegalArgumentException ex) {
        if (format.contains("T")) {
          String fromat2=format.replaceAll("T","'T'");
          try {
            simpleDateFormat=new SimpleDateFormat(fromat2,parser.lexer.getLocale());
          }
 catch (          IllegalArgumentException e2) {
            throw ex;
          }
        }
      }
      if (JSON.defaultTimeZone != null) {
        simpleDateFormat.setTimeZone(parser.lexer.getTimeZone());
      }
      try {
        val=simpleDateFormat.parse(strVal);
      }
 catch (      ParseException ex) {
        val=null;
      }
      if (val == null && JSON.defaultLocale == Locale.CHINA) {
        try {
          simpleDateFormat=new SimpleDateFormat(format,Locale.US);
        }
 catch (        IllegalArgumentException ex) {
          if (format.contains("T")) {
            String fromat2=format.replaceAll("T","'T'");
            try {
              simpleDateFormat=new SimpleDateFormat(fromat2,parser.lexer.getLocale());
            }
 catch (            IllegalArgumentException e2) {
              throw ex;
            }
          }
        }
        simpleDateFormat.setTimeZone(parser.lexer.getTimeZone());
        try {
          val=simpleDateFormat.parse(strVal);
        }
 catch (        ParseException ex) {
          val=null;
        }
      }
      if (val == null) {
        if (format.equals("yyyy-MM-dd'T'HH:mm:ss.SSS") && strVal.length() == 19) {
          try {
            SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",JSON.defaultLocale);
            df.setTimeZone(JSON.defaultTimeZone);
            val=df.parse(strVal);
          }
 catch (          ParseException ex2) {
            val=null;
          }
        }
 else {
          val=null;
        }
      }
    }
 else {
      val=null;
    }
    if (val == null) {
      val=strVal;
      lexer.nextToken(JSONToken.COMMA);
      if (lexer.isEnabled(Feature.AllowISO8601DateFormat)) {
        JSONScanner iso8601Lexer=new JSONScanner(strVal);
        if (iso8601Lexer.scanISO8601DateIfMatch()) {
          val=iso8601Lexer.getCalendar().getTime();
        }
        iso8601Lexer.close();
      }
    }
  }
 else   if (lexer.token() == JSONToken.NULL) {
    lexer.nextToken();
    val=null;
  }
 else   if (lexer.token() == JSONToken.LBRACE) {
    lexer.nextToken();
    String key;
    clazz=deserialze_extraction_4(parser,clazz,lexer);
    long timeMillis=deserialze_extraction_5(lexer);
    val=timeMillis;
    parser.accept(JSONToken.RBRACE);
  }
 else   val=deserialze_extraction_6(parser,lexer);
  return (T)cast(parser,clazz,fieldName,val);
}

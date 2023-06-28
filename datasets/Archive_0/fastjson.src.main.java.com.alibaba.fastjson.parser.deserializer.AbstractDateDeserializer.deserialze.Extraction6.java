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
      val=deserialze_extraction_1(parser,format,strVal,simpleDateFormat);
    }
 else {
      val=null;
    }
    val=deserialze_extraction_3(lexer,val,strVal);
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

protected Point parsePoint(DefaultJSONParser parser,Object fieldName){
  JSONLexer lexer=parser.lexer;
  int x=0, y=0;
  for (; ; ) {
    if (lexer.token() == JSONToken.RBRACE) {
      lexer.nextToken();
      break;
    }
    String key;
    if (lexer.token() == JSONToken.LITERAL_STRING) {
      key=lexer.stringVal();
      if (JSON.DEFAULT_TYPE_KEY.equals(key)) {
        parser.acceptType("java.awt.Point");
        continue;
      }
      if ("$ref".equals(key)) {
        return (Point)parseRef(parser,fieldName);
      }
      lexer.nextTokenWithColon(JSONToken.LITERAL_INT);
    }
 else {
      throw new JSONException("syntax error");
    }
    int token=lexer.token();
    int val;
    if (token == JSONToken.LITERAL_INT) {
      val=lexer.intValue();
      lexer.nextToken();
    }
 else     if (token == JSONToken.LITERAL_FLOAT) {
      val=(int)lexer.floatValue();
      lexer.nextToken();
    }
 else {
      throw new JSONException("syntax error : " + lexer.tokenName());
    }
    if (key.equalsIgnoreCase("x")) {
      x=val;
    }
 else     if (key.equalsIgnoreCase("y")) {
      y=val;
    }
 else {
      throw new JSONException("syntax error, " + key);
    }
    parsePoint_extraction_3(lexer);
  }
  return new Point(x,y);
}

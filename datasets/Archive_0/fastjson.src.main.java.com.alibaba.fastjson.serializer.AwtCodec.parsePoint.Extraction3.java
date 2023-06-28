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
    int val=parsePoint_extraction_1(lexer,token);
    if (key.equalsIgnoreCase("x")) {
      x=val;
    }
 else     y=parsePoint_extraction_2(y,key,val);
    parsePoint_extraction_3(lexer);
  }
  return new Point(x,y);
}

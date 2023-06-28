protected Font parseFont(DefaultJSONParser parser){
  JSONLexer lexer=parser.lexer;
  int size=0, style=0;
  String name=null;
  for (; ; ) {
    if (lexer.token() == JSONToken.RBRACE) {
      lexer.nextToken();
      break;
    }
    String key;
    if (lexer.token() == JSONToken.LITERAL_STRING) {
      key=lexer.stringVal();
      lexer.nextTokenWithColon(JSONToken.LITERAL_INT);
    }
 else {
      throw new JSONException("syntax error");
    }
    if (key.equalsIgnoreCase("name")) {
      if (lexer.token() == JSONToken.LITERAL_STRING) {
        name=lexer.stringVal();
        lexer.nextToken();
      }
 else {
        throw new JSONException("syntax error");
      }
    }
 else     if (key.equalsIgnoreCase("style")) {
      style=parseFont_extraction_1(lexer,style);
    }
 else     size=parseFont_extraction_2(lexer,size,key);
    if (lexer.token() == JSONToken.COMMA) {
      lexer.nextToken(JSONToken.LITERAL_STRING);
    }
  }
  return new Font(name,style,size);
}

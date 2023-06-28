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
      if (lexer.token() == JSONToken.LITERAL_INT) {
        style=lexer.intValue();
        lexer.nextToken();
      }
 else {
        throw new JSONException("syntax error");
      }
    }
 else     if (key.equalsIgnoreCase("size")) {
      if (lexer.token() == JSONToken.LITERAL_INT) {
        size=lexer.intValue();
        lexer.nextToken();
      }
 else {
        throw new JSONException("syntax error");
      }
    }
 else {
      throw new JSONException("syntax error, " + key);
    }
    if (lexer.token() == JSONToken.COMMA) {
      lexer.nextToken(JSONToken.LITERAL_STRING);
    }
  }
  return new Font(name,style,size);
}

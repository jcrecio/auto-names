public static Object parseMap(DefaultJSONParser parser,Map<Object,Object> map,Type keyType,Type valueType,Object fieldName){
  JSONLexer lexer=parser.lexer;
  if (lexer.token() != JSONToken.LBRACE && lexer.token() != JSONToken.COMMA) {
    throw new JSONException("syntax error, expect {, actual " + lexer.tokenName());
  }
  ObjectDeserializer keyDeserializer=parser.getConfig().getDeserializer(keyType);
  ObjectDeserializer valueDeserializer=parser.getConfig().getDeserializer(valueType);
  lexer.nextToken(keyDeserializer.getFastMatchToken());
  ParseContext context=parser.getContext();
  try {
    for (; ; ) {
      if (lexer.token() == JSONToken.RBRACE) {
        lexer.nextToken(JSONToken.COMMA);
        break;
      }
      if (lexer.token() == JSONToken.LITERAL_STRING && lexer.isRef() && !lexer.isEnabled(Feature.DisableSpecialKeyDetect)) {
        Object object=null;
        lexer.nextTokenWithColon(JSONToken.LITERAL_STRING);
        return parseMap_extraction_1(parser,lexer,context,object);
      }
      if (map.size() == 0 && lexer.token() == JSONToken.LITERAL_STRING && JSON.DEFAULT_TYPE_KEY.equals(lexer.stringVal()) && !lexer.isEnabled(Feature.DisableSpecialKeyDetect)) {
        lexer.nextTokenWithColon(JSONToken.LITERAL_STRING);
        lexer.nextToken(JSONToken.COMMA);
        if (lexer.token() == JSONToken.RBRACE) {
          lexer.nextToken();
          return map;
        }
        lexer.nextToken(keyDeserializer.getFastMatchToken());
      }
      Object key=parseMap_extraction_2(parser,keyType,lexer,keyDeserializer,valueDeserializer);
      Object value=valueDeserializer.deserialze(parser,valueType,key);
      parser.checkMapResolve(map,key);
      map.put(key,value);
      parseMap_extraction_3(lexer,keyDeserializer);
    }
  }
  finally {
    parser.setContext(context);
  }
  return map;
}

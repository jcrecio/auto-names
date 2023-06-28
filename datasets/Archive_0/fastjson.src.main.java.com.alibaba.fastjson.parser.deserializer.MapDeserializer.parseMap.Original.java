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
        if (lexer.token() == JSONToken.LITERAL_STRING) {
          String ref=lexer.stringVal();
          if ("..".equals(ref)) {
            ParseContext parentContext=context.parent;
            object=parentContext.object;
          }
 else           if ("$".equals(ref)) {
            ParseContext rootContext=context;
            while (rootContext.parent != null) {
              rootContext=rootContext.parent;
            }
            object=rootContext.object;
          }
 else {
            parser.addResolveTask(new ResolveTask(context,ref));
            parser.setResolveStatus(DefaultJSONParser.NeedToResolve);
          }
        }
 else {
          throw new JSONException("illegal ref, " + JSONToken.name(lexer.token()));
        }
        lexer.nextToken(JSONToken.RBRACE);
        if (lexer.token() != JSONToken.RBRACE) {
          throw new JSONException("illegal ref");
        }
        lexer.nextToken(JSONToken.COMMA);
        return object;
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
      Object key;
      if (lexer.token() == JSONToken.LITERAL_STRING && keyDeserializer instanceof JavaBeanDeserializer) {
        String keyStrValue=lexer.stringVal();
        lexer.nextToken();
        DefaultJSONParser keyParser=new DefaultJSONParser(keyStrValue,parser.getConfig(),parser.getLexer().getFeatures());
        keyParser.setDateFormat(parser.getDateFomartPattern());
        key=keyDeserializer.deserialze(keyParser,keyType,null);
      }
 else {
        key=keyDeserializer.deserialze(parser,keyType,null);
      }
      if (lexer.token() != JSONToken.COLON) {
        throw new JSONException("syntax error, expect :, actual " + lexer.token());
      }
      lexer.nextToken(valueDeserializer.getFastMatchToken());
      Object value=valueDeserializer.deserialze(parser,valueType,key);
      parser.checkMapResolve(map,key);
      map.put(key,value);
      if (lexer.token() == JSONToken.COMMA) {
        lexer.nextToken(keyDeserializer.getFastMatchToken());
      }
    }
  }
  finally {
    parser.setContext(context);
  }
  return map;
}

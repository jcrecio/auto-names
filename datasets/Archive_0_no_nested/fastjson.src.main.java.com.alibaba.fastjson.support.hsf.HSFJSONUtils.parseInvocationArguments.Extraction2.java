public static Object[] parseInvocationArguments(String json,MethodLocator methodLocator){
  DefaultJSONParser parser=new DefaultJSONParser(json);
  JSONLexerBase lexer=(JSONLexerBase)parser.getLexer();
  ParseContext rootContext=parser.setContext(null,null);
  Object[] values;
  int token=lexer.token();
  if (token == JSONToken.LBRACE) {
    String[] typeNames=lexer.scanFieldStringArray(fieldName_argsTypes,-1,typeSymbolTable);
    if (typeNames == null && lexer.matchStat == NOT_MATCH_NAME) {
      String type=lexer.scanFieldString(fieldName_type);
      if ("com.alibaba.fastjson.JSONObject".equals(type)) {
        typeNames=lexer.scanFieldStringArray(fieldName_argsTypes,-1,typeSymbolTable);
      }
    }
    Method method=methodLocator.findMethod(typeNames);
    values=parseInvocationArguments_extraction_1(json,methodLocator,parser,lexer,rootContext,method);
  }
 else   if (token == JSONToken.LBRACKET) {
    String[] typeNames=lexer.scanFieldStringArray(null,-1,typeSymbolTable);
    lexer.skipWhitespace();
    char ch=lexer.getCurrent();
    if (ch == ']') {
      Method method=methodLocator.findMethod(null);
      return parseInvocationArguments_extraction_2(parser,typeNames,method);
    }
    if (ch == ',') {
      lexer.next();
      lexer.skipWhitespace();
    }
    lexer.nextToken(JSONToken.LBRACKET);
    Method method=methodLocator.findMethod(typeNames);
    Type[] argTypes=method.getGenericParameterTypes();
    values=parser.parseArray(argTypes);
    lexer.close();
  }
 else {
    values=null;
  }
  return values;
}

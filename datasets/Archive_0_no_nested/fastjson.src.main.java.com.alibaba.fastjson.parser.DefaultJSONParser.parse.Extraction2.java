public Object parse(Object fieldName){
  final JSONLexer lexer=this.lexer;
switch (lexer.token()) {
case SET:    lexer.nextToken();
  HashSet<Object> set=new HashSet<Object>();
parseArray(set,fieldName);
return set;
case TREE_SET:lexer.nextToken();
TreeSet<Object> treeSet=new TreeSet<Object>();
parseArray(treeSet,fieldName);
return treeSet;
case LBRACKET:Collection array=isEnabled(Feature.UseNativeJavaObject) ? new ArrayList() : new JSONArray();
parseArray(array,fieldName);
if (lexer.isEnabled(Feature.UseObjectArray)) {
return array.toArray();
}
return array;
case LBRACE:Map object=isEnabled(Feature.UseNativeJavaObject) ? lexer.isEnabled(Feature.OrderedField) ? new HashMap() : new LinkedHashMap() : new JSONObject(lexer.isEnabled(Feature.OrderedField));
return parseObject(object,fieldName);
case LITERAL_INT:Number intValue=lexer.integerValue();
lexer.nextToken();
return intValue;
case LITERAL_FLOAT:Object value=lexer.decimalValue(lexer.isEnabled(Feature.UseBigDecimal));
lexer.nextToken();
return value;
case LITERAL_STRING:String stringLiteral=lexer.stringVal();
lexer.nextToken(JSONToken.COMMA);
return parse_extraction_1(lexer,stringLiteral);
case NULL:lexer.nextToken();
return null;
case UNDEFINED:lexer.nextToken();
return null;
case TRUE:lexer.nextToken();
return Boolean.TRUE;
case FALSE:lexer.nextToken();
return Boolean.FALSE;
case NEW:lexer.nextToken(JSONToken.IDENTIFIER);
if (lexer.token() != JSONToken.IDENTIFIER) {
throw new JSONException("syntax error");
}
lexer.nextToken(JSONToken.LPAREN);
accept(JSONToken.LPAREN);
long time=lexer.integerValue().longValue();
accept(JSONToken.LITERAL_INT);
accept(JSONToken.RPAREN);
return new Date(time);
case EOF:if (lexer.isBlankInput()) {
return null;
}
throw new JSONException("unterminated json string, " + lexer.info());
case HEX:byte[] bytes=lexer.bytesValue();
lexer.nextToken();
return bytes;
case IDENTIFIER:String identifier=lexer.stringVal();
return parse_extraction_2(lexer,identifier);
case ERROR:default:throw new JSONException("syntax error, " + lexer.info());
}
}

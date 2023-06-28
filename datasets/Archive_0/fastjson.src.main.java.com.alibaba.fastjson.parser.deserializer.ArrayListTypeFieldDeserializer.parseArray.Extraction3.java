@SuppressWarnings({"unchecked","rawtypes"}) public final void parseArray(DefaultJSONParser parser,Type objectType,Collection array){
  Type itemType=this.itemType;
  ObjectDeserializer itemTypeDeser=this.deserializer;
  if (objectType instanceof ParameterizedType) {
    if (itemType instanceof TypeVariable) {
      TypeVariable typeVar=(TypeVariable)itemType;
      ParameterizedType paramType=(ParameterizedType)objectType;
      Class<?> objectClass=null;
      if (paramType.getRawType() instanceof Class) {
        objectClass=(Class<?>)paramType.getRawType();
      }
      int paramIndex=-1;
      if (objectClass != null) {
        for (int i=0, size=objectClass.getTypeParameters().length; i < size; ++i) {
          TypeVariable item=objectClass.getTypeParameters()[i];
          if (item.getName().equals(typeVar.getName())) {
            paramIndex=i;
            break;
          }
        }
      }
      if (paramIndex != -1) {
        itemType=paramType.getActualTypeArguments()[paramIndex];
        if (!itemType.equals(this.itemType)) {
          itemTypeDeser=parser.getConfig().getDeserializer(itemType);
        }
      }
    }
 else     if (itemType instanceof ParameterizedType) {
      ParameterizedType parameterizedItemType=(ParameterizedType)itemType;
      Type[] itemActualTypeArgs=parameterizedItemType.getActualTypeArguments();
      itemType=parseArray_extraction_2(objectType,itemType,parameterizedItemType,itemActualTypeArgs);
    }
  }
 else   itemType=parseArray_extraction_3(objectType,itemType);
  final JSONLexer lexer=parser.lexer;
  final int token=lexer.token();
  if (token == JSONToken.LBRACKET) {
    parseArray_extraction_4(parser,array,itemType,itemTypeDeser,lexer);
  }
 else {
    if (itemTypeDeser == null) {
      itemTypeDeser=deserializer=parser.getConfig().getDeserializer(itemType);
    }
    Object val=itemTypeDeser.deserialze(parser,itemType,0);
    array.add(val);
    parser.checkListResolve(array);
  }
}

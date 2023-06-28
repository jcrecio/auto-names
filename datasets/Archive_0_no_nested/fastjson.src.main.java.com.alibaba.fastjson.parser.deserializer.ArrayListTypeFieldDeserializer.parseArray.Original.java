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
      if (itemActualTypeArgs.length == 1 && itemActualTypeArgs[0] instanceof TypeVariable) {
        TypeVariable typeVar=(TypeVariable)itemActualTypeArgs[0];
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
          itemActualTypeArgs[0]=paramType.getActualTypeArguments()[paramIndex];
          itemType=TypeReference.intern(new ParameterizedTypeImpl(itemActualTypeArgs,parameterizedItemType.getOwnerType(),parameterizedItemType.getRawType()));
        }
      }
    }
  }
 else   if (itemType instanceof TypeVariable && objectType instanceof Class) {
    Class objectClass=(Class)objectType;
    TypeVariable typeVar=(TypeVariable)itemType;
    objectClass.getTypeParameters();
    for (int i=0, size=objectClass.getTypeParameters().length; i < size; ++i) {
      TypeVariable item=objectClass.getTypeParameters()[i];
      if (item.getName().equals(typeVar.getName())) {
        Type[] bounds=item.getBounds();
        if (bounds.length == 1) {
          itemType=bounds[0];
        }
        break;
      }
    }
  }
  final JSONLexer lexer=parser.lexer;
  final int token=lexer.token();
  if (token == JSONToken.LBRACKET) {
    if (itemTypeDeser == null) {
      itemTypeDeser=deserializer=parser.getConfig().getDeserializer(itemType);
      itemFastMatchToken=deserializer.getFastMatchToken();
    }
    lexer.nextToken(itemFastMatchToken);
    for (int i=0; ; ++i) {
      if (lexer.isEnabled(Feature.AllowArbitraryCommas)) {
        while (lexer.token() == JSONToken.COMMA) {
          lexer.nextToken();
          continue;
        }
      }
      if (lexer.token() == JSONToken.RBRACKET) {
        break;
      }
      Object val=itemTypeDeser.deserialze(parser,itemType,i);
      array.add(val);
      parser.checkListResolve(array);
      if (lexer.token() == JSONToken.COMMA) {
        lexer.nextToken(itemFastMatchToken);
        continue;
      }
    }
    lexer.nextToken(JSONToken.COMMA);
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

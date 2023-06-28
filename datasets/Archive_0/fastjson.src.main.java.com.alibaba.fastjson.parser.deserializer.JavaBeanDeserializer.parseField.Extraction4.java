public boolean parseField(DefaultJSONParser parser,String key,Object object,Type objectType,Map<String,Object> fieldValues,int[] setFlags){
  JSONLexer lexer=parser.lexer;
  final int disableFieldSmartMatchMask=Feature.DisableFieldSmartMatch.mask;
  final int initStringFieldAsEmpty=Feature.InitStringFieldAsEmpty.mask;
  FieldDeserializer fieldDeserializer;
  if (lexer.isEnabled(disableFieldSmartMatchMask) || (this.beanInfo.parserFeatures & disableFieldSmartMatchMask) != 0) {
    fieldDeserializer=getFieldDeserializer(key);
  }
 else   if (lexer.isEnabled(initStringFieldAsEmpty) || (this.beanInfo.parserFeatures & initStringFieldAsEmpty) != 0) {
    fieldDeserializer=smartMatch(key);
  }
 else {
    fieldDeserializer=smartMatch(key,setFlags);
  }
  final int mask=Feature.SupportNonPublicField.mask;
  if (fieldDeserializer == null && (lexer.isEnabled(mask) || (this.beanInfo.parserFeatures & mask) != 0)) {
    if (this.extraFieldDeserializers == null) {
      ConcurrentHashMap extraFieldDeserializers=new ConcurrentHashMap<String,Object>(1,0.75f,1);
      for (Class c=this.clazz; c != null && c != Object.class; c=c.getSuperclass()) {
        parseField_extraction_2(extraFieldDeserializers,c);
      }
      this.extraFieldDeserializers=extraFieldDeserializers;
    }
    Object deserOrField=extraFieldDeserializers.get(key);
    if (deserOrField != null) {
      if (deserOrField instanceof FieldDeserializer) {
        fieldDeserializer=((FieldDeserializer)deserOrField);
      }
 else {
        Field field=(Field)deserOrField;
        field.setAccessible(true);
        FieldInfo fieldInfo=new FieldInfo(key,field.getDeclaringClass(),field.getType(),field.getGenericType(),field,0,0,0);
        fieldDeserializer=new DefaultFieldDeserializer(parser.getConfig(),clazz,fieldInfo);
        extraFieldDeserializers.put(key,fieldDeserializer);
      }
    }
  }
  if (fieldDeserializer == null) {
    if (!lexer.isEnabled(Feature.IgnoreNotMatch)) {
      throw new JSONException("setter not found, class " + clazz.getName() + ", property "+ key);
    }
    int fieldIndex=-1;
    fieldIndex=parseField_extraction_3(parser,key,object,objectType,fieldValues,lexer,fieldIndex);
    if (fieldIndex != -1) {
      if (setFlags != null) {
        int flagIndex=fieldIndex / 32;
        int bitIndex=fieldIndex % 32;
        setFlags[flagIndex]|=(1 << bitIndex);
      }
      return true;
    }
    parser.parseExtra(object,key);
    return false;
  }
  int fieldIndex=-1;
  return parseField_extraction_5(parser,key,object,objectType,fieldValues,setFlags,lexer,fieldDeserializer,fieldIndex);
}

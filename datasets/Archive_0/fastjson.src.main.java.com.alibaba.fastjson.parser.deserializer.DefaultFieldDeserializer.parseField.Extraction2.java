@Override public void parseField(DefaultJSONParser parser,Object object,Type objectType,Map<String,Object> fieldValues){
  if (this.fieldValueDeserilizer == null) {
    getFieldValueDeserilizer(parser.getConfig());
  }
  ObjectDeserializer fieldValueDeserilizer=this.fieldValueDeserilizer;
  Type fieldType=fieldInfo.fieldType;
  if (objectType instanceof ParameterizedType) {
    ParseContext objContext=parser.getContext();
    if (objContext != null) {
      objContext.type=objectType;
    }
    if (fieldType != objectType) {
      fieldType=FieldInfo.getFieldType(this.clazz,objectType,fieldType);
      if (fieldValueDeserilizer instanceof JavaObjectDeserializer) {
        fieldValueDeserilizer=parser.getConfig().getDeserializer(fieldType);
      }
    }
  }
  Object value;
  value=parseField_extraction_1(parser,fieldValueDeserilizer,fieldType);
  if (parser.getResolveStatus() == DefaultJSONParser.NeedToResolve) {
    ResolveTask task=parser.getLastResolveTask();
    task.fieldDeserializer=this;
    task.ownerContext=parser.getContext();
    parser.setResolveStatus(DefaultJSONParser.NONE);
  }
 else {
    if (object == null) {
      fieldValues.put(fieldInfo.name,value);
    }
 else {
      setValue(object,value);
    }
  }
}

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
  if (fieldValueDeserilizer instanceof JavaBeanDeserializer && fieldInfo.parserFeatures != 0) {
    JavaBeanDeserializer javaBeanDeser=(JavaBeanDeserializer)fieldValueDeserilizer;
    value=javaBeanDeser.deserialze(parser,fieldType,fieldInfo.name,fieldInfo.parserFeatures);
  }
 else {
    if ((this.fieldInfo.format != null || this.fieldInfo.parserFeatures != 0) && fieldValueDeserilizer instanceof ContextObjectDeserializer) {
      value=((ContextObjectDeserializer)fieldValueDeserilizer).deserialze(parser,fieldType,fieldInfo.name,fieldInfo.format,fieldInfo.parserFeatures);
    }
 else {
      value=fieldValueDeserilizer.deserialze(parser,fieldType,fieldInfo.name);
    }
  }
  value=parseField_extraction_2(value);
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

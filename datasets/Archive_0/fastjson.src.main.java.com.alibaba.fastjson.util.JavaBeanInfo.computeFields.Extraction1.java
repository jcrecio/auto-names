private static void computeFields(Class<?> clazz,Type type,PropertyNamingStrategy propertyNamingStrategy,List<FieldInfo> fieldList,Field[] fields){
  Map<TypeVariable,Type> genericInfo=buildGenericInfo(clazz);
  for (  Field field : fields) {
    int modifiers=field.getModifiers();
    if ((modifiers & Modifier.STATIC) != 0) {
      continue;
    }
    if ((modifiers & Modifier.FINAL) != 0) {
      Class<?> fieldType=field.getType();
      boolean supportReadOnly=Map.class.isAssignableFrom(fieldType) || Collection.class.isAssignableFrom(fieldType) || AtomicLong.class.equals(fieldType)|| AtomicInteger.class.equals(fieldType)|| AtomicBoolean.class.equals(fieldType);
      if (!supportReadOnly) {
        continue;
      }
    }
    boolean contains=false;
    for (    FieldInfo item : fieldList) {
      if (item.name.equals(field.getName())) {
        contains=true;
        break;
      }
    }
    if (contains) {
      continue;
    }
    int ordinal=0, serialzeFeatures=0, parserFeatures=0;
    String propertyName=field.getName();
    JSONField fieldAnnotation=TypeUtils.getAnnotation(field,JSONField.class);
    if (fieldAnnotation != null) {
      if (!fieldAnnotation.deserialize()) {
        continue;
      }
      ordinal=fieldAnnotation.ordinal();
      serialzeFeatures=SerializerFeature.of(fieldAnnotation.serialzeFeatures());
      parserFeatures=Feature.of(fieldAnnotation.parseFeatures());
      if (fieldAnnotation.name().length() != 0) {
        propertyName=fieldAnnotation.name();
      }
    }
    propertyName=computeFields_extraction_4(clazz,type,propertyNamingStrategy,fieldList,genericInfo,field,ordinal,serialzeFeatures,parserFeatures,propertyName,fieldAnnotation);
  }
}

private static void computeFields(Class<?> clazz,Type type,PropertyNamingStrategy propertyNamingStrategy,List<FieldInfo> fieldList,Field[] fields){
  Map<TypeVariable,Type> genericInfo=buildGenericInfo(clazz);
  for (  Field field : fields) {
    int modifiers=field.getModifiers();
    if ((modifiers & Modifier.STATIC) != 0) {
      continue;
    }
    if ((modifiers & Modifier.FINAL) != 0) {
      boolean supportReadOnly=computeFields_extraction_1(field);
      if (!supportReadOnly) {
        continue;
      }
    }
    boolean contains=computeFields_extraction_2(fieldList,field);
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
      propertyName=computeFields_extraction_3(propertyName,fieldAnnotation);
    }
    propertyName=computeFields_extraction_4(clazz,type,propertyNamingStrategy,fieldList,genericInfo,field,ordinal,serialzeFeatures,parserFeatures,propertyName,fieldAnnotation);
  }
}

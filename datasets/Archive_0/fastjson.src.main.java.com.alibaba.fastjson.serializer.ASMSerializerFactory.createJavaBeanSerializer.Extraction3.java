public JavaBeanSerializer createJavaBeanSerializer(SerializeBeanInfo beanInfo) throws Exception {
  Class<?> clazz=beanInfo.beanType;
  if (clazz.isPrimitive()) {
    throw new JSONException("unsupportd class " + clazz.getName());
  }
  JSONType jsonType=TypeUtils.getAnnotation(clazz,JSONType.class);
  FieldInfo[] unsortedGetters=beanInfo.fields;
  for (  FieldInfo fieldInfo : unsortedGetters) {
    if (fieldInfo.field == null && fieldInfo.method != null && fieldInfo.method.getDeclaringClass().isInterface()) {
      return new JavaBeanSerializer(beanInfo);
    }
  }
  FieldInfo[] getters=beanInfo.sortedFields;
  boolean nativeSorted=beanInfo.sortedFields == beanInfo.fields;
  if (getters.length > 256) {
    return new JavaBeanSerializer(beanInfo);
  }
  for (  FieldInfo getter : getters) {
    if (!ASMUtils.checkName(getter.getMember().getName())) {
      return new JavaBeanSerializer(beanInfo);
    }
  }
  return createJavaBeanSerializer_extraction_1(beanInfo,clazz,jsonType,unsortedGetters,getters,nativeSorted);
}

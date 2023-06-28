@SuppressWarnings({"unchecked"}) public static <T>T castToJavaBean(Map<String,Object> map,Class<T> clazz,ParserConfig config){
  try {
    if (clazz == StackTraceElement.class) {
      String declaringClass=(String)map.get("className");
      String methodName=(String)map.get("methodName");
      String fileName=(String)map.get("fileName");
      int lineNumber;
{
        Number value=(Number)map.get("lineNumber");
        if (value == null) {
          lineNumber=0;
        }
 else         if (value instanceof BigDecimal) {
          lineNumber=((BigDecimal)value).intValueExact();
        }
 else {
          lineNumber=value.intValue();
        }
      }
      return (T)new StackTraceElement(declaringClass,methodName,fileName,lineNumber);
    }
{
      Object iClassObject=map.get(JSON.DEFAULT_TYPE_KEY);
      if (iClassObject instanceof String) {
        String className=(String)iClassObject;
        Class<?> loadClazz;
        if (config == null) {
          config=ParserConfig.global;
        }
        loadClazz=config.checkAutoType(className,null);
        if (loadClazz == null) {
          throw new ClassNotFoundException(className + " not found");
        }
        if (!loadClazz.equals(clazz)) {
          return (T)castToJavaBean(map,loadClazz,config);
        }
      }
    }
    return castToJavaBean_extraction_1(map,clazz,config);
  }
 catch (  Exception e) {
    throw new JSONException(e.getMessage(),e);
  }
}

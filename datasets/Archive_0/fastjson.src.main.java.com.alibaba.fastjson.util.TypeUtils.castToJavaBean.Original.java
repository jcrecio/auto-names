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
    if (clazz.isInterface()) {
      JSONObject object;
      if (map instanceof JSONObject) {
        object=(JSONObject)map;
      }
 else {
        object=new JSONObject(map);
      }
      if (config == null) {
        config=ParserConfig.getGlobalInstance();
      }
      ObjectDeserializer deserializer=config.get(clazz);
      if (deserializer != null) {
        String json=JSON.toJSONString(object);
        return JSON.parseObject(json,clazz);
      }
      return (T)Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),new Class<?>[]{clazz},object);
    }
    if (clazz == Locale.class) {
      Object arg0=map.get("language");
      Object arg1=map.get("country");
      if (arg0 instanceof String) {
        String language=(String)arg0;
        if (arg1 instanceof String) {
          String country=(String)arg1;
          return (T)new Locale(language,country);
        }
 else         if (arg1 == null) {
          return (T)new Locale(language);
        }
      }
    }
    if (clazz == String.class && map instanceof JSONObject) {
      return (T)map.toString();
    }
    if (clazz == JSON.class && map instanceof JSONObject) {
      return (T)map;
    }
    if (clazz == LinkedHashMap.class && map instanceof JSONObject) {
      JSONObject jsonObject=(JSONObject)map;
      Map<String,Object> innerMap=jsonObject.getInnerMap();
      if (innerMap instanceof LinkedHashMap) {
        return (T)innerMap;
      }
    }
    if (clazz.isInstance(map)) {
      return (T)map;
    }
    if (clazz == JSONObject.class) {
      return (T)new JSONObject(map);
    }
    if (config == null) {
      config=ParserConfig.getGlobalInstance();
    }
    JavaBeanDeserializer javaBeanDeser=null;
    ObjectDeserializer deserializer=config.getDeserializer(clazz);
    if (deserializer instanceof JavaBeanDeserializer) {
      javaBeanDeser=(JavaBeanDeserializer)deserializer;
    }
    if (javaBeanDeser == null) {
      throw new JSONException("can not get javaBeanDeserializer. " + clazz.getName());
    }
    return (T)javaBeanDeser.createInstance(map,config);
  }
 catch (  Exception e) {
    throw new JSONException(e.getMessage(),e);
  }
}

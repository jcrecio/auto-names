private static void paths(Map<Object,String> values,Map<String,Object> paths,String parent,Object javaObject,SerializeConfig config){
  if (javaObject == null) {
    return;
  }
  String p=values.put(javaObject,parent);
  if (p != null) {
    Class<?> type=javaObject.getClass();
    boolean basicType=type == String.class || type == Boolean.class || type == Character.class || type == UUID.class || type.isEnum() || javaObject instanceof Number || javaObject instanceof Date;
    if (!basicType) {
      return;
    }
  }
  paths.put(parent,javaObject);
  if (javaObject instanceof Map) {
    Map map=(Map)javaObject;
    for (    Object entryObj : map.entrySet()) {
      Map.Entry entry=(Map.Entry)entryObj;
      Object key=entry.getKey();
      if (key instanceof String) {
        String path=parent.equals("/") ? "/" + key : parent + "/" + key;
        paths(values,paths,path,entry.getValue(),config);
      }
    }
    return;
  }
  if (javaObject instanceof Collection) {
    Collection collection=(Collection)javaObject;
    int i=0;
    for (    Object item : collection) {
      String path=parent.equals("/") ? "/" + i : parent + "/" + i;
      paths(values,paths,path,item,config);
      ++i;
    }
    return;
  }
  Class<?> clazz=javaObject.getClass();
  if (clazz.isArray()) {
    int len=Array.getLength(javaObject);
    for (int i=0; i < len; ++i) {
      Object item=Array.get(javaObject,i);
      String path=parent.equals("/") ? "/" + i : parent + "/" + i;
      paths(values,paths,path,item,config);
    }
    return;
  }
  paths_extraction_2(values,paths,parent,javaObject,config,clazz);
}

@SuppressWarnings("unchecked") public static Object toJSON(Object javaObject,SerializeConfig config){
  if (javaObject == null) {
    return null;
  }
  if (javaObject instanceof JSON) {
    return javaObject;
  }
  if (javaObject instanceof Map) {
    Map<Object,Object> map=(Map<Object,Object>)javaObject;
    int size=map.size();
    Map innerMap;
    if (map instanceof LinkedHashMap) {
      innerMap=new LinkedHashMap(size);
    }
 else     if (map instanceof TreeMap) {
      innerMap=new TreeMap();
    }
 else {
      innerMap=new HashMap(size);
    }
    JSONObject json=new JSONObject(innerMap);
    for (    Map.Entry<Object,Object> entry : map.entrySet()) {
      Object key=entry.getKey();
      String jsonKey=TypeUtils.castToString(key);
      Object jsonValue=toJSON(entry.getValue(),config);
      json.put(jsonKey,jsonValue);
    }
    return json;
  }
  if (javaObject instanceof Collection) {
    Collection<Object> collection=(Collection<Object>)javaObject;
    JSONArray array=new JSONArray(collection.size());
    for (    Object item : collection) {
      Object jsonValue=toJSON(item,config);
      array.add(jsonValue);
    }
    return array;
  }
  if (javaObject instanceof JSONSerializable) {
    String json=JSON.toJSONString(javaObject);
    return JSON.parse(json);
  }
  Class<?> clazz=javaObject.getClass();
  return toJSON_extraction_1(javaObject,config,clazz);
}

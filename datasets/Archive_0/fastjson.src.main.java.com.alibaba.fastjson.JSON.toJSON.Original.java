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
  if (clazz.isEnum()) {
    return ((Enum<?>)javaObject).name();
  }
  if (clazz.isArray()) {
    int len=Array.getLength(javaObject);
    JSONArray array=new JSONArray(len);
    for (int i=0; i < len; ++i) {
      Object item=Array.get(javaObject,i);
      Object jsonValue=toJSON(item);
      array.add(jsonValue);
    }
    return array;
  }
  if (ParserConfig.isPrimitive2(clazz)) {
    return javaObject;
  }
  ObjectSerializer serializer=config.getObjectWriter(clazz);
  if (serializer instanceof JavaBeanSerializer) {
    JavaBeanSerializer javaBeanSerializer=(JavaBeanSerializer)serializer;
    JSONType jsonType=javaBeanSerializer.getJSONType();
    boolean ordered=false;
    if (jsonType != null) {
      for (      SerializerFeature serializerFeature : jsonType.serialzeFeatures()) {
        if (serializerFeature == SerializerFeature.SortField || serializerFeature == SerializerFeature.MapSortField) {
          ordered=true;
        }
      }
    }
    JSONObject json=new JSONObject(ordered);
    try {
      Map<String,Object> values=javaBeanSerializer.getFieldValuesMap(javaObject);
      for (      Map.Entry<String,Object> entry : values.entrySet()) {
        json.put(entry.getKey(),toJSON(entry.getValue(),config));
      }
    }
 catch (    Exception e) {
      throw new JSONException("toJSON error",e);
    }
    return json;
  }
  String text=JSON.toJSONString(javaObject,config);
  return JSON.parse(text);
}

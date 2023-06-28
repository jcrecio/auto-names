protected Object getPropertyValue(Object currentObject,String propertyName,long propertyNameHash){
  if (currentObject == null) {
    return null;
  }
  if (currentObject instanceof String) {
    try {
      JSONObject object=(JSONObject)JSON.parse((String)currentObject,parserConfig);
      currentObject=object;
    }
 catch (    Exception ex) {
    }
  }
  if (currentObject instanceof Map) {
    Map map=(Map)currentObject;
    Object val=map.get(propertyName);
    if (val == null && (SIZE == propertyNameHash || LENGTH == propertyNameHash)) {
      val=map.size();
    }
    return val;
  }
  final Class<?> currentClass=currentObject.getClass();
  JavaBeanSerializer beanSerializer=getJavaBeanSerializer(currentClass);
  if (beanSerializer != null) {
    try {
      return beanSerializer.getFieldValue(currentObject,propertyName,propertyNameHash,false);
    }
 catch (    Exception e) {
      throw new JSONPathException("jsonpath error, path " + path + ", segement "+ propertyName,e);
    }
  }
  return getPropertyValue_extraction_1(currentObject,propertyName,propertyNameHash);
}

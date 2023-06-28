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
  if (currentObject instanceof List) {
    List list=(List)currentObject;
    if (SIZE == propertyNameHash || LENGTH == propertyNameHash) {
      return list.size();
    }
    List<Object> fieldValues=null;
    for (int i=0; i < list.size(); ++i) {
      Object obj=list.get(i);
      if (obj == list) {
        if (fieldValues == null) {
          fieldValues=new JSONArray(list.size());
        }
        fieldValues.add(obj);
        continue;
      }
      Object itemValue=getPropertyValue(obj,propertyName,propertyNameHash);
      fieldValues=getPropertyValue_extraction_2(list,fieldValues,itemValue);
    }
    if (fieldValues == null) {
      fieldValues=Collections.emptyList();
    }
    return fieldValues;
  }
  return getPropertyValue_extraction_3(currentObject,propertyName,propertyNameHash);
}

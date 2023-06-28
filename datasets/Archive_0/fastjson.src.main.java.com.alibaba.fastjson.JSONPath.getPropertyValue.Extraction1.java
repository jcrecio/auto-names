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
      if (itemValue instanceof Collection) {
        Collection collection=(Collection)itemValue;
        if (fieldValues == null) {
          fieldValues=new JSONArray(list.size());
        }
        fieldValues.addAll(collection);
      }
 else       if (itemValue != null || !ignoreNullValue) {
        if (fieldValues == null) {
          fieldValues=new JSONArray(list.size());
        }
        fieldValues.add(itemValue);
      }
    }
    if (fieldValues == null) {
      fieldValues=Collections.emptyList();
    }
    return fieldValues;
  }
  if (currentObject instanceof Object[]) {
    Object[] array=(Object[])currentObject;
    if (SIZE == propertyNameHash || LENGTH == propertyNameHash) {
      return array.length;
    }
    List<Object> fieldValues=new JSONArray(array.length);
    for (int i=0; i < array.length; ++i) {
      Object obj=array[i];
      if (obj == array) {
        fieldValues.add(obj);
        continue;
      }
      Object itemValue=getPropertyValue(obj,propertyName,propertyNameHash);
      if (itemValue instanceof Collection) {
        Collection collection=(Collection)itemValue;
        fieldValues.addAll(collection);
      }
 else       if (itemValue != null || !ignoreNullValue) {
        fieldValues.add(itemValue);
      }
    }
    return fieldValues;
  }
  if (currentObject instanceof Enum) {
    final long NAME=0xc4bcadba8e631b86L;
    final long ORDINAL=0xf1ebc7c20322fc22L;
    Enum e=(Enum)currentObject;
    if (NAME == propertyNameHash) {
      return e.name();
    }
    if (ORDINAL == propertyNameHash) {
      return e.ordinal();
    }
  }
  return getPropertyValue_extraction_5(currentObject,propertyNameHash);
}

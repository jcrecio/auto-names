@SuppressWarnings("rawtypes") protected void deepScan(final Object currentObject,final String propertyName,List<Object> results){
  if (currentObject == null) {
    return;
  }
  if (currentObject instanceof Map) {
    Map<?,?> map=(Map<?,?>)currentObject;
    for (    Map.Entry entry : map.entrySet()) {
      Object val=entry.getValue();
      deepScan_extraction_1(propertyName,results,entry,val);
    }
    return;
  }
  if (currentObject instanceof Collection) {
    Iterator iterator=((Collection)currentObject).iterator();
    while (iterator.hasNext()) {
      Object next=iterator.next();
      if (ParserConfig.isPrimitive2(next.getClass())) {
        continue;
      }
      deepScan(next,propertyName,results);
    }
    return;
  }
  final Class<?> currentClass=currentObject.getClass();
  JavaBeanSerializer beanSerializer=getJavaBeanSerializer(currentClass);
  if (beanSerializer != null) {
    deepScan_extraction_2(currentObject,propertyName,results,beanSerializer);
  }
  if (currentObject instanceof List) {
    List list=(List)currentObject;
    for (int i=0; i < list.size(); ++i) {
      Object val=list.get(i);
      deepScan(val,propertyName,results);
    }
    return;
  }
}

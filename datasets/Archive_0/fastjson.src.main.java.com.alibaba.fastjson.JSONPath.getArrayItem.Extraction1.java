@SuppressWarnings("rawtypes") protected Object getArrayItem(final Object currentObject,int index){
  if (currentObject == null) {
    return null;
  }
  if (currentObject instanceof List) {
    List list=(List)currentObject;
    if (index >= 0) {
      if (index < list.size()) {
        return list.get(index);
      }
      return null;
    }
 else {
      if (Math.abs(index) <= list.size()) {
        return list.get(list.size() + index);
      }
      return null;
    }
  }
  if (currentObject.getClass().isArray()) {
    return getArrayItem_extraction_2(currentObject,index);
  }
  if (currentObject instanceof Map) {
    Map map=(Map)currentObject;
    Object value=map.get(index);
    if (value == null) {
      value=map.get(Integer.toString(index));
    }
    return value;
  }
  if (currentObject instanceof Collection) {
    Collection collection=(Collection)currentObject;
    int i=0;
    for (    Object item : collection) {
      if (i == index) {
        return item;
      }
      i++;
    }
    return null;
  }
  if (index == 0) {
    return currentObject;
  }
  throw new UnsupportedOperationException();
}

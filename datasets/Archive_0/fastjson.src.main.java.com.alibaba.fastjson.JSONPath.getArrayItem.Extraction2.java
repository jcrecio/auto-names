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
  return getArrayItem_extraction_1(currentObject,index);
}

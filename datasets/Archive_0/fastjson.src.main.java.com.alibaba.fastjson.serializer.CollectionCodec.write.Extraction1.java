public void write(JSONSerializer serializer,Object object,Object fieldName,Type fieldType,int features) throws IOException {
  SerializeWriter out=serializer.out;
  if (object == null) {
    out.writeNull(SerializerFeature.WriteNullListAsEmpty);
    return;
  }
  Type elementType=null;
  if (out.isEnabled(SerializerFeature.WriteClassName) || SerializerFeature.isEnabled(features,SerializerFeature.WriteClassName)) {
    elementType=TypeUtils.getCollectionItemType(fieldType);
  }
  Collection<?> collection=(Collection<?>)object;
  SerialContext context=serializer.context;
  serializer.setContext(context,object,fieldName,0);
  if (out.isEnabled(SerializerFeature.WriteClassName)) {
    if (HashSet.class.isAssignableFrom(collection.getClass())) {
      out.append("Set");
    }
 else     if (TreeSet.class == collection.getClass()) {
      out.append("TreeSet");
    }
  }
  try {
    int i=0;
    out.append('[');
    for (    Object item : collection) {
      if (i++ != 0) {
        out.append(',');
      }
      if (item == null) {
        out.writeNull();
        continue;
      }
      Class<?> clazz=item.getClass();
      if (clazz == Integer.class) {
        out.writeInt(((Integer)item).intValue());
        continue;
      }
      if (clazz == Long.class) {
        out.writeLong(((Long)item).longValue());
        if (out.isEnabled(SerializerFeature.WriteClassName)) {
          out.write('L');
        }
        continue;
      }
      ObjectSerializer itemSerializer=write_extraction_2(serializer,features,elementType,i,item,clazz);
    }
    out.append(']');
  }
  finally {
    serializer.context=context;
  }
}

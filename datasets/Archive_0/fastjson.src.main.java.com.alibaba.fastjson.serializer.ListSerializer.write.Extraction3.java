public final void write(JSONSerializer serializer,Object object,Object fieldName,Type fieldType,int features) throws IOException {
  boolean writeClassName=serializer.out.isEnabled(SerializerFeature.WriteClassName) || SerializerFeature.isEnabled(features,SerializerFeature.WriteClassName);
  SerializeWriter out=serializer.out;
  Type elementType=null;
  if (writeClassName) {
    elementType=TypeUtils.getCollectionItemType(fieldType);
  }
  if (object == null) {
    out.writeNull(SerializerFeature.WriteNullListAsEmpty);
    return;
  }
  List<?> list=(List<?>)object;
  if (list.size() == 0) {
    out.append("[]");
    return;
  }
  SerialContext context=serializer.context;
  serializer.setContext(context,object,fieldName,0);
  ObjectSerializer itemSerializer=null;
  try {
    if (out.isEnabled(SerializerFeature.PrettyFormat)) {
      out.append('[');
      serializer.incrementIndent();
      int i=0;
      write_extraction_1(serializer,object,fieldName,features,out,elementType,list,context,i);
      serializer.println();
      out.append(']');
      return;
    }
    out.append('[');
    for (int i=0, size=list.size(); i < size; ++i) {
      Object item=list.get(i);
      if (i != 0) {
        out.append(',');
      }
      if (item == null) {
        out.append("null");
      }
 else {
        Class<?> clazz=item.getClass();
        if (clazz == Integer.class) {
          out.writeInt(((Integer)item).intValue());
        }
 else         write_extraction_2(serializer,object,fieldName,features,writeClassName,out,elementType,context,i,item,clazz);
      }
    }
    out.append(']');
  }
  finally {
    serializer.context=context;
  }
}

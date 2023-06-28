public void writeValue(JSONSerializer serializer,Object propertyValue) throws Exception {
  if (runtimeInfo == null) {
    Class<?> runtimeFieldClass;
    if (propertyValue == null) {
      runtimeFieldClass=this.fieldInfo.fieldClass;
      if (runtimeFieldClass == byte.class) {
        runtimeFieldClass=Byte.class;
      }
 else       if (runtimeFieldClass == short.class) {
        runtimeFieldClass=Short.class;
      }
 else       if (runtimeFieldClass == int.class) {
        runtimeFieldClass=Integer.class;
      }
 else       if (runtimeFieldClass == long.class) {
        runtimeFieldClass=Long.class;
      }
 else       if (runtimeFieldClass == float.class) {
        runtimeFieldClass=Float.class;
      }
 else       if (runtimeFieldClass == double.class) {
        runtimeFieldClass=Double.class;
      }
 else       if (runtimeFieldClass == boolean.class) {
        runtimeFieldClass=Boolean.class;
      }
    }
 else {
      runtimeFieldClass=propertyValue.getClass();
    }
    ObjectSerializer fieldSerializer=null;
    JSONField fieldAnnotation=fieldInfo.getAnnotation();
    if (fieldAnnotation != null && fieldAnnotation.serializeUsing() != Void.class) {
      fieldSerializer=(ObjectSerializer)fieldAnnotation.serializeUsing().newInstance();
      serializeUsing=true;
    }
 else {
      if (format != null) {
        if (runtimeFieldClass == double.class || runtimeFieldClass == Double.class) {
          fieldSerializer=new DoubleSerializer(format);
        }
 else         if (runtimeFieldClass == float.class || runtimeFieldClass == Float.class) {
          fieldSerializer=new FloatCodec(format);
        }
      }
      if (fieldSerializer == null) {
        fieldSerializer=serializer.getObjectWriter(runtimeFieldClass);
      }
    }
    runtimeInfo=new RuntimeSerializerInfo(fieldSerializer,runtimeFieldClass);
  }
  final RuntimeSerializerInfo runtimeInfo=this.runtimeInfo;
  final int fieldFeatures=(disableCircularReferenceDetect ? (fieldInfo.serialzeFeatures | SerializerFeature.DisableCircularReferenceDetect.mask) : fieldInfo.serialzeFeatures) | features;
  if (propertyValue == null) {
    writeValue_extraction_3(serializer,runtimeInfo,fieldFeatures);
  }
  if (fieldInfo.isEnum) {
    if (writeEnumUsingName) {
      serializer.out.writeString(((Enum<?>)propertyValue).name());
      return;
    }
    if (writeEnumUsingToString) {
      serializer.out.writeString(((Enum<?>)propertyValue).toString());
      return;
    }
  }
  Class<?> valueClass=propertyValue.getClass();
  ObjectSerializer valueSerializer;
  if (valueClass == runtimeInfo.runtimeFieldClass || serializeUsing) {
    valueSerializer=runtimeInfo.fieldSerializer;
  }
 else {
    valueSerializer=serializer.getObjectWriter(valueClass);
  }
  if (format != null && !(valueSerializer instanceof DoubleSerializer || valueSerializer instanceof FloatCodec)) {
    if (valueSerializer instanceof ContextObjectSerializer) {
      ((ContextObjectSerializer)valueSerializer).write(serializer,propertyValue,this.fieldContext);
    }
 else {
      serializer.writeWithFormat(propertyValue,format);
    }
    return;
  }
  writeValue_extraction_4(serializer,propertyValue,fieldFeatures,valueClass,valueSerializer);
}

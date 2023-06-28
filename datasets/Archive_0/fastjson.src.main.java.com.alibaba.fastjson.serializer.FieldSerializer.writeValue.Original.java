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
    SerializeWriter out=serializer.out;
    if (fieldInfo.fieldClass == Object.class && out.isEnabled(SerializerFeature.WRITE_MAP_NULL_FEATURES)) {
      out.writeNull();
      return;
    }
    Class<?> runtimeFieldClass=runtimeInfo.runtimeFieldClass;
    if (Number.class.isAssignableFrom(runtimeFieldClass)) {
      out.writeNull(features,SerializerFeature.WriteNullNumberAsZero.mask);
      return;
    }
 else     if (String.class == runtimeFieldClass) {
      out.writeNull(features,SerializerFeature.WriteNullStringAsEmpty.mask);
      return;
    }
 else     if (Boolean.class == runtimeFieldClass) {
      out.writeNull(features,SerializerFeature.WriteNullBooleanAsFalse.mask);
      return;
    }
 else     if (Collection.class.isAssignableFrom(runtimeFieldClass) || runtimeFieldClass.isArray()) {
      out.writeNull(features,SerializerFeature.WriteNullListAsEmpty.mask);
      return;
    }
    ObjectSerializer fieldSerializer=runtimeInfo.fieldSerializer;
    if ((out.isEnabled(SerializerFeature.WRITE_MAP_NULL_FEATURES)) && fieldSerializer instanceof JavaBeanSerializer) {
      out.writeNull();
      return;
    }
    fieldSerializer.write(serializer,null,fieldInfo.name,fieldInfo.fieldType,fieldFeatures);
    return;
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
  if (fieldInfo.unwrapped) {
    if (valueSerializer instanceof JavaBeanSerializer) {
      JavaBeanSerializer javaBeanSerializer=(JavaBeanSerializer)valueSerializer;
      javaBeanSerializer.write(serializer,propertyValue,fieldInfo.name,fieldInfo.fieldType,fieldFeatures,true);
      return;
    }
    if (valueSerializer instanceof MapSerializer) {
      MapSerializer mapSerializer=(MapSerializer)valueSerializer;
      mapSerializer.write(serializer,propertyValue,fieldInfo.name,fieldInfo.fieldType,fieldFeatures,true);
      return;
    }
  }
  if ((features & SerializerFeature.WriteClassName.mask) != 0 && valueClass != fieldInfo.fieldClass && valueSerializer instanceof JavaBeanSerializer) {
    ((JavaBeanSerializer)valueSerializer).write(serializer,propertyValue,fieldInfo.name,fieldInfo.fieldType,fieldFeatures,false);
    return;
  }
  if (browserCompatible && (fieldInfo.fieldClass == long.class || fieldInfo.fieldClass == Long.class)) {
    long value=(Long)propertyValue;
    if (value > 9007199254740991L || value < -9007199254740991L) {
      serializer.getWriter().writeString(Long.toString(value));
      return;
    }
  }
  valueSerializer.write(serializer,propertyValue,fieldInfo.name,fieldInfo.fieldType,fieldFeatures);
}

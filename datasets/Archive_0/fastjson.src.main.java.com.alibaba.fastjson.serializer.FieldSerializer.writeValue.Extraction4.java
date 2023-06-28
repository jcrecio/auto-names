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
    fieldSerializer=writeValue_extraction_1(serializer,runtimeFieldClass,fieldSerializer,fieldAnnotation);
    runtimeInfo=new RuntimeSerializerInfo(fieldSerializer,runtimeFieldClass);
  }
  final RuntimeSerializerInfo runtimeInfo=this.runtimeInfo;
  final int fieldFeatures=(disableCircularReferenceDetect ? (fieldInfo.serialzeFeatures | SerializerFeature.DisableCircularReferenceDetect.mask) : fieldInfo.serialzeFeatures) | features;
  writeValue_extraction_2(serializer,propertyValue,runtimeInfo,fieldFeatures);
}

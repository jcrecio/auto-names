public FieldDeserializer smartMatch(String key,int[] setFlags){
  if (key == null) {
    return null;
  }
  FieldDeserializer fieldDeserializer=getFieldDeserializer(key,setFlags);
  if (fieldDeserializer == null) {
    long smartKeyHash;
    int pos=smartMatch_extraction_1(key);
    boolean is=false;
    if (pos < 0 && (is=key.startsWith("is"))) {
      smartKeyHash=TypeUtils.fnv1a_64_extract(key.substring(2));
      pos=Arrays.binarySearch(smartMatchHashArray,smartKeyHash);
    }
    fieldDeserializer=smartMatch_extraction_2(setFlags,fieldDeserializer,pos);
    if (fieldDeserializer != null) {
      FieldInfo fieldInfo=fieldDeserializer.fieldInfo;
      if ((fieldInfo.parserFeatures & Feature.DisableFieldSmartMatch.mask) != 0) {
        return null;
      }
      Class fieldClass=fieldInfo.fieldClass;
      if (is && (fieldClass != boolean.class && fieldClass != Boolean.class)) {
        fieldDeserializer=null;
      }
    }
  }
  return fieldDeserializer;
}

public JavaBeanSerializer createJavaBeanSerializer(SerializeBeanInfo beanInfo) throws Exception {
  Class<?> clazz=beanInfo.beanType;
  if (clazz.isPrimitive()) {
    throw new JSONException("unsupportd class " + clazz.getName());
  }
  JSONType jsonType=TypeUtils.getAnnotation(clazz,JSONType.class);
  FieldInfo[] unsortedGetters=beanInfo.fields;
  for (  FieldInfo fieldInfo : unsortedGetters) {
    if (fieldInfo.field == null && fieldInfo.method != null && fieldInfo.method.getDeclaringClass().isInterface()) {
      return new JavaBeanSerializer(beanInfo);
    }
  }
  FieldInfo[] getters=beanInfo.sortedFields;
  boolean nativeSorted=beanInfo.sortedFields == beanInfo.fields;
  if (getters.length > 256) {
    return new JavaBeanSerializer(beanInfo);
  }
  for (  FieldInfo getter : getters) {
    if (!ASMUtils.checkName(getter.getMember().getName())) {
      return new JavaBeanSerializer(beanInfo);
    }
  }
  String className="ASMSerializer_" + seed.incrementAndGet() + "_"+ clazz.getSimpleName();
  String classNameType;
  String classNameFull;
  Package pkg=ASMSerializerFactory.class.getPackage();
  if (pkg != null) {
    String packageName=pkg.getName();
    classNameType=packageName.replace('.','/') + "/" + className;
    classNameFull=packageName + "." + className;
  }
 else {
    classNameType=className;
    classNameFull=className;
  }
  ClassWriter cw=new ClassWriter();
  cw.visit(V1_5,ACC_PUBLIC + ACC_SUPER,classNameType,JavaBeanSerializer,new String[]{ObjectSerializer});
  MethodVisitor mw=createJavaBeanSerializer_extraction_2(getters,classNameType,cw);
  mw.visitInsn(RETURN);
  mw.visitMaxs(4,4);
  mw.visitEnd();
  boolean DisableCircularReferenceDetect=false;
  if (jsonType != null) {
    for (    SerializerFeature featrues : jsonType.serialzeFeatures()) {
      if (featrues == SerializerFeature.DisableCircularReferenceDetect) {
        DisableCircularReferenceDetect=true;
        break;
      }
    }
  }
  for (int i=0; i < 3; ++i) {
    createJavaBeanSerializer_extraction_3(beanInfo,clazz,jsonType,getters,nativeSorted,classNameType,cw,DisableCircularReferenceDetect,i);
  }
  if (!nativeSorted) {
    Context context=new Context(getters,beanInfo,classNameType,false,DisableCircularReferenceDetect);
    mw=new MethodWriter(cw,ACC_PUBLIC,"writeUnsorted","(L" + JSONSerializer + ";Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;I)V",null,new String[]{"java/io/IOException"});
    mw.visitVarInsn(ALOAD,Context.serializer);
    mw.visitFieldInsn(GETFIELD,JSONSerializer,"out",SerializeWriter_desc);
    mw.visitVarInsn(ASTORE,context.var("out"));
    mw.visitVarInsn(ALOAD,Context.obj);
    mw.visitTypeInsn(CHECKCAST,type(clazz));
    mw.visitVarInsn(ASTORE,context.var("entity"));
    generateWriteMethod(clazz,mw,unsortedGetters,context);
    mw.visitInsn(RETURN);
    mw.visitMaxs(7,context.variantIndex + 2);
    mw.visitEnd();
  }
  for (int i=0; i < 3; ++i) {
    String methodName;
    boolean nonContext=DisableCircularReferenceDetect;
    boolean writeDirect=false;
    if (i == 0) {
      methodName="writeAsArray";
      writeDirect=true;
    }
 else     if (i == 1) {
      methodName="writeAsArrayNormal";
    }
 else {
      writeDirect=true;
      nonContext=true;
      methodName="writeAsArrayNonContext";
    }
    Context context=new Context(getters,beanInfo,classNameType,writeDirect,nonContext);
    mw=new MethodWriter(cw,ACC_PUBLIC,methodName,"(L" + JSONSerializer + ";Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;I)V",null,new String[]{"java/io/IOException"});
    mw.visitVarInsn(ALOAD,Context.serializer);
    mw.visitFieldInsn(GETFIELD,JSONSerializer,"out",SerializeWriter_desc);
    mw.visitVarInsn(ASTORE,context.var("out"));
    mw.visitVarInsn(ALOAD,Context.obj);
    mw.visitTypeInsn(CHECKCAST,type(clazz));
    mw.visitVarInsn(ASTORE,context.var("entity"));
    generateWriteAsArray(clazz,mw,getters,context);
    mw.visitInsn(RETURN);
    mw.visitMaxs(7,context.variantIndex + 2);
    mw.visitEnd();
  }
  byte[] code=cw.toByteArray();
  Class<?> serializerClass=classLoader.defineClassPublic(classNameFull,code,0,code.length);
  Constructor<?> constructor=serializerClass.getConstructor(SerializeBeanInfo.class);
  Object instance=constructor.newInstance(beanInfo);
  return (JavaBeanSerializer)instance;
}

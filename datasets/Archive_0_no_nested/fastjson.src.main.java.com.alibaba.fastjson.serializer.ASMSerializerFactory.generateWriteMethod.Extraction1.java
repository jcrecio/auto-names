private void generateWriteMethod(Class<?> clazz,MethodVisitor mw,FieldInfo[] getters,Context context) throws Exception {
  Label end=new Label();
  int size=getters.length;
  if (!context.writeDirect) {
    Label endSupper_=new Label();
    Label supper_=new Label();
    mw.visitVarInsn(ALOAD,context.var("out"));
    mw.visitLdcInsn(SerializerFeature.PrettyFormat.mask);
    mw.visitMethodInsn(INVOKEVIRTUAL,SerializeWriter,"isEnabled","(I)Z");
    mw.visitJumpInsn(IFNE,supper_);
    boolean hasMethod=false;
    for (    FieldInfo getter : getters) {
      if (getter.method != null) {
        hasMethod=true;
        break;
      }
    }
    if (hasMethod) {
      mw.visitVarInsn(ALOAD,context.var("out"));
      mw.visitLdcInsn(SerializerFeature.IgnoreErrorGetter.mask);
      mw.visitMethodInsn(INVOKEVIRTUAL,SerializeWriter,"isEnabled","(I)Z");
      mw.visitJumpInsn(IFEQ,endSupper_);
    }
 else {
      mw.visitJumpInsn(GOTO,endSupper_);
    }
    mw.visitLabel(supper_);
    mw.visitVarInsn(ALOAD,0);
    mw.visitVarInsn(ALOAD,1);
    mw.visitVarInsn(ALOAD,2);
    mw.visitVarInsn(ALOAD,3);
    mw.visitVarInsn(ALOAD,4);
    mw.visitVarInsn(ILOAD,5);
    mw.visitMethodInsn(INVOKESPECIAL,JavaBeanSerializer,"write","(L" + JSONSerializer + ";Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;I)V");
    mw.visitInsn(RETURN);
    mw.visitLabel(endSupper_);
  }
  if (!context.nonContext) {
    Label endRef_=new Label();
    mw.visitVarInsn(ALOAD,0);
    mw.visitVarInsn(ALOAD,Context.serializer);
    mw.visitVarInsn(ALOAD,Context.obj);
    mw.visitVarInsn(ILOAD,Context.features);
    mw.visitMethodInsn(INVOKEVIRTUAL,JavaBeanSerializer,"writeReference","(L" + JSONSerializer + ";Ljava/lang/Object;I)Z");
    mw.visitJumpInsn(IFEQ,endRef_);
    mw.visitInsn(RETURN);
    mw.visitLabel(endRef_);
  }
  final String writeAsArrayMethodName;
  if (context.writeDirect) {
    if (context.nonContext) {
      writeAsArrayMethodName="writeAsArrayNonContext";
    }
 else {
      writeAsArrayMethodName="writeAsArray";
    }
  }
 else {
    writeAsArrayMethodName="writeAsArrayNormal";
  }
  if ((context.beanInfo.features & SerializerFeature.BeanToArray.mask) == 0) {
    Label endWriteAsArray_=new Label();
    mw.visitVarInsn(ALOAD,context.var("out"));
    mw.visitLdcInsn(SerializerFeature.BeanToArray.mask);
    mw.visitMethodInsn(INVOKEVIRTUAL,SerializeWriter,"isEnabled","(I)Z");
    mw.visitJumpInsn(IFEQ,endWriteAsArray_);
    mw.visitVarInsn(ALOAD,0);
    mw.visitVarInsn(ALOAD,Context.serializer);
    mw.visitVarInsn(ALOAD,2);
    mw.visitVarInsn(ALOAD,3);
    mw.visitVarInsn(ALOAD,4);
    mw.visitVarInsn(ILOAD,5);
    mw.visitMethodInsn(INVOKEVIRTUAL,context.className,writeAsArrayMethodName,"(L" + JSONSerializer + ";Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;I)V");
    mw.visitInsn(RETURN);
    mw.visitLabel(endWriteAsArray_);
  }
 else {
    mw.visitVarInsn(ALOAD,0);
    mw.visitVarInsn(ALOAD,Context.serializer);
    mw.visitVarInsn(ALOAD,2);
    mw.visitVarInsn(ALOAD,3);
    mw.visitVarInsn(ALOAD,4);
    mw.visitVarInsn(ILOAD,5);
    mw.visitMethodInsn(INVOKEVIRTUAL,context.className,writeAsArrayMethodName,"(L" + JSONSerializer + ";Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;I)V");
    mw.visitInsn(RETURN);
  }
  if (!context.nonContext) {
    mw.visitVarInsn(ALOAD,Context.serializer);
    mw.visitMethodInsn(INVOKEVIRTUAL,JSONSerializer,"getContext","()" + SerialContext_desc);
    mw.visitVarInsn(ASTORE,context.var("parent"));
    mw.visitVarInsn(ALOAD,Context.serializer);
    mw.visitVarInsn(ALOAD,context.var("parent"));
    mw.visitVarInsn(ALOAD,Context.obj);
    mw.visitVarInsn(ALOAD,Context.paramFieldName);
    mw.visitLdcInsn(context.beanInfo.features);
    mw.visitMethodInsn(INVOKEVIRTUAL,JSONSerializer,"setContext","(" + SerialContext_desc + "Ljava/lang/Object;Ljava/lang/Object;I)V");
  }
  boolean writeClasName=(context.beanInfo.features & SerializerFeature.WriteClassName.mask) != 0;
  if (writeClasName || !context.writeDirect) {
    Label end_=new Label();
    Label else_=new Label();
    Label writeClass_=new Label();
    if (!writeClasName) {
      mw.visitVarInsn(ALOAD,Context.serializer);
      mw.visitVarInsn(ALOAD,Context.paramFieldType);
      mw.visitVarInsn(ALOAD,Context.obj);
      mw.visitMethodInsn(INVOKEVIRTUAL,JSONSerializer,"isWriteClassName","(Ljava/lang/reflect/Type;Ljava/lang/Object;)Z");
      mw.visitJumpInsn(IFEQ,else_);
    }
    mw.visitVarInsn(ALOAD,Context.paramFieldType);
    mw.visitVarInsn(ALOAD,Context.obj);
    mw.visitMethodInsn(INVOKEVIRTUAL,"java/lang/Object","getClass","()Ljava/lang/Class;");
    mw.visitJumpInsn(IF_ACMPEQ,else_);
    mw.visitLabel(writeClass_);
    mw.visitVarInsn(ALOAD,context.var("out"));
    mw.visitVarInsn(BIPUSH,'{');
    mw.visitMethodInsn(INVOKEVIRTUAL,SerializeWriter,"write","(I)V");
    mw.visitVarInsn(ALOAD,0);
    mw.visitVarInsn(ALOAD,Context.serializer);
    if (context.beanInfo.typeKey != null) {
      mw.visitLdcInsn(context.beanInfo.typeKey);
    }
 else {
      mw.visitInsn(ACONST_NULL);
    }
    mw.visitVarInsn(ALOAD,Context.obj);
    mw.visitMethodInsn(INVOKEVIRTUAL,JavaBeanSerializer,"writeClassName","(L" + JSONSerializer + ";Ljava/lang/String;Ljava/lang/Object;)V");
    mw.visitVarInsn(BIPUSH,',');
    mw.visitJumpInsn(GOTO,end_);
    mw.visitLabel(else_);
    mw.visitVarInsn(BIPUSH,'{');
    mw.visitLabel(end_);
  }
 else {
    mw.visitVarInsn(BIPUSH,'{');
  }
  mw.visitVarInsn(ISTORE,context.var("seperator"));
  if (!context.writeDirect) {
    _before(mw,context);
  }
  if (!context.writeDirect) {
    mw.visitVarInsn(ALOAD,context.var("out"));
    mw.visitMethodInsn(INVOKEVIRTUAL,SerializeWriter,"isNotWriteDefaultValue","()Z");
    mw.visitVarInsn(ISTORE,context.var("notWriteDefaultValue"));
    mw.visitVarInsn(ALOAD,Context.serializer);
    mw.visitVarInsn(ALOAD,0);
    mw.visitMethodInsn(INVOKEVIRTUAL,JSONSerializer,"checkValue","(" + SerializeFilterable_desc + ")Z");
    mw.visitVarInsn(ISTORE,context.var("checkValue"));
    mw.visitVarInsn(ALOAD,Context.serializer);
    mw.visitVarInsn(ALOAD,0);
    mw.visitMethodInsn(INVOKEVIRTUAL,JSONSerializer,"hasNameFilters","(" + SerializeFilterable_desc + ")Z");
    mw.visitVarInsn(ISTORE,context.var("hasNameFilters"));
  }
  Label _end_if=generateWriteMethod_extraction_2(clazz,mw,getters,context,size);
  mw.visitVarInsn(ALOAD,context.var("out"));
  mw.visitVarInsn(BIPUSH,'}');
  mw.visitMethodInsn(INVOKEVIRTUAL,SerializeWriter,"write","(I)V");
  mw.visitLabel(_end_if);
  mw.visitLabel(end);
  if (!context.nonContext) {
    mw.visitVarInsn(ALOAD,Context.serializer);
    mw.visitVarInsn(ALOAD,context.var("parent"));
    mw.visitMethodInsn(INVOKEVIRTUAL,JSONSerializer,"setContext","(" + SerialContext_desc + ")V");
  }
}

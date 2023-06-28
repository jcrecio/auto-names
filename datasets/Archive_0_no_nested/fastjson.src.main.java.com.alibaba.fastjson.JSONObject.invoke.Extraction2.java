public Object invoke(Object proxy,Method method,Object[] args) throws Throwable {
  Class<?>[] parameterTypes=method.getParameterTypes();
  if (parameterTypes.length == 1) {
    if (method.getName().equals("equals")) {
      return this.equals(args[0]);
    }
    Class<?> returnType=method.getReturnType();
    invoke_extraction_1(method,args,returnType);
    return null;
  }
  if (parameterTypes.length == 0) {
    Class<?> returnType=method.getReturnType();
    if (returnType == void.class) {
      throw new JSONException("illegal getter");
    }
    String name=null;
    JSONField annotation=TypeUtils.getAnnotation(method,JSONField.class);
    if (annotation != null) {
      if (annotation.name().length() != 0) {
        name=annotation.name();
      }
    }
    return invoke_extraction_2(method,name);
  }
  throw new UnsupportedOperationException(method.toGenericString());
}

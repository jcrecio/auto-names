public Object invoke(Object proxy,Method method,Object[] args) throws Throwable {
  Class<?>[] parameterTypes=method.getParameterTypes();
  if (parameterTypes.length == 1) {
    if (method.getName().equals("equals")) {
      return this.equals(args[0]);
    }
    Class<?> returnType=method.getReturnType();
    if (returnType != void.class) {
      throw new JSONException("illegal setter");
    }
    String name=null;
    JSONField annotation=TypeUtils.getAnnotation(method,JSONField.class);
    if (annotation != null) {
      if (annotation.name().length() != 0) {
        name=annotation.name();
      }
    }
    if (name == null) {
      name=method.getName();
      if (!name.startsWith("set")) {
        throw new JSONException("illegal setter");
      }
      name=name.substring(3);
      if (name.length() == 0) {
        throw new JSONException("illegal setter");
      }
      name=Character.toLowerCase(name.charAt(0)) + name.substring(1);
    }
    map.put(name,args[0]);
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
    if (name == null) {
      name=method.getName();
      if (name.startsWith("get")) {
        name=name.substring(3);
        if (name.length() == 0) {
          throw new JSONException("illegal getter");
        }
        name=Character.toLowerCase(name.charAt(0)) + name.substring(1);
      }
 else       if (name.startsWith("is")) {
        name=name.substring(2);
        if (name.length() == 0) {
          throw new JSONException("illegal getter");
        }
        name=Character.toLowerCase(name.charAt(0)) + name.substring(1);
      }
 else       if (name.startsWith("hashCode")) {
        return this.hashCode();
      }
 else       if (name.startsWith("toString")) {
        return this.toString();
      }
 else {
        throw new JSONException("illegal getter");
      }
    }
    Object value=map.get(name);
    return TypeUtils.cast(value,method.getGenericReturnType(),ParserConfig.getGlobalInstance());
  }
  throw new UnsupportedOperationException(method.toGenericString());
}

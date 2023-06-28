public void handleResovleTask(Object value){
  if (resolveTaskList == null) {
    return;
  }
  for (int i=0, size=resolveTaskList.size(); i < size; ++i) {
    ResolveTask task=resolveTaskList.get(i);
    String ref=task.referenceValue;
    Object object=null;
    if (task.ownerContext != null) {
      object=task.ownerContext.object;
    }
    Object refValue;
    if (ref.startsWith("$")) {
      refValue=getObject(ref);
      if (refValue == null) {
        try {
          JSONPath jsonpath=new JSONPath(ref,SerializeConfig.getGlobalInstance(),config,true);
          if (jsonpath.isRef()) {
            refValue=jsonpath.eval(value);
          }
        }
 catch (        JSONPathException ex) {
        }
      }
    }
 else {
      refValue=task.context.object;
    }
    FieldDeserializer fieldDeser=task.fieldDeserializer;
    if (fieldDeser != null) {
      if (refValue != null && refValue.getClass() == JSONObject.class && fieldDeser.fieldInfo != null && !Map.class.isAssignableFrom(fieldDeser.fieldInfo.fieldClass)) {
        Object root=this.contextArray[0].object;
        JSONPath jsonpath=JSONPath.compile(ref);
        if (jsonpath.isRef()) {
          refValue=jsonpath.eval(root);
        }
      }
      if (fieldDeser.getOwnerClass() != null && (!fieldDeser.getOwnerClass().isInstance(object)) && task.ownerContext.parent != null) {
        for (ParseContext ctx=task.ownerContext.parent; ctx != null; ctx=ctx.parent) {
          if (fieldDeser.getOwnerClass().isInstance(ctx.object)) {
            object=ctx.object;
            break;
          }
        }
      }
      fieldDeser.setValue(object,refValue);
    }
  }
}

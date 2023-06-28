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
      refValue=handleResovleTask_extraction_1(value,ref,refValue);
    }
 else {
      refValue=task.context.object;
    }
    FieldDeserializer fieldDeser=task.fieldDeserializer;
    if (fieldDeser != null) {
      handleResovleTask_extraction_2(task,ref,object,refValue,fieldDeser);
    }
  }
}

/** 
 * Recursively walks the object graph to 1) determine the nesting of objects to help determine which objects are to be instrumented; and 2) attach the {@code collector}s to any matching objects. <p> In order to avoid cycles in the object graph, objects are only traversed the first time they are encountered.  If an object appears multiple times in the object graph, the  {@code instrument} method will only beinvoked once. <p> When generating the nesting of objects, anonymous classes are given the placeholder type  {@code "(Anonymous)"}, without quotes.  While the contents of arrays and  {@link Collection}s are listed in the nesting, the array/collection object itself is not listed.  For example, the nesting will show  {@code CompoundVariation >> PM} instead of{@code CompoundVariation >> ArrayList >> Object[] >> PM}. <p> This method is reentrant.
 * @param algorithm the instrumented algorithm
 * @param collectors the collectors to be attached
 * @param visited the set of visited objects, which may include the currentobject when traversing its superclasses
 * @param parents the objects in which the current object is contained
 * @param object the current object undergoing reflection
 * @param type the superclass whose members are being reflected; or{@code null} if the base type is to be used
 */
protected void instrument(InstrumentedAlgorithm algorithm,List<Collector> collectors,Set<Object> visited,Stack<Object> parents,Object object,Class<?> type){
  if (object == null) {
    return;
  }
 else   if ((type == null) || (type.equals(object.getClass()))) {
    try {
      if (visited.contains(object)) {
        return;
      }
    }
 catch (    NullPointerException e) {
      return;
    }
    type=object.getClass();
  }
  if (type.isAnnotation() || type.isEnum() || type.isPrimitive()) {
    return;
  }
 else   if (object instanceof Instrumenter) {
    return;
  }
 else   if (type.isArray()) {
    for (int i=0; i < Array.getLength(object); i++) {
      instrument(algorithm,collectors,visited,parents,Array.get(object,i),null);
    }
  }
 else   if (object instanceof Collection) {
    for (    Object element : (Collection<?>)object) {
      instrument(algorithm,collectors,visited,parents,element,null);
    }
  }
 else   if (type.getPackage() != null) {
    if (type.getPackage().getName().startsWith("java.")) {
      return;
    }
  }
  instrument_extraction_2(algorithm,collectors,visited,parents,object,type);
}

@Override public void execute(List<ClassNode> classNodeList){
  if (console)   new PluginConsole("EZ Injection v" + version);
  if (accessModifiers)   print("Setting all of the access modifiers to public/public static.");
  if (injectHooks)   print("Injecting hook...");
  if (debugHooks)   print("Hooks are debugging.");
 else   if (injectHooks)   print("Hooks are not debugging.");
 else   print("Hooks are disabled completely.");
  if (useProxy)   print("Forcing proxy as '" + proxy + "'.");
  if (launchKit)   print("Launching the Graphicial Reflection Kit upon a succcessful invoke of the main method.");
  for (  ClassNode classNode : classNodeList) {
    for (    Object o : classNode.fields.toArray()) {
      FieldNode f=(FieldNode)o;
      execute_extraction_1(f);
    }
    for (    Object o : classNode.methods.toArray()) {
      MethodNode m=(MethodNode)o;
      execute_extraction_2(classNode,m);
    }
  }
  execute_extraction_4(classNodeList);
}

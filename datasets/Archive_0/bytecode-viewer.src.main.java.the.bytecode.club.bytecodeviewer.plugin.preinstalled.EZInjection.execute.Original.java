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
      if (accessModifiers) {
        if (f.access == Opcodes.ACC_PRIVATE || f.access == Opcodes.ACC_PROTECTED)         f.access=Opcodes.ACC_PUBLIC;
        if (f.access == Opcodes.ACC_PRIVATE + Opcodes.ACC_STATIC || f.access == Opcodes.ACC_PROTECTED + Opcodes.ACC_STATIC)         f.access=Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC;
        if (f.access == Opcodes.ACC_PRIVATE + Opcodes.ACC_FINAL || f.access == Opcodes.ACC_PROTECTED + Opcodes.ACC_FINAL)         f.access=Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL;
        if (f.access == Opcodes.ACC_PRIVATE + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC || f.access == Opcodes.ACC_PROTECTED + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC)         f.access=Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC;
      }
    }
    for (    Object o : classNode.methods.toArray()) {
      MethodNode m=(MethodNode)o;
      if (accessModifiers) {
        if (m.access == Opcodes.ACC_PRIVATE || m.access == Opcodes.ACC_PROTECTED)         m.access=Opcodes.ACC_PUBLIC;
        if (m.access == Opcodes.ACC_PRIVATE + Opcodes.ACC_STATIC || m.access == Opcodes.ACC_PROTECTED + Opcodes.ACC_STATIC)         m.access=Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC;
        if (m.access == Opcodes.ACC_PRIVATE + Opcodes.ACC_FINAL || m.access == Opcodes.ACC_PROTECTED + Opcodes.ACC_FINAL)         m.access=Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL;
        if (m.access == Opcodes.ACC_PRIVATE + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC || m.access == Opcodes.ACC_PROTECTED + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC)         m.access=Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC;
      }
      if (injectHooks && m.access != Opcodes.ACC_ABSTRACT && m.access != Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT && m.access != Opcodes.ACC_PRIVATE + Opcodes.ACC_ABSTRACT && m.access != Opcodes.ACC_PROTECTED + Opcodes.ACC_ABSTRACT && m.access != Opcodes.ACC_FINAL + Opcodes.ACC_ABSTRACT && m.access != Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_ABSTRACT && m.access != Opcodes.ACC_PRIVATE + Opcodes.ACC_FINAL + Opcodes.ACC_ABSTRACT && m.access != Opcodes.ACC_PROTECTED + Opcodes.ACC_FINAL + Opcodes.ACC_ABSTRACT && m.access != Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC+ Opcodes.ACC_ABSTRACT && m.access != Opcodes.ACC_PRIVATE + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC+ Opcodes.ACC_ABSTRACT && m.access != Opcodes.ACC_PROTECTED + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC+ Opcodes.ACC_ABSTRACT) {
        boolean inject=true;
        if (m.instructions.size() >= 2 && m.instructions.get(1) instanceof MethodInsnNode) {
          MethodInsnNode mn=(MethodInsnNode)m.instructions.get(1);
          if (mn.owner.equals(EZInjection.class.getName().replace(".","/")))           inject=false;
        }
        if (inject) {
          m.instructions.insert(new MethodInsnNode(Opcodes.INVOKESTATIC,EZInjection.class.getName().replace(".","/"),"hook","(Ljava/lang/String;)V"));
          m.instructions.insert(new LdcInsnNode(classNode.name + "." + m.name+ m.desc));
        }
      }
    }
  }
  if (useProxy) {
    try {
      String[] split=proxy.split(":");
      setProxy(split[0],split[1]);
    }
 catch (    Exception e) {
    }
  }
  print("Done setting up.");
  setFinished();
  if (invokeMethod) {
    BytecodeViewer.sm.setPrinting(true);
    for (    ClassNode cn : BytecodeViewer.getLoadedClasses())     BCV.getClassNodeLoader().addClass(cn);
    print("Attempting to find " + invokeMethodInformation + ":"+ nl+ nl);
    for (    ClassNode classNode : classNodeList) {
      for (      Object o : classNode.methods.toArray()) {
        MethodNode m=(MethodNode)o;
        String methodInformation=classNode.name + "." + m.name+ m.desc;
        if (invokeMethodInformation.equals(methodInformation)) {
          for (          Method m2 : BCV.getClassNodeLoader().nodeToClass(classNode).getMethods()) {
            if (m2.getName().equals(m.name)) {
              print("Invoking " + invokeMethodInformation + ":"+ nl+ nl);
              GraphicalReflectionKit kit=launchKit ? new GraphicalReflectionKit() : null;
              try {
                if (kit != null)                 kit.setVisible(true);
                m2.invoke(classNode.getClass().newInstance(),(Object[])new String[1]);
                print("Finished running " + invokeMethodInformation);
              }
 catch (              Exception e) {
                StringWriter sw=new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                e.printStackTrace();
                print(sw.toString());
              }
 finally {
                BytecodeViewer.sm.setPrinting(false);
                if (kit != null)                 kit.setVisible(false);
              }
            }
          }
        }
      }
    }
  }
}

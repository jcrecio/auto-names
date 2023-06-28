/** 
 * @author DeathMarine
 */
private void doSaveJarDecompiled(File inFile,File outFile) throws Exception {
  try (JarFile jfile=new JarFile(inFile);FileOutputStream dest=new FileOutputStream(outFile);BufferedOutputStream buffDest=new BufferedOutputStream(dest);ZipOutputStream out=new ZipOutputStream(buffDest)){
    byte[] data=new byte[1024];
    DecompilerSettings settings=getDecompilerSettings();
    LuytenTypeLoader typeLoader=new LuytenTypeLoader();
    MetadataSystem metadataSystem=new MetadataSystem(typeLoader);
    ITypeLoader jarLoader=new JarTypeLoader(jfile);
    typeLoader.getTypeLoaders().add(jarLoader);
    DecompilationOptions decompilationOptions=new DecompilationOptions();
    decompilationOptions.setSettings(settings);
    decompilationOptions.setFullDecompilation(true);
    Enumeration<JarEntry> ent=jfile.entries();
    Set<JarEntry> history=new HashSet<>();
    while (ent.hasMoreElements()) {
      JarEntry entry=ent.nextElement();
      if (entry.getName().endsWith(".class")) {
        JarEntry etn=new JarEntry(entry.getName().replace(".class",".java"));
        if (history.add(etn)) {
          out.putNextEntry(etn);
          try {
            String internalName=StringUtilities.removeRight(entry.getName(),".class");
            TypeReference type=metadataSystem.lookupType(internalName);
            TypeDefinition resolvedType;
            if ((type == null) || ((resolvedType=type.resolve()) == null)) {
              throw new Exception("Unable to resolve type.");
            }
            try (Writer writer=new OutputStreamWriter(out)){
              settings.getLanguage().decompileType(resolvedType,new PlainTextOutput(writer),decompilationOptions);
              writer.flush();
            }
           }
  finally {
            out.closeEntry();
          }
        }
      }
 else {
        try {
          JarEntry etn=new JarEntry(entry.getName());
          if (history.add(etn))           continue;
          doSaveJarDecompiled_extraction_2(jfile,out,data,history,entry,etn);
        }
 catch (        ZipException ze) {
          if (!ze.getMessage().contains("duplicate")) {
            throw ze;
          }
        }
      }
    }
  }
 }

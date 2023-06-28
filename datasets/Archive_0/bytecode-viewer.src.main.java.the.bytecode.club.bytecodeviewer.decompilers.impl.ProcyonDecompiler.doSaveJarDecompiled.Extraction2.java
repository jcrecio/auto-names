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
      doSaveJarDecompiled_extraction_1(jfile,out,data,settings,metadataSystem,decompilationOptions,history,entry);
    }
  }
 }

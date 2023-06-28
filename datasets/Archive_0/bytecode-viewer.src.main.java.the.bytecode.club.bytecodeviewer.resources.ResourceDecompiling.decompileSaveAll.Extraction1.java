public static void decompileSaveAll(){
  if (BytecodeViewer.promptIfNoLoadedClasses())   return;
  Thread decompileThread=new Thread(() -> {
    if (!BytecodeViewer.autoCompileSuccessful())     return;
    JFileChooser fc=new FileChooser(Configuration.getLastSaveDirectory(),"Select Zip Export","Zip Archives","zip");
    int returnVal=fc.showSaveDialog(BytecodeViewer.viewer);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      Configuration.setLastSaveDirectory(fc.getSelectedFile());
      File file=fc.getSelectedFile();
      if (!file.getAbsolutePath().endsWith(".zip"))       file=new File(file.getAbsolutePath() + ".zip");
      if (!DialogUtils.canOverwriteFile(file))       return;
      final File javaSucks=file;
      final String path=MiscUtils.append(file,".zip");
      JOptionPane pane=new JOptionPane("What decompiler will you use?");
      Object[] options=new String[]{"All","Procyon","CFR","Fernflower","Krakatau","Cancel"};
      pane.setOptions(options);
      JDialog dialog=pane.createDialog(BytecodeViewer.viewer,"Bytecode Viewer - Select Decompiler");
      dialog.setVisible(true);
      Object obj=pane.getValue();
      int result=-1;
      for (int k=0; k < options.length; k++)       if (options[k].equals(obj))       result=k;
      BytecodeViewer.updateBusyStatus(true);
      File tempZip=new File(tempDirectory + fs + "temp_"+ MiscUtils.getRandomizedName()+ ".jar");
      if (tempZip.exists())       tempZip.delete();
      JarUtils.saveAsJarClassesOnly(BytecodeViewer.getLoadedClasses(),tempZip.getAbsolutePath());
      if (result == 0) {
        Thread t12=new Thread(() -> {
          try {
            Decompiler.PROCYON_DECOMPILER.getDecompiler().decompileToZip(tempZip.getAbsolutePath(),MiscUtils.append(javaSucks,"-procyon.zip"));
            BytecodeViewer.updateBusyStatus(false);
          }
 catch (          Exception e) {
            BytecodeViewer.handleException(e);
          }
        }
);
        t12.start();
        Thread t2=new Thread(() -> {
          try {
            BytecodeViewer.updateBusyStatus(true);
            Decompiler.CFR_DECOMPILER.getDecompiler().decompileToZip(tempZip.getAbsolutePath(),MiscUtils.append(javaSucks,"-CFR.zip"));
            BytecodeViewer.updateBusyStatus(false);
          }
 catch (          Exception e) {
            BytecodeViewer.handleException(e);
          }
        }
);
        t2.start();
        Thread t3=new Thread(() -> {
          try {
            BytecodeViewer.updateBusyStatus(true);
            Decompiler.FERNFLOWER_DECOMPILER.getDecompiler().decompileToZip(tempZip.getAbsolutePath(),MiscUtils.append(javaSucks,"-fernflower.zip"));
            BytecodeViewer.updateBusyStatus(false);
          }
 catch (          Exception e) {
            BytecodeViewer.handleException(e);
          }
        }
);
        t3.start();
        Thread t4=new Thread(() -> {
          try {
            BytecodeViewer.updateBusyStatus(true);
            Decompiler.KRAKATAU_DECOMPILER.getDecompiler().decompileToZip(tempZip.getAbsolutePath(),MiscUtils.append(javaSucks,"-kraktau.zip"));
            BytecodeViewer.updateBusyStatus(false);
          }
 catch (          Exception e) {
            BytecodeViewer.handleException(e);
          }
        }
);
        t4.start();
      }
      if (result == 1) {
        Thread t12=new Thread(() -> {
          try {
            Decompiler.PROCYON_DECOMPILER.getDecompiler().decompileToZip(tempZip.getAbsolutePath(),path);
            BytecodeViewer.updateBusyStatus(false);
          }
 catch (          Exception e) {
            BytecodeViewer.handleException(e);
          }
        }
);
        t12.start();
      }
      if (result == 2) {
        Thread t12=new Thread(() -> {
          try {
            Decompiler.CFR_DECOMPILER.getDecompiler().decompileToZip(tempZip.getAbsolutePath(),path);
            BytecodeViewer.updateBusyStatus(false);
          }
 catch (          Exception e) {
            BytecodeViewer.handleException(e);
          }
        }
);
        t12.start();
      }
      decompileSaveAll_extraction_4(path,result,tempZip);
    }
  }
,"Decompile Thread");
  decompileThread.start();
}

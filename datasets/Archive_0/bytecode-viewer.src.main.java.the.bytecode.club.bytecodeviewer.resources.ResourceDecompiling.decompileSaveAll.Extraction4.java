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
      decompileSaveAll_extraction_1(file);
    }
  }
,"Decompile Thread");
  decompileThread.start();
}

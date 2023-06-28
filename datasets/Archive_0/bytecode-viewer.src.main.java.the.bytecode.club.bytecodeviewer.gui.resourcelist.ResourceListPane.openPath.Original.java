public void openPath(TreePath path){
  if (path == null || path.getPathCount() == 1)   return;
  final StringBuilder nameBuffer=new StringBuilder();
  for (int i=2; i < path.getPathCount(); i++) {
    nameBuffer.append(path.getPathComponent(i));
    if (i < path.getPathCount() - 1)     nameBuffer.append("/");
  }
  String cheapHax=path.getPathComponent(1).toString();
  ResourceContainer container=null;
  for (  ResourceContainer c : BytecodeViewer.resourceContainers.values()) {
    if (c.name.equals(cheapHax))     container=c;
  }
  String name=nameBuffer.toString();
  boolean resourceMode=false;
  byte[] content=container.resourceClassBytes.get(name);
  if (content == null) {
    content=container.resourceFiles.get(name);
    resourceMode=true;
  }
  if (content != null && MiscUtils.getFileHeaderMagicNumber(content).equalsIgnoreCase("cafebabe") || name.endsWith(".class")) {
    try {
      if (resourceMode) {
      }
      BytecodeViewer.viewer.workPane.addClassResource(container,name);
    }
 catch (    Exception e) {
      e.printStackTrace();
      BytecodeViewer.viewer.workPane.addFileResource(container,name);
    }
  }
 else   if (container.resourceFiles.containsKey(name)) {
    final String fn=name.toLowerCase();
    final String extension=fn.contains(":") ? null : FilenameUtils.getExtension(fn);
    Import imp=Import.extensionMap.get(extension);
    if (imp == null)     BytecodeViewer.viewer.workPane.addFileResource(container,name);
 else {
      int hash=(container.name + name).hashCode();
      File tempFile=new File(tempDirectory + fs + hash+ fs+ name+ "."+ extension);
      if (!tempFile.exists()) {
        DiskWriter.replaceFileBytes(tempFile.getAbsolutePath(),content,false);
        try {
          imp.getImporter().open(tempFile);
        }
 catch (        Exception e) {
          e.printStackTrace();
          BytecodeViewer.viewer.workPane.addFileResource(container,name);
        }
      }
 else {
      }
    }
  }
}

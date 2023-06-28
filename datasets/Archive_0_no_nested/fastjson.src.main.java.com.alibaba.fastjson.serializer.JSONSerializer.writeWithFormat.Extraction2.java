public final void writeWithFormat(Object object,String format){
  if (object instanceof Date) {
    writeWithFormat_extraction_1(object,format);
  }
  if (object instanceof byte[]) {
    byte[] bytes=(byte[])object;
    if ("gzip".equals(format) || "gzip,base64".equals(format)) {
      GZIPOutputStream gzipOut=null;
      try {
        ByteArrayOutputStream byteOut=new ByteArrayOutputStream();
        if (bytes.length < 512) {
          gzipOut=new GZIPOutputStream(byteOut,bytes.length);
        }
 else {
          gzipOut=new GZIPOutputStream(byteOut);
        }
        gzipOut.write(bytes);
        gzipOut.finish();
        out.writeByteArray(byteOut.toByteArray());
      }
 catch (      IOException ex) {
        throw new JSONException("write gzipBytes error",ex);
      }
 finally {
        IOUtils.close(gzipOut);
      }
    }
 else     if ("hex".equals(format)) {
      out.writeHex(bytes);
    }
 else {
      out.writeByteArray(bytes);
    }
    return;
  }
  writeWithFormat_extraction_2(object,format);
}

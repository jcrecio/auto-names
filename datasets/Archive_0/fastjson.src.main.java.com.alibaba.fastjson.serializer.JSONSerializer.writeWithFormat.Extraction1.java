public final void writeWithFormat(Object object,String format){
  if (object instanceof Date) {
    if ("unixtime".equals(format)) {
      long seconds=((Date)object).getTime() / 1000L;
      out.writeInt((int)seconds);
      return;
    }
    if ("millis".equals(format)) {
      out.writeLong(((Date)object).getTime());
      return;
    }
    DateFormat dateFormat=this.getDateFormat();
    if (dateFormat == null) {
      if (format != null) {
        try {
          dateFormat=this.generateDateFormat(format);
        }
 catch (        IllegalArgumentException e) {
          String format2=format.replaceAll("T","'T'");
          dateFormat=this.generateDateFormat(format2);
        }
      }
 else       if (fastJsonConfigDateFormatPattern != null) {
        dateFormat=this.generateDateFormat(fastJsonConfigDateFormatPattern);
      }
 else {
        dateFormat=this.generateDateFormat(JSON.DEFFAULT_DATE_FORMAT);
      }
    }
    String text=dateFormat.format((Date)object);
    out.writeString(text);
    return;
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

public final void scanNumber(){
  np=bp;
  if (ch == '-') {
    sp++;
    next();
  }
  for (; ; ) {
    if (ch >= '0' && ch <= '9') {
      sp++;
    }
 else {
      break;
    }
    next();
  }
  boolean isDouble=false;
  if (ch == '.') {
    sp++;
    next();
    isDouble=true;
    for (; ; ) {
      if (ch >= '0' && ch <= '9') {
        sp++;
      }
 else {
        break;
      }
      next();
    }
  }
  if (sp > 65535) {
    throw new JSONException("scanNumber overflow");
  }
  scanNumber_extraction_1(isDouble);
}

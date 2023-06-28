protected void writeStringWithSingleQuote(String text){
  if (text == null) {
    int newcount=count + 4;
    if (newcount > buf.length) {
      expandCapacity(newcount);
    }
    "null".getChars(0,4,buf,count);
    count=newcount;
    return;
  }
  writeStringWithSingleQuote_extraction_1(text);
}

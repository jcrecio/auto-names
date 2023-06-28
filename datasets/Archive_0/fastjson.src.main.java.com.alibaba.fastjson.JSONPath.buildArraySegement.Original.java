Segment buildArraySegement(String indexText){
  final int indexTextLen=indexText.length();
  final char firstChar=indexText.charAt(0);
  final char lastChar=indexText.charAt(indexTextLen - 1);
  int commaIndex=indexText.indexOf(',');
  if (indexText.length() > 2 && firstChar == '\'' && lastChar == '\'') {
    String propertyName=indexText.substring(1,indexTextLen - 1);
    if (commaIndex == -1 || !strArrayPatternx.matcher(indexText).find()) {
      return new PropertySegment(propertyName,false);
    }
    String[] propertyNames=propertyName.split(strArrayRegex);
    return new MultiPropertySegment(propertyNames);
  }
  int colonIndex=indexText.indexOf(':');
  if (commaIndex == -1 && colonIndex == -1) {
    if (TypeUtils.isNumber(indexText)) {
      try {
        int index=Integer.parseInt(indexText);
        return new ArrayAccessSegment(index);
      }
 catch (      NumberFormatException ex) {
        return new PropertySegment(indexText,false);
      }
    }
 else {
      if (indexText.charAt(0) == '"' && indexText.charAt(indexText.length() - 1) == '"') {
        indexText=indexText.substring(1,indexText.length() - 1);
      }
      return new PropertySegment(indexText,false);
    }
  }
  if (commaIndex != -1) {
    String[] indexesText=indexText.split(",");
    int[] indexes=new int[indexesText.length];
    for (int i=0; i < indexesText.length; ++i) {
      indexes[i]=Integer.parseInt(indexesText[i]);
    }
    return new MultiIndexSegment(indexes);
  }
  if (colonIndex != -1) {
    String[] indexesText=indexText.split(":");
    int[] indexes=new int[indexesText.length];
    for (int i=0; i < indexesText.length; ++i) {
      String str=indexesText[i];
      if (str.length() == 0) {
        if (i == 0) {
          indexes[i]=0;
        }
 else {
          throw new UnsupportedOperationException();
        }
      }
 else {
        indexes[i]=Integer.parseInt(str);
      }
    }
    int start=indexes[0];
    int end;
    if (indexes.length > 1) {
      end=indexes[1];
    }
 else {
      end=-1;
    }
    int step;
    if (indexes.length == 3) {
      step=indexes[2];
    }
 else {
      step=1;
    }
    if (end >= 0 && end < start) {
      throw new UnsupportedOperationException("end must greater than or equals start. start " + start + ",  end "+ end);
    }
    if (step <= 0) {
      throw new UnsupportedOperationException("step must greater than zero : " + step);
    }
    return new RangeSegment(start,end,step);
  }
  throw new UnsupportedOperationException();
}

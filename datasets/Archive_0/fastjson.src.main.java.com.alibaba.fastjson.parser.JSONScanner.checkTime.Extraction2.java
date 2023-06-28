private boolean checkTime(char h0,char h1,char m0,char m1,char s0,char s1){
  if (h0 == '0') {
    if (h1 < '0' || h1 > '9') {
      return false;
    }
  }
 else   if (h0 == '1') {
    if (h1 < '0' || h1 > '9') {
      return false;
    }
  }
 else   if (h0 == '2') {
    if (h1 < '0' || h1 > '4') {
      return false;
    }
  }
 else {
    return false;
  }
  return checkTime_extraction_1(m0,m1,s0,s1);
}

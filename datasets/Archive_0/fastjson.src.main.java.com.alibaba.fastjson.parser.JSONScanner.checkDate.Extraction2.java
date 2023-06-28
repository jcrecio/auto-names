static boolean checkDate(char y0,char y1,char y2,char y3,char M0,char M1,int d0,int d1){
  if (y0 < '0' || y0 > '9') {
    return false;
  }
  if (y1 < '0' || y1 > '9') {
    return false;
  }
  if (y2 < '0' || y2 > '9') {
    return false;
  }
  if (y3 < '0' || y3 > '9') {
    return false;
  }
  return checkDate_extraction_1(M0,M1,d0,d1);
}

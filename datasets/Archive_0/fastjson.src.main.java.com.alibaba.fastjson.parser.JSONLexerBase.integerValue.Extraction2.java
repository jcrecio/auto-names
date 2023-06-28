public final Number integerValue() throws NumberFormatException {
  long result=0;
  boolean negative=false;
  if (np == -1) {
    np=0;
  }
  int i=np, max=np + sp;
  long limit;
  long multmin;
  int digit;
  char type=' ';
switch (charAt(max - 1)) {
case 'L':    max--;
  type='L';
break;
case 'S':max--;
type='S';
break;
case 'B':max--;
type='B';
break;
default:break;
}
if (charAt(np) == '-') {
negative=true;
limit=Long.MIN_VALUE;
i++;
}
 else {
limit=-Long.MAX_VALUE;
}
multmin=MULTMIN_RADIX_TEN;
if (i < max) {
digit=charAt(i++) - '0';
result=-digit;
}
while (i < max) {
digit=charAt(i++) - '0';
if (result < multmin) {
return new BigInteger(numberString(),10);
}
result*=10;
if (result < limit + digit) {
return new BigInteger(numberString(),10);
}
result-=digit;
}
return integerValue_extraction_1(result,negative,i,type);
}

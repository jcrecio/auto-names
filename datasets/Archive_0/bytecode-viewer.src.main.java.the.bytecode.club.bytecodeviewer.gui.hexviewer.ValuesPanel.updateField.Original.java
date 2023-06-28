private void updateField(ValuesPanelField valuesPanelField){
switch (valuesPanelField) {
case BINARY0:{
      binaryCheckBox0.setSelected((values[0] & 0x80) > 0);
      break;
    }
case BINARY1:{
    binaryCheckBox1.setSelected((values[0] & 0x40) > 0);
    break;
  }
case BINARY2:{
  binaryCheckBox2.setSelected((values[0] & 0x20) > 0);
  break;
}
case BINARY3:{
binaryCheckBox3.setSelected((values[0] & 0x10) > 0);
break;
}
case BINARY4:{
binaryCheckBox4.setSelected((values[0] & 0x8) > 0);
break;
}
case BINARY5:{
binaryCheckBox5.setSelected((values[0] & 0x4) > 0);
break;
}
case BINARY6:{
binaryCheckBox6.setSelected((values[0] & 0x2) > 0);
break;
}
case BINARY7:{
binaryCheckBox7.setSelected((values[0] & 0x1) > 0);
break;
}
case BYTE:{
byteTextField.setText(String.valueOf(signed ? values[0] : values[0] & 0xff));
break;
}
case WORD:{
int wordValue=signed ? (byteOrder == ByteOrder.LITTLE_ENDIAN ? (values[0] & 0xff) | (values[1] << 8) : (values[1] & 0xff) | (values[0] << 8)) : (byteOrder == ByteOrder.LITTLE_ENDIAN ? (values[0] & 0xff) | ((values[1] & 0xff) << 8) : (values[1] & 0xff) | ((values[0] & 0xff) << 8));
wordTextField.setText(String.valueOf(wordValue));
break;
}
case INTEGER:{
long intValue=signed ? (byteOrder == ByteOrder.LITTLE_ENDIAN ? (values[0] & 0xffl) | ((values[1] & 0xffl) << 8) | ((values[2] & 0xffl) << 16)| (values[3] << 24) : (values[3] & 0xffl) | ((values[2] & 0xffl) << 8) | ((values[1] & 0xffl) << 16)| (values[0] << 24)) : (byteOrder == ByteOrder.LITTLE_ENDIAN ? (values[0] & 0xffl) | ((values[1] & 0xffl) << 8) | ((values[2] & 0xffl) << 16)| ((values[3] & 0xffl) << 24) : (values[3] & 0xffl) | ((values[2] & 0xffl) << 8) | ((values[1] & 0xffl) << 16)| ((values[0] & 0xffl) << 24));
intTextField.setText(String.valueOf(intValue));
break;
}
case LONG:{
if (signed) {
byteBuffer.rewind();
if (byteBuffer.order() != byteOrder) {
byteBuffer.order(byteOrder);
}
longTextField.setText(String.valueOf(byteBuffer.getLong()));
}
 else {
long longValue=byteOrder == ByteOrder.LITTLE_ENDIAN ? (values[0] & 0xffl) | ((values[1] & 0xffl) << 8) | ((values[2] & 0xffl) << 16)| ((values[3] & 0xffl) << 24)| ((values[4] & 0xffl) << 32)| ((values[5] & 0xffl) << 40)| ((values[6] & 0xffl) << 48) : (values[7] & 0xffl) | ((values[6] & 0xffl) << 8) | ((values[5] & 0xffl) << 16)| ((values[4] & 0xffl) << 24)| ((values[3] & 0xffl) << 32)| ((values[2] & 0xffl) << 40)| ((values[1] & 0xffl) << 48);
BigInteger bigInt1=BigInteger.valueOf(values[byteOrder == ByteOrder.LITTLE_ENDIAN ? 7 : 0] & 0xffl);
BigInteger bigInt2=bigInt1.shiftLeft(56);
BigInteger bigInt3=bigInt2.add(BigInteger.valueOf(longValue));
longTextField.setText(bigInt3.toString());
}
break;
}
case FLOAT:{
byteBuffer.rewind();
if (byteBuffer.order() != byteOrder) {
byteBuffer.order(byteOrder);
}
floatTextField.setText(String.valueOf(byteBuffer.getFloat()));
break;
}
case DOUBLE:{
byteBuffer.rewind();
if (byteBuffer.order() != byteOrder) {
byteBuffer.order(byteOrder);
}
doubleTextField.setText(String.valueOf(byteBuffer.getDouble()));
break;
}
case CHARACTER:{
String strValue=new String(values,codeArea.getCharset());
if (strValue.length() > 0) {
characterTextField.setText(strValue.substring(0,1));
}
 else {
characterTextField.setText("");
}
break;
}
case STRING:{
String strValue=new String(values,codeArea.getCharset());
for (int i=0; i < strValue.length(); i++) {
char charAt=strValue.charAt(i);
if (charAt == '\r' || charAt == '\n' || charAt == 0) {
strValue=strValue.substring(0,i);
break;
}
}
stringTextField.setText(strValue);
stringTextField.setCaretPosition(0);
break;
}
}
}

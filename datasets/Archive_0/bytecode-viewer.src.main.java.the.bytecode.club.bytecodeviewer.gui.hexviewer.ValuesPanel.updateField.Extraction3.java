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
updateField_extraction_2();
break;
}
case LONG:{
updateField_extraction_3();
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
String strValue=updateField_extraction_4();
stringTextField.setText(strValue);
stringTextField.setCaretPosition(0);
break;
}
}
}

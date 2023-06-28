public String translate(String propertyName){
switch (this) {
case SnakeCase:{
      StringBuilder buf=translate_extraction_1(propertyName);
      return buf.toString();
    }
case KebabCase:{
    StringBuilder buf=translate_extraction_2(propertyName);
    return buf.toString();
  }
case PascalCase:{
  char ch=propertyName.charAt(0);
  if (ch >= 'a' && ch <= 'z') {
    char[] chars=propertyName.toCharArray();
    chars[0]-=32;
    return new String(chars);
  }
  return propertyName;
}
case CamelCase:{
char ch=propertyName.charAt(0);
if (ch >= 'A' && ch <= 'Z') {
  char[] chars=propertyName.toCharArray();
  chars[0]+=32;
  return new String(chars);
}
return propertyName;
}
case NoChange:case NeverUseThisValueExceptDefaultValue:default:return propertyName;
}
}

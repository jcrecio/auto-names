private JSONObject decodeRequestParameters(JSONObject requestValParams) throws JSONException, IOException {
  JSONObject toReturn=new JSONObject();
  Iterator keys=requestValParams.keys();
  while (keys.hasNext()) {
    String key=(String)keys.next();
    Object valueObj=requestValParams.get(key);
    if (valueObj instanceof Number) {
      String value=String.valueOf(valueObj);
      if (!value.equals("") && !value.equalsIgnoreCase("%")) {
        toReturn.put(key,URLDecoder.decode(value.replaceAll("%","%25").replace("+","%2B"),"UTF-8"));
      }
 else {
        toReturn.put(key,value);
      }
    }
 else     if (valueObj instanceof String) {
      String value=String.valueOf(valueObj);
      if (!value.equals("") && !value.equalsIgnoreCase("%")) {
        toReturn.put(key,URLDecoder.decode(value.replaceAll("%","%25").replace("+","%2B"),"UTF-8"));
      }
 else {
        toReturn.put(key,value);
      }
    }
 else     if (valueObj instanceof JSONArray) {
      decodeRequestParameters_extraction_1(toReturn,key,valueObj);
    }
 else     decodeRequestParameters_extraction_2(toReturn,key,valueObj);
  }
  return toReturn;
}

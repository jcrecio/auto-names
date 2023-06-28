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
      JSONArray valuesLst=(JSONArray)valueObj;
      JSONArray ValuesLstDecoded=new JSONArray();
      for (int v=0; v < valuesLst.length(); v++) {
        String value=(valuesLst.get(v) != null) ? String.valueOf(valuesLst.get(v)) : "";
        if (!value.equals("") && !value.equalsIgnoreCase("%")) {
          ValuesLstDecoded.put(URLDecoder.decode(value.replaceAll("%","%25").replace("+","%2B"),"UTF-8"));
        }
 else {
          ValuesLstDecoded.put(value);
          URLDecoder.decode(value.replaceAll("%","%25").replace("+","%2B"),"UTF-8");
        }
      }
      toReturn.put(key,ValuesLstDecoded);
    }
 else     if (valueObj instanceof JSONObject) {
      JSONObject valuesLst=(JSONObject)valueObj;
      JSONArray ValuesLstDecoded=new JSONArray();
      Iterator keysObject=valuesLst.keys();
      while (keysObject.hasNext()) {
        String keyObj=(String)keysObject.next();
        Object valueOb=valuesLst.get(keyObj);
        String value=String.valueOf(valueOb);
        if (!value.equals("") && !value.equalsIgnoreCase("%")) {
          ValuesLstDecoded.put(URLDecoder.decode(value.replaceAll("%","%25").replace("+","%2B"),"UTF-8"));
        }
 else {
          ValuesLstDecoded.put(value);
        }
      }
      toReturn.put(key,ValuesLstDecoded);
    }
  }
  return toReturn;
}

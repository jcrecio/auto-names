@Override public JSONObject convert(JSONArray widgetFilters){
  ;
  JSONObject selections=new JSONObject();
  JSONObject datasetFilters=new JSONObject();
  if (filters != null) {
    JSONObject obj=filters.optJSONObject(dataSetLabel);
    if (obj != null) {
      String[] names=JSONObject.getNames(obj);
      if (names != null) {
        for (int i=0; i < names.length; i++) {
          String filter=names[i];
          JSONArray array=new JSONArray();
          convert_extraction_1(datasetFilters,obj,filter,array);
        }
      }
    }
  }
  if (widgetFilters != null) {
    for (int i=0; i < widgetFilters.length(); i++) {
      JSONObject widgetFilter;
      convert_extraction_2(widgetFilters,datasetFilters,i);
    }
  }
  try {
    selections.put(dataSetLabel,datasetFilters);
  }
 catch (  JSONException e) {
  }
  return selections;
}

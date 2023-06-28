private ValidationErrors validateDataset(IDataStore dataStore,JSONArray columns,String dsConfiguration) throws JSONException {
  ValidationErrors validationErrors=new ValidationErrors();
  long records=dataStore.getRecordsCount();
  for (int i=0; i < columns.length(); i++) {
    int index=(int)Math.round(Math.floor(i / 2));
    JSONObject jo=new JSONObject();
    for (int j=0; j < records; j++) {
      jo=(JSONObject)columns.get(i);
      String pvalue=jo.opt("pvalue").toString().toUpperCase();
      if (!pvalue.equals("MEASURE") && !pvalue.equals("ATTRIBUTE")) {
        Object obj=dataStore.getRecordAt(j).getFieldAt(index).getValue();
        validateDataset_extraction_1(dataStore,dsConfiguration,validationErrors,i,index,j,pvalue,obj);
      }
    }
  }
  return validationErrors;
}

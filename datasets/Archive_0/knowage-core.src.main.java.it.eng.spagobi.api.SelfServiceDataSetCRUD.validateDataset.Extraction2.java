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
switch (pvalue) {
case "DOUBLE":          try {
            if (obj != null && !(obj instanceof BigDecimal) && !(obj instanceof Double) && !(obj instanceof Float)) {
              Double.parseDouble(obj.toString());
            }
          }
 catch (          NumberFormatException nfe) {
            logger.error("The cell cannot be formatted as a Double value",nfe);
            validationErrors.addError(j,i,dataStore.getRecordAt(j).getFieldAt(index),"sbi.workspace.dataset.wizard.metadata.validation.error.double.title");
          }
        break;
case "LONG":      try {
        if (obj != null && !(obj instanceof Integer) && !(obj instanceof Long)) {
          Long.parseLong(obj.toString());
        }
      }
 catch (      NumberFormatException nfe) {
        logger.error("The cell cannot be formatted as an Long value",nfe);
        validationErrors.addError(j,i,dataStore.getRecordAt(j).getFieldAt(index),"sbi.workspace.dataset.wizard.metadata.validation.error.long.title");
      }
    break;
case "INTEGER":  try {
    if (obj != null && !(obj instanceof Integer) && !(obj instanceof Long)) {
      Integer.parseInt(obj.toString());
    }
  }
 catch (  NumberFormatException nfe) {
    logger.error("The cell cannot be formatted as an Integer value",nfe);
    validationErrors.addError(j,i,dataStore.getRecordAt(j).getFieldAt(index),"sbi.workspace.dataset.wizard.metadata.validation.error.integer.title");
  }
break;
case "DATE":validateDataset_extraction_3(dataStore,dsConfiguration,validationErrors,i,index,j,obj);
break;
case "TIMESTAMP":try {
validateDataset_extraction_4(dsConfiguration,obj);
}
 catch (Exception e) {
logger.error("The cell cannot be formatted as Timestamp value",e);
validationErrors.addError(j,i,dataStore.getRecordAt(j).getFieldAt(index),"sbi.workspace.dataset.wizard.metadata.validation.error.timestamp.title");
}
break;
}
}
}
}
return validationErrors;
}

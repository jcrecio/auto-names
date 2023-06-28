public SimpleFilter getFilter(String operatorString,JSONArray valuesJsonArray,String columns,IDataSet dataSet,Map<String,String> columnAliasToColumnName) throws JSONException {
  SimpleFilter filter=null;
  if (operatorString != null) {
    SimpleFilterOperator operator=SimpleFilterOperator.ofSymbol(operatorString.toUpperCase());
    if (valuesJsonArray.length() > 0 || operator.isNullary() || operator.isPlaceholder()) {
      filter=getFilter_extraction_1(valuesJsonArray,columns,dataSet,columnAliasToColumnName,operator);
    }
  }
  return filter;
}

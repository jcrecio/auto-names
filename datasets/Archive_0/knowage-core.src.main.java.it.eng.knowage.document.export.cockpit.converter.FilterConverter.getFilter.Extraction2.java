public SimpleFilter getFilter(String operatorString,JSONArray valuesJsonArray,String columns,IDataSet dataSet,Map<String,String> columnAliasToColumnName) throws JSONException {
  SimpleFilter filter=null;
  if (operatorString != null) {
    SimpleFilterOperator operator=SimpleFilterOperator.ofSymbol(operatorString.toUpperCase());
    if (valuesJsonArray.length() > 0 || operator.isNullary() || operator.isPlaceholder()) {
      List<String> columnsList=getColumnList(columns,dataSet,columnAliasToColumnName);
      List<Projection> projections=new ArrayList<>(columnsList.size());
      for (      String columnName : columnsList) {
        projections.add(new Projection(dataSet,columnName));
      }
      List<Object> valueObjects=new ArrayList<>(0);
      filter=getFilter_extraction_1(valuesJsonArray,operator,projections,valueObjects);
    }
  }
  return filter;
}

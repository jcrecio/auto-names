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
      if (!operator.isNullary() && !operator.isPlaceholder()) {
        for (int i=0; i < valuesJsonArray.length(); i++) {
          String[] valuesArray=StringUtilities.splitBetween(valuesJsonArray.getString(i),"'","','","'");
          for (int j=0; j < valuesArray.length; j++) {
            Projection projection=projections.get(j % projections.size());
            valueObjects.add(DataSetUtilities.getValue(valuesArray[j],projection.getType()));
          }
        }
      }
      filter=getFilter_extraction_2(operator,projections,valueObjects);
    }
  }
  return filter;
}

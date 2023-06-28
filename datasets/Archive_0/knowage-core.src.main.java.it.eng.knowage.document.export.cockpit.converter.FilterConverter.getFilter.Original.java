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
      if (operator.isPlaceholder()) {
        filter=new PlaceholderFilter(projections.get(0),operator);
      }
 else {
        if (SimpleFilterOperator.IN.equals(operator)) {
          if (valueObjects.isEmpty()) {
            filter=new NullaryFilter(projections.get(0),SimpleFilterOperator.IS_NULL);
          }
 else {
            filter=new InFilter(projections,valueObjects);
          }
        }
 else         if (SimpleFilterOperator.LIKE.equals(operator)) {
          filter=new LikeFilter(projections.get(0),valueObjects.get(0).toString(),LikeFilter.TYPE.PATTERN);
        }
 else         if (SimpleFilterOperator.BETWEEN.equals(operator)) {
          filter=new BetweenFilter(projections.get(0),valueObjects.get(0),valueObjects.get(1));
        }
 else         if (operator.isNullary()) {
          filter=new NullaryFilter(projections.get(0),operator);
        }
 else {
          filter=new UnaryFilter(projections.get(0),operator,valueObjects.get(0));
        }
      }
    }
  }
  return filter;
}

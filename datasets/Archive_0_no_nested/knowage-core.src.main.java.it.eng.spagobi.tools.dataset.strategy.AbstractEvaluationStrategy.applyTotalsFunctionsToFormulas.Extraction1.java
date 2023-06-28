private List<AbstractSelectionField> applyTotalsFunctionsToFormulas(IDataSet dataSet,List<AbstractSelectionField> projections,Filter filter,int maxRowCount){
  List<AbstractSelectionField> toReturnList=new ArrayList<AbstractSelectionField>();
  Set<String> totalFunctions=new HashSet<String>();
  for (  AbstractSelectionField abstractSelectionField : projections) {
    if (abstractSelectionField instanceof DataStoreCalculatedField) {
      String formula=((DataStoreCalculatedField)abstractSelectionField).getFormula();
      if (formula.contains("TOTAL_")) {
        String pattern="((?:TOTAL_SUM|TOTAL_AVG|TOTAL_MIN|TOTAL_MAX|TOTAL_COUNT)\\()(\\\"[a-zA-Z0-9\\-\\_\\s]*\\\")(\\))";
        Pattern r=Pattern.compile(pattern);
        Matcher m=r.matcher(formula);
        while (m.find()) {
          totalFunctions.add(m.group(0).replace("TOTAL_",""));
        }
        pattern="((?:TOTAL_SUM|TOTAL_AVG|TOTAL_MIN|TOTAL_MAX|TOTAL_COUNT)\\()([a-zA-Z0-9\\-\\+\\/\\*\\_\\s\\$\\{\\}\\\"]*)(\\))";
        r=Pattern.compile(Pattern.quote(pattern));
        m=r.matcher(formula);
        while (m.find()) {
          totalFunctions.add(m.group(0).replace("TOTAL_",""));
        }
      }
    }
  }
  if (!totalFunctions.isEmpty()) {
    IDataStore totalsFunctionDataStore=executeTotalsFunctions(dataSet,totalFunctions,filter,maxRowCount);
    HashMap<String,String> totalsMap=new HashMap<String,String>();
    int i=0;
    for (    String function : totalFunctions) {
      totalsMap.put(function,String.valueOf(totalsFunctionDataStore.getRecordAt(0).getFieldAt(i).getValue()));
      i++;
    }
    for (    AbstractSelectionField abstractSelectionField : projections) {
      AbstractSelectionField tmp=abstractSelectionField;
      if (tmp instanceof DataStoreCalculatedField) {
        String formula=((DataStoreCalculatedField)tmp).getFormula();
        if (formula.contains("TOTAL_")) {
          formula=applyTotalsFunctionsToFormulas_extraction_2(totalsMap,formula);
          ((DataStoreCalculatedField)tmp).setFormula(formula);
        }
      }
      toReturnList.add(tmp);
    }
  }
 else {
    toReturnList=projections;
  }
  return toReturnList;
}

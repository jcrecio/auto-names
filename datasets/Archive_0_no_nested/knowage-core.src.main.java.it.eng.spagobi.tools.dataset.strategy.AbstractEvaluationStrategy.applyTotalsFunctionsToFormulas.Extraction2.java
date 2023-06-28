private List<AbstractSelectionField> applyTotalsFunctionsToFormulas(IDataSet dataSet,List<AbstractSelectionField> projections,Filter filter,int maxRowCount){
  List<AbstractSelectionField> toReturnList=new ArrayList<AbstractSelectionField>();
  Set<String> totalFunctions=applyTotalsFunctionsToFormulas_extraction_1(projections);
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

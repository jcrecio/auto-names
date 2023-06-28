@Override protected IDataStore executeSummaryRow(List<AbstractSelectionField> summaryRowProjections,IMetaData metaData,Filter filter,int maxRowCount){
  List<AbstractSelectionField> prjList=new ArrayList<AbstractSelectionField>();
  List<AbstractSelectionField> calcList=new ArrayList<AbstractSelectionField>();
  for (  AbstractSelectionField entry : summaryRowProjections) {
    if (entry instanceof DataStoreCalculatedField) {
      calcList.add(entry);
    }
 else {
      prjList.add(entry);
    }
  }
  IDataStore dataStore=new DataStore(metaData);
  SolrDataSet solrDataSet=dataSet.getImplementation(SolrDataSet.class);
  SolrQuery solrQuery;
  try {
    solrQuery=new ExtendedSolrQuery(solrDataSet.getSolrQuery()).fields(prjList).filter(filter).stats(prjList);
    logger.debug("Solr query for summary row: " + solrQuery);
  }
 catch (  Throwable t) {
    throw new RuntimeException("An unexpected error occured while loading datastore",t);
  }
  SolrClient solrClient=new HttpSolrClient.Builder(solrDataSet.getSolrUrlWithCollection()).build();
  Map<String,FieldStatsInfo> fieldStatsInfo;
  try {
    fieldStatsInfo=solrClient.query(solrQuery).getFieldStatsInfo();
  }
 catch (  SolrServerException|IOException e) {
    throw new RuntimeException(e);
  }
  IRecord summaryRow=new Record(dataStore);
  for (int i=0; i < dataStore.getMetaData().getFieldCount(); i++) {
    String fieldName=dataStore.getMetaData().getFieldName(i);
    boolean found=false;
    found=executeSummaryRow_extraction_1(prjList,dataStore,fieldStatsInfo,summaryRow,i,fieldName,found);
    if (!found) {
      summaryRow.appendField(null);
    }
  }
  dataStore.appendRecord(summaryRow);
  dataStore.getMetaData().setProperty("resultNumber",1);
  try {
    for (    AbstractSelectionField abstractSelectionField : calcList) {
      dataStore=appendCalculatedFieldColumnToSummaryRow(abstractSelectionField,null,dataStore);
    }
  }
 catch (  Throwable t) {
    throw new RuntimeException("An unexpected error occured while loading datastore",t);
  }
  IDataStore dataStoreFinal=new DataStore();
  Record newRecord=new Record();
  MetaData meta=new MetaData();
  for (int i=0; i < metaData.getFieldCount(); i++) {
    for (    AbstractSelectionField entry : summaryRowProjections) {
      executeSummaryRow_extraction_2(metaData,dataStore,newRecord,meta,i,entry);
    }
  }
  dataStoreFinal.setMetaData(meta);
  dataStoreFinal.appendRecord(newRecord);
  return dataStoreFinal;
}

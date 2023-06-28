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
    for (    AbstractSelectionField proj : prjList) {
      if (proj instanceof Projection) {
        Projection projection=(Projection)proj;
        if (projection.getName().equals(fieldName)) {
          Object value=getValue(fieldStatsInfo.get(fieldName),projection.getAggregationFunction());
          IField field=new Field(value);
          dataStore.getMetaData().getFieldMeta(i).setType(value.getClass());
          summaryRow.appendField(field);
          found=true;
          break;
        }
      }
 else {
        DataStoreCalculatedField projection=(DataStoreCalculatedField)proj;
        if (projection.getName().equals(fieldName)) {
          Object value=getValue(fieldStatsInfo.get(fieldName),projection.getAggregationFunction());
          IField field=new Field(value);
          dataStore.getMetaData().getFieldMeta(i).setType(value.getClass());
          summaryRow.appendField(field);
          found=true;
          break;
        }
      }
    }
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
      if (entry instanceof DataStoreCalculatedField) {
        if (metaData.getFieldName(i).equals(((DataStoreCalculatedField)entry).getAlias())) {
          int realIndex=0;
          for (int j=0; j < dataStore.getMetaData().getFieldCount(); j++) {
            if (dataStore.getMetaData().getFieldName(j).equals(((DataStoreCalculatedField)entry).getAlias())) {
              realIndex=j;
            }
          }
          IField field=dataStore.getRecordAt(0).getFieldAt(realIndex);
          meta.addFiedMeta(metaData.getFieldMeta(i));
          newRecord.appendField(field);
        }
      }
 else {
        if (metaData.getFieldName(i).equals(((Projection)entry).getAlias())) {
          executeSummaryRow_extraction_3(metaData,dataStore,newRecord,meta,i,entry);
        }
      }
    }
  }
  dataStoreFinal.setMetaData(meta);
  dataStoreFinal.appendRecord(newRecord);
  return dataStoreFinal;
}

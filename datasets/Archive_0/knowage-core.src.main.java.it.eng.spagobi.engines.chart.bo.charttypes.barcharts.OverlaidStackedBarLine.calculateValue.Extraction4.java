public DatasetMap calculateValue() throws Exception {
  seriesNames=new Vector();
  seriesCaptions=new LinkedHashMap();
  categoriesTooltip=new HashMap<String,String>();
  seriesTooltip=new HashMap<String,String>();
  String res=DataSetAccessFunctions.getDataSetResultFromId(profile,getData(),parametersObject);
  categories=new LinkedHashMap();
  DatasetMap datasetMap=new DatasetMap();
  SourceBean sbRows=SourceBean.fromXMLString(res);
  List listAtts=sbRows.getAttributeAsList("ROW");
  categoriesNumber=0;
  datasetMap.getDatasets().put("stackedbar",new DefaultCategoryDataset());
  datasetMap.getDatasets().put("line",new DefaultCategoryDataset());
  boolean first=true;
  for (Iterator iterator=listAtts.iterator(); iterator.hasNext(); ) {
    SourceBean category=(SourceBean)iterator.next();
    List atts=category.getContainedAttributes();
    HashMap series=new LinkedHashMap();
    HashMap additionalValues=new LinkedHashMap();
    String catValue="";
    String nameP="";
    String value;
    first=calculateValue_extraction_1(first,atts);
    int contSer=0;
    for (Iterator iterator2=atts.iterator(); iterator2.hasNext(); ) {
      SourceBeanAttribute object=(SourceBeanAttribute)iterator2.next();
      nameP=new String(object.getKey());
      value=new String((String)object.getValue());
      if (nameP.equalsIgnoreCase("x")) {
        catValue=value;
        categoriesNumber=categoriesNumber + 1;
        categories.put(new Integer(categoriesNumber),value);
      }
 else {
        contSer=calculateValue_extraction_2(series,additionalValues,catValue,nameP,value,contSer);
      }
    }
    for (Iterator iterator3=series.keySet().iterator(); iterator3.hasNext(); ) {
      String nameS=(String)iterator3.next();
      String labelS="";
      String valueS=(String)series.get(nameS);
      Double valueD=null;
      try {
        valueD=Double.valueOf(valueS);
      }
 catch (      Exception e) {
        logger.warn("error in double conversion, put default to null");
        valueD=null;
      }
      if (!hiddenSeries.contains(nameS)) {
        calculateValue_extraction_4(datasetMap,additionalValues,catValue,nameS,valueD);
      }
    }
  }
  return datasetMap;
}

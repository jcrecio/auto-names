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
    String value="";
    if (first) {
      if (name.indexOf("$F{") >= 0) {
        setTitleParameter(atts);
      }
      if (getSubName() != null && getSubName().indexOf("$F") >= 0) {
        setSubTitleParameter(atts);
      }
      first=false;
    }
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
        if (nameP.startsWith("add_") || nameP.startsWith("ADD_")) {
          if (additionalLabels) {
            String ind=nameP.substring(4);
            additionalValues.put(ind,value);
          }
        }
 else         if (nameP.toUpperCase().startsWith("TIP_X")) {
          if (enableToolTips) {
            categoriesTooltip.put(nameP + "_" + catValue,value);
          }
        }
 else         if (nameP.toUpperCase().startsWith("TIP_")) {
          if (enableToolTips) {
            seriesTooltip.put(nameP,value);
          }
        }
 else         if (nameP.toUpperCase().startsWith("FREETIP_X")) {
          if (enableToolTips) {
            freeToolTips=true;
            categoriesTooltip.put(nameP + "_" + catValue,value);
          }
        }
 else {
          if (seriesLabelsMap != null) {
            if ((this.getNumberSerVisualization() > 0 && contSer < this.getNumberSerVisualization()) && seriesDraw.get(nameP) != null && ((String)seriesDraw.get(nameP)).equalsIgnoreCase("StackedBar")) {
              String serieLabel=(String)seriesLabelsMap.get(nameP);
              series.put(serieLabel,value);
              seriesCaptions.put(serieLabel,nameP);
              contSer++;
            }
 else             if (this.getNumberSerVisualization() == 0 || (seriesDraw.get(nameP) != null && ((String)seriesDraw.get(nameP)).equalsIgnoreCase("line"))) {
              String serieLabel=(String)seriesLabelsMap.get(nameP);
              series.put(serieLabel,value);
              seriesCaptions.put(serieLabel,nameP);
            }
          }
 else           if (this.getNumberSerVisualization() > 0 && contSer < this.getNumberSerVisualization() && ((String)seriesDraw.get(nameP)).equalsIgnoreCase("StackedBar")) {
            series.put(nameP,value);
            contSer++;
          }
 else           if (this.getNumberSerVisualization() == 0 || ((String)seriesDraw.get(nameP)).equalsIgnoreCase("line")) {
            series.put(nameP,value);
          }
        }
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
        if (seriesLabelsMap != null && (seriesCaptions != null && seriesCaptions.size() > 0)) {
          nameS=(String)(seriesCaptions.get(nameS));
          labelS=(String)seriesLabelsMap.get(nameS);
        }
 else         labelS=nameS;
        if (seriesDraw.get(nameS) != null && ((String)seriesDraw.get(nameS.toUpperCase())).equalsIgnoreCase("line")) {
          if (!seriesNames.contains(nameS.toUpperCase()))           seriesNames.add(nameS.toUpperCase());
          ((DefaultCategoryDataset)(datasetMap.getDatasets().get("line"))).addValue(valueD != null ? valueD.doubleValue() : null,labelS,catValue);
        }
 else {
          if (!seriesNames.contains(nameS.toUpperCase()))           seriesNames.add(nameS.toUpperCase());
          ((DefaultCategoryDataset)(datasetMap.getDatasets().get("stackedbar"))).addValue(valueD != null ? valueD.doubleValue() : null,labelS,catValue);
        }
        if (additionalValues.get(nameS) != null) {
          String val=(String)additionalValues.get(nameS);
          String index=catValue + "-" + nameS;
          catSerLabels.put(index,val);
        }
      }
    }
  }
  return datasetMap;
}

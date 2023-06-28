/** 
 * Override this functions from BarCharts beacuse I want the hidden serie to be the first!
 * @return the dataset
 * @throws Exception the exception
 */
public DatasetMap calculateValue() throws Exception {
  logger.debug("IN");
  String res=DataSetAccessFunctions.getDataSetResultFromId(profile,getData(),parametersObject);
  categories=new HashMap();
  double cumulativeValue=0.0;
  DefaultCategoryDataset dataset=new DefaultCategoryDataset();
  SourceBean sbRows=SourceBean.fromXMLString(res);
  List listAtts=sbRows.getAttributeAsList("ROW");
  categoriesNumber=0;
  seriesNames=new Vector();
  catGroupNames=new Vector();
  if (filterCatGroups == true) {
    catGroups=new HashMap();
  }
  boolean first=true;
  for (Iterator iterator=listAtts.iterator(); iterator.hasNext(); ) {
    SourceBean category=(SourceBean)iterator.next();
    List atts=category.getContainedAttributes();
    HashMap series=new HashMap();
    HashMap additionalValues=new HashMap();
    String catValue="";
    String cat_group_name="";
    String nameP="";
    String value="";
    ArrayList orderSeries=new ArrayList();
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
 else       if (nameP.equalsIgnoreCase("cat_group")) {
        cat_group_name=value;
      }
 else {
        nameP=nameP.toUpperCase();
        if (nameP.startsWith("ADD_")) {
          if (additionalLabels) {
            String ind=nameP.substring(4);
            additionalValues.put(ind,value);
          }
        }
 else         if (this.getNumberSerVisualization() > 0 && contSer < this.getNumberSerVisualization()) {
          String serieName=nameP;
          if (seriesLabelsMap != null && seriesLabelsMap.keySet().contains(nameP)) {
            serieName=seriesLabelsMap.get(nameP).toString();
          }
          series.put(serieName,value);
          orderSeries.add(serieName);
          contSer++;
        }
 else         if (this.getNumberSerVisualization() == 0) {
          String serieName=nameP;
          if (seriesLabelsMap != null && seriesLabelsMap.keySet().contains(nameP)) {
            serieName=seriesLabelsMap.get(nameP).toString();
          }
          series.put(serieName,value);
          orderSeries.add(serieName);
        }
      }
    }
    if (!cat_group_name.equalsIgnoreCase("") && !catValue.equalsIgnoreCase("") && catGroups != null) {
      catGroups.put(catValue,cat_group_name);
      if (!(catGroupNames.contains(cat_group_name))) {
        catGroupNames.add(cat_group_name);
      }
    }
    if (cumulative) {
      dataset.addValue(cumulativeValue,"CUMULATIVE",catValue);
    }
    for (Iterator iterator3=orderSeries.iterator(); iterator3.hasNext(); ) {
      String nameS=(String)iterator3.next();
      if (!hiddenSeries.contains(nameS)) {
        String valueS=((String)series.get(nameS)).equalsIgnoreCase("null") ? "0" : (String)series.get(nameS);
        Double valueD=null;
        try {
          valueD=Double.valueOf(valueS);
        }
 catch (        Exception e) {
          logger.warn("error in double conversion, put default to null");
          valueD=null;
        }
        dataset.addValue(valueD != null ? valueD.doubleValue() : null,nameS,catValue);
        cumulativeValue+=valueD != null ? valueD.doubleValue() : 0.0;
        if (!seriesNames.contains(nameS)) {
          seriesNames.add(nameS);
        }
        if (additionalValues.get(nameS) != null) {
          String val=(String)additionalValues.get(nameS);
          String index=catValue + "-" + nameS;
          String totalVal=val;
          catSerLabels.put(index,totalVal);
        }
      }
    }
    if (additionalValues.get("CUMULATIVE") != null) {
      String val=(String)additionalValues.get("CUMULATIVE");
      String index=catValue + "-" + "CUMULATIVE";
      catSerLabels.put(index,val);
    }
  }
  logger.debug("OUT");
  DatasetMap datasets=new DatasetMap();
  datasets.addDataset("1",dataset);
  return datasets;
}

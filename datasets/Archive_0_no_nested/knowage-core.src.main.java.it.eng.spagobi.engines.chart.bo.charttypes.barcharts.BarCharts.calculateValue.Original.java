/** 
 * Inherited by IChart: calculates chart value.
 * @return the dataset
 * @throws Exception the exception
 */
public DatasetMap calculateValue() throws Exception {
  logger.debug("IN");
  String res=DataSetAccessFunctions.getDataSetResultFromId(profile,getData(),parametersObject);
  categories=new HashMap();
  seriesCaptions=new LinkedHashMap();
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
    if (first) {
      if (name.indexOf("$F{") >= 0) {
        setTitleParameter(atts);
      }
      if (getSubName() != null && getSubName().indexOf("$F") >= 0) {
        setSubTitleParameter(atts);
      }
      first=false;
    }
    HashMap series=new HashMap();
    String catValue="";
    String cat_group_name="";
    String name="";
    String value="";
    int contSer=0;
    for (Iterator iterator2=atts.iterator(); iterator2.hasNext(); ) {
      SourceBeanAttribute object=(SourceBeanAttribute)iterator2.next();
      name=new String(object.getKey());
      value=new String((String)object.getValue());
      if (name.equalsIgnoreCase("x")) {
        catValue=value;
        categoriesNumber=categoriesNumber + 1;
        categories.put(new Integer(categoriesNumber),value);
      }
 else       if (name.equalsIgnoreCase("cat_group")) {
        cat_group_name=value;
      }
 else       if (this.getNumberSerVisualization().intValue() > 0 && contSer < this.getNumberSerVisualization().intValue()) {
        series.put(name,value);
        contSer++;
      }
 else       if (seriesLabelsMap != null) {
        String serieLabel=(String)seriesLabelsMap.get(name);
        if (serieLabel != null) {
          series.put(serieLabel,value);
          seriesCaptions.put(serieLabel,name);
        }
      }
 else       series.put(name,value);
    }
    if (!cat_group_name.equalsIgnoreCase("") && !catValue.equalsIgnoreCase("") && catGroups != null) {
      catGroups.put(catValue,cat_group_name);
      if (!(catGroupNames.contains(cat_group_name))) {
        catGroupNames.add(cat_group_name);
      }
    }
    for (Iterator iterator3=series.keySet().iterator(); iterator3.hasNext(); ) {
      String nameS=(String)iterator3.next();
      String labelS="";
      if (!hiddenSeries.contains(nameS)) {
        if (seriesLabelsMap != null && (seriesCaptions != null && seriesCaptions.size() > 0)) {
          nameS=(String)(seriesCaptions.get(nameS));
          labelS=(String)seriesLabelsMap.get(nameS);
        }
 else         labelS=nameS;
        String valueS=(String)series.get(labelS);
        if (labelS != null && valueS != null && !valueS.equals("null") && !valueS.equals("")) {
          dataset.addValue(Double.valueOf(valueS).doubleValue(),labelS,catValue);
          if (!seriesNames.contains(labelS)) {
            seriesNames.add(labelS);
          }
        }
      }
    }
  }
  if (listAtts.size() == 0) {
    if (name.indexOf("$F{") >= 0) {
      setTitleParameter("");
    }
    if (getSubName() != null && getSubName().indexOf("$F") >= 0) {
      setSubTitleParameter("");
    }
  }
  logger.debug("OUT");
  DatasetMap datasets=new DatasetMap();
  datasets.addDataset("1",dataset);
  return datasets;
}

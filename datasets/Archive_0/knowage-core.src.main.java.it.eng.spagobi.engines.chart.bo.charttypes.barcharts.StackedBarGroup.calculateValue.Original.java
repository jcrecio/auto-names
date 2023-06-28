/** 
 * Override this functions from BarCharts beacuse I manage a group of stacked bar!
 * @return the dataset
 * @throws Exception the exception
 */
public DatasetMap calculateValue() throws Exception {
  logger.debug("IN");
  String res=DataSetAccessFunctions.getDataSetResultFromId(profile,getData(),parametersObject);
  categories=new LinkedHashMap();
  subCategories=new LinkedHashMap();
  subCategoryNames=new ArrayList();
  categoryNames=new ArrayList();
  DefaultCategoryDataset dataset=new DefaultCategoryDataset();
  SourceBean sbRows=SourceBean.fromXMLString(res);
  List listAtts=sbRows.getAttributeAsList("ROW");
  categoriesNumber=0;
  seriesNames=new Vector();
  boolean first=true;
  for (Iterator iterator=listAtts.iterator(); iterator.hasNext(); ) {
    SourceBean category=(SourceBean)iterator.next();
    List atts=category.getContainedAttributes();
    HashMap myseries=new LinkedHashMap();
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
        categoriesNumber++;
        categories.put(new Integer(categoriesNumber),value);
        if (!categoryNames.contains(value)) {
          categoryNames.add(value);
          realCatNumber++;
        }
      }
 else       if (nameP.equalsIgnoreCase("x2")) {
        subCategoriesNumber++;
        subCategories.put(new Integer(subCategoriesNumber),value);
        if (!subCategoryNames.contains(value)) {
          subCategoryNames.add(value);
          realSubCatNumber++;
        }
      }
 else {
        if (nameP.startsWith("add_") || nameP.startsWith("ADD_")) {
          if (additionalLabels) {
            String ind=nameP.substring(4);
            additionalValues.put(ind,value);
          }
        }
 else {
          if (this.getNumberSerVisualization() > 0 && contSer < this.getNumberSerVisualization()) {
            myseries.put(nameP,value);
            contSer++;
          }
 else           if (this.getNumberSerVisualization() == 0)           myseries.put(nameP,value);
        }
      }
    }
    for (Iterator iterator3=myseries.keySet().iterator(); iterator3.hasNext(); ) {
      String nameS=(String)iterator3.next();
      if (!hiddenSeries.contains(nameS)) {
        String valueS=(String)myseries.get(nameS);
        Double valueD=null;
        try {
          valueD=Double.valueOf(valueS);
        }
 catch (        Exception e) {
          logger.warn("error in double conversion, put default to null");
          valueD=null;
        }
        String subcat=(String)subCategoryNames.get(realSubCatNumber - 1);
        dataset.addValue(valueD != null ? valueD.doubleValue() : null,subcat,catValue);
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
  }
  logger.debug("OUT");
  DatasetMap datasets=new DatasetMap();
  datasets.addDataset("1",dataset);
  return datasets;
}

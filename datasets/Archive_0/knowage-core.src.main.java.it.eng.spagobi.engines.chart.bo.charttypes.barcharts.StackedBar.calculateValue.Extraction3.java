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
    catValue=calculateValue_extraction_1(cumulativeValue,dataset,atts,series,additionalValues,catValue,cat_group_name,orderSeries);
    for (Iterator iterator3=orderSeries.iterator(); iterator3.hasNext(); ) {
      String nameS=(String)iterator3.next();
      cumulativeValue=calculateValue_extraction_3(cumulativeValue,dataset,series,additionalValues,catValue,nameS);
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

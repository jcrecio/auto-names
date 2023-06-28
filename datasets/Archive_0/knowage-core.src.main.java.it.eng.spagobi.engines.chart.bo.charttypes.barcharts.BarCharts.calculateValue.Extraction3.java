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
      first=calculateValue_extraction_1(atts);
    }
    HashMap series=new HashMap();
    String catValue="";
    catValue=calculateValue_extraction_2(atts,series,catValue);
    for (Iterator iterator3=series.keySet().iterator(); iterator3.hasNext(); ) {
      calculateValue_extraction_3(dataset,series,catValue,iterator3);
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

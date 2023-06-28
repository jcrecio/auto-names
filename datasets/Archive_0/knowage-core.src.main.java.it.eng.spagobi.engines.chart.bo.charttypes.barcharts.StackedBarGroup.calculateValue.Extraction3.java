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
    calculateValue_extraction_1(dataset,atts,myseries,additionalValues,catValue,contSer);
  }
  logger.debug("OUT");
  DatasetMap datasets=new DatasetMap();
  datasets.addDataset("1",dataset);
  return datasets;
}

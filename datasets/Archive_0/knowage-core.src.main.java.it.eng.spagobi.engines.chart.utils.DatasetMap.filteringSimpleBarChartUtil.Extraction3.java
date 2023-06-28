/** 
 * @param aServiceResponse: This is the service response, check it's not null, if it has requested parameters they win against request ones, cause means that chart has been re-executed
 * @param request
 * @param sbi
 * @param sbiMode
 * @param docComposition
 * @return
 */
public DatasetMap filteringSimpleBarChartUtil(AttributesContainer requestCont,AttributesContainer responseCont,BarCharts sbi,String sbiMode,boolean docComposition){
  logger.debug("IN");
  DefaultCategoryDataset dataset=(DefaultCategoryDataset)datasets.get("1");
  Dataset copyDataset=null;
  DatasetMap newDatasetMap=null;
  boolean notDisappearSlider=false;
  try {
    copyDataset=(DefaultCategoryDataset)dataset.clone();
  }
 catch (  CloneNotSupportedException e) {
    logger.error("error copying dataset");
    e.printStackTrace();
  }
  try {
    series=new TreeSet(((DefaultCategoryDataset)dataset).getRowKeys());
    categories=(HashMap)((BarCharts)sbi).getCategories();
    catsnum=new Integer(sbi.getCategoriesNumber());
    if (requestCont.getAttribute("n_visualization") != null) {
      String nVis=(String)requestCont.getAttribute("n_visualization");
      Integer catD=Integer.valueOf(nVis);
      if (catD.equals(0))       catD=new Integer(catsnum);
      sbi.setNumberCatVisualization(catD);
      if (catD >= catsnum)       notDisappearSlider=true;
    }
    numberCatVisualization=sbi.getNumberCatVisualization();
    numberSerVisualization=sbi.getNumberSerVisualization();
    catTitle=sbi.getCategoryLabel();
    serTitle=sbi.getValueLabel();
    filteringSimpleBarChartUtil_extraction_1(requestCont,responseCont,sbi);
    HashMap cats=(HashMap)((BarCharts)sbi).getCategories();
    if (categoryCurrent != 0) {
      categoryCurrentName=(String)cats.get(new Integer(categoryCurrent));
      logger.debug("current category " + categoryCurrentName);
      copyDataset=(DefaultCategoryDataset)sbi.filterDataset(copyDataset,categories,categoryCurrent,numberCatVisualization.intValue());
    }
 else {
      logger.debug("current category is the first");
      categoryCurrentName="All";
    }
    selectedCatGroups=new Vector();
    logger.debug("check particular category groups");
    copyDataset=filteringSimpleBarChartUtil_extraction_2(requestCont,sbi,copyDataset);
    if (sbi.isFilterCategories() == true && (catsnum.intValue()) > numberCatVisualization.intValue()) {
      logger.debug("slider is to be drawn");
      makeSlider=true;
    }
 else     if (sbi.isFilterCategories() == true && notDisappearSlider == true) {
      logger.debug("slider is to be drawn");
      makeSlider=true;
    }
    filterStyle=sbi.getFilterStyle();
    if (copyDataset == null) {
      copyDataset=dataset;
    }
    newDatasetMap=this.copyDatasetMap(copyDataset);
  }
 catch (  Exception e) {
    logger.error("Error while filtering simple Chart ",e);
  }
  dynamicNVisualization=sbi.isDynamicNumberCatVisualization();
  logger.debug("OUT");
  return newDatasetMap;
}

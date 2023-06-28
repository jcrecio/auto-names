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
    if (!responseCont.isNull() && responseCont.getAttribute("category") != null) {
      String catS=(String)responseCont.getAttribute("category");
      logger.debug("category specified in module response by slider " + catS);
      Double catD=Double.valueOf(catS);
      categoryCurrent=catD.intValue();
    }
 else     if (requestCont.getParameter("categoryAll") != null) {
      logger.debug("All categories have to be shown");
      categoryCurrent=0;
    }
 else     if (requestCont.getParameter("category") != null) {
      String catS=(String)requestCont.getParameter("category");
      logger.debug("category specified in request by slider " + catS);
      Double catD=Double.valueOf(catS);
      categoryCurrent=catD.intValue();
    }
 else {
      logger.debug("no particulary category specified by slider: startFromEnd option is " + sbi.isSliderStartFromEnd());
      if (sbi.isSliderStartFromEnd() == true) {
        categoryCurrent=sbi.getCategoriesNumber() - (numberCatVisualization != null ? numberCatVisualization.intValue() : 1) + 1;
      }
 else {
        categoryCurrent=1;
      }
    }
    valueSlider=(new Integer(categoryCurrent)).toString();
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
    if (sbi.isFilterCatGroups() == true) {
      if (requestCont.getParameter("cat_group") != null) {
        Object[] cio=requestCont.getParameterValues("cat_group");
        for (int i=0; i < cio.length; i++) {
          selectedCatGroups.add(cio[i].toString());
        }
      }
 else {
        selectedCatGroups.add("allgroups");
      }
      if (selectedCatGroups.contains("allgroups")) {
        ((BarCharts)sbi).setCurrentCatGroups(null);
      }
 else {
        copyDataset=sbi.filterDatasetCatGroups(copyDataset,selectedCatGroups);
      }
    }
 else     selectedCatGroups.add("allgroups");
    copyDataset=filteringSimpleBarChartUtil_extraction_3(requestCont,sbi,copyDataset);
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

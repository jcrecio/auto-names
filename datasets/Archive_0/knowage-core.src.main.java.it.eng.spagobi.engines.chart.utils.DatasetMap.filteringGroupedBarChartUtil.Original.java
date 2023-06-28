public DatasetMap filteringGroupedBarChartUtil(AttributesContainer requestCont,AttributesContainer responseCont,StackedBarGroup sbi,String sbiMode,boolean docComposition){
  logger.debug("IN");
  DefaultCategoryDataset dataset=(DefaultCategoryDataset)datasets.get("1");
  Dataset copyDataset=null;
  boolean notDisappearSlider=false;
  try {
    copyDataset=(DefaultCategoryDataset)dataset.clone();
  }
 catch (  CloneNotSupportedException e) {
    e.printStackTrace();
  }
  series=new TreeSet();
  int contSer=0;
  for (Iterator iterator2=(((DefaultCategoryDataset)dataset).getRowKeys()).iterator(); iterator2.hasNext(); ) {
    if (this.getNumberSerVisualization() > 0 && contSer < this.getNumberSerVisualization()) {
      String serie=(String)iterator2.next();
      if (!series.contains(serie)) {
        series.add(serie);
        contSer++;
      }
    }
 else     if (this.getNumberSerVisualization() == 0) {
      String serie=(String)iterator2.next();
      if (!series.contains(serie))       series.add(serie);
    }
  }
  categories=(HashMap)((BarCharts)sbi).getCategories();
  catsnum=new Integer(sbi.getRealCatNumber());
  if (responseCont.getAttribute("n_visualization") != null) {
    String nVis=(String)responseCont.getAttribute("n_visualization");
    Integer catD=Integer.valueOf(nVis);
    if (catD.equals(0))     catD=new Integer(catsnum);
    sbi.setNumberCatVisualization(catD);
    if (catD >= catsnum)     notDisappearSlider=true;
  }
  numberCatVisualization=sbi.getNumberCatVisualization();
  numberSerVisualization=sbi.getNumberSerVisualization();
  subCategories=(HashMap)((StackedBarGroup)sbi).getSubCategories();
  catTitle=sbi.getCategoryLabel();
  subcatTitle=sbi.getSubCategoryLabel();
  serTitle=sbi.getValueLabel();
  if (responseCont != null && responseCont.getAttribute("category") != null) {
    String catS=(String)responseCont.getAttribute("category");
    logger.debug("category specified in module response by slider " + catS);
    Double catD=Double.valueOf(catS);
    categoryCurrent=catD.intValue();
  }
 else   if (requestCont.getParameter("categoryAll") != null) {
    logger.debug("All categories have to be shown");
    categoryCurrent=0;
  }
 else   if (requestCont.getParameter("category") != null) {
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
    categoryCurrentName="All";
    valueSlider="1";
  }
  selectedSeries=new Vector();
  if (requestCont.getParameter("serie") != null) {
    Object[] cio=requestCont.getParameterValues("serie");
    for (int i=0; i < cio.length; i++) {
      selectedSeries.add(cio[i].toString());
    }
  }
 else {
    selectedSeries.add("allseries");
  }
  if (selectedSeries.contains("allseries")) {
    ((BarCharts)sbi).setCurrentSeries(null);
  }
 else {
    copyDataset=sbi.filterDatasetSeries(copyDataset,selectedSeries);
  }
  if (sbi.isFilterCategories() == true && (catsnum.intValue()) > numberCatVisualization.intValue()) {
    makeSlider=true;
  }
 else   if (sbi.isFilterCategories() == true && notDisappearSlider == true) {
    logger.debug("slider is to be drawn");
    makeSlider=true;
  }
  filterStyle=sbi.getFilterStyle();
  if (copyDataset == null) {
    copyDataset=dataset;
  }
  DatasetMap newDatasetMap=this.copyDatasetMap(copyDataset);
  dynamicNVisualization=sbi.isDynamicNumberCatVisualization();
  logger.debug("OUT");
  return newDatasetMap;
}

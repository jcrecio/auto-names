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
  copyDataset=filteringGroupedBarChartUtil_extraction_1(requestCont,responseCont,sbi,copyDataset,notDisappearSlider);
  filterStyle=sbi.getFilterStyle();
  if (copyDataset == null) {
    copyDataset=dataset;
  }
  DatasetMap newDatasetMap=this.copyDatasetMap(copyDataset);
  dynamicNVisualization=sbi.isDynamicNumberCatVisualization();
  logger.debug("OUT");
  return newDatasetMap;
}

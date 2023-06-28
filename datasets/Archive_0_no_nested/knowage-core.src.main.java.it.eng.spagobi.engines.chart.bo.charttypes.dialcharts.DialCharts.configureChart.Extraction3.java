public void configureChart(SourceBean content){
  logger.debug("IN");
  super.configureChart(content);
  try {
    configureChart_extraction_1(content);
    SourceBean styleValueLabelsSB=(SourceBean)content.getAttribute(STYLE_VALUE_LABEL);
    if (styleValueLabelsSB != null) {
      configureChart_extraction_2(styleValueLabelsSB);
    }
 else {
      labelsValueStyle=defaultLabelsStyle;
    }
    if (isLovConfDefined == false) {
      logger.debug("Configuration in template");
      confParameters=new HashMap();
      configureChart_extraction_3(content);
    }
 else {
      logger.debug("configuration parameters set in LOV");
      String parameters=DataSetAccessFunctions.getDataSetResultFromLabel(profile,confDataset,parametersObject);
      SourceBean sourceBeanResult=null;
      try {
        sourceBeanResult=SourceBean.fromXMLString(parameters);
      }
 catch (      SourceBeanException e) {
        logger.error("error in reading configuration lov");
        throw new Exception("error in reading configuration lov");
      }
      sbRow=(SourceBean)sourceBeanResult.getAttribute("ROW");
      String lower=(String)sbRow.getAttribute(LOWER);
      String upper=(String)sbRow.getAttribute(UPPER);
      String legend=(String)sbRow.getAttribute(LEGEND);
      String multichart=(String)sbRow.getAttribute(MULTICHART);
      String orientation=(String)sbRow.getAttribute(ORIENTATION_MULTICHART);
      if (lower == null || upper == null) {
        logger.error("error in reading configuration lov");
        throw new Exception("error in reading configuration lov");
      }
      setLower(Double.valueOf(lower).doubleValue());
      setUpper(Double.valueOf(upper).doubleValue());
      setMultichart((multichart.equalsIgnoreCase("true") ? true : false));
      setLegend(legend.equalsIgnoreCase("true") ? true : false);
      setOrientationMultichart(orientation);
    }
  }
 catch (  Exception e) {
    logger.error("error in reading template configurations");
  }
  logger.debug("OUT");
}

public void configureChart(SourceBean content){
  logger.debug("IN");
  super.configureChart(content);
  try {
    SourceBean styleTickLabelsSB=(SourceBean)content.getAttribute(STYLE_TICK_LABELS);
    if (styleTickLabelsSB != null) {
      String fontS=(String)styleTickLabelsSB.getAttribute(FONT_STYLE);
      if (fontS == null) {
        fontS=defaultLabelsStyle.getFontName();
      }
      String sizeS=(String)styleTickLabelsSB.getAttribute(SIZE_STYLE);
      String colorS=(String)styleTickLabelsSB.getAttribute(COLOR_STYLE);
      try {
        Color color=Color.BLACK;
        if (colorS != null) {
          color=Color.decode(colorS);
        }
 else {
          defaultLabelsStyle.getColor();
        }
        int size=12;
        if (sizeS != null) {
          size=Integer.valueOf(sizeS).intValue();
        }
 else {
          size=defaultLabelsStyle.getSize();
        }
        labelsTickStyle=new StyleLabel(fontS,size,color);
      }
 catch (      Exception e) {
        logger.error("Wrong style labels settings, use default");
      }
    }
 else {
      labelsTickStyle=defaultLabelsStyle;
    }
    SourceBean styleValueLabelsSB=(SourceBean)content.getAttribute(STYLE_VALUE_LABEL);
    if (styleValueLabelsSB != null) {
      String fontS=(String)styleValueLabelsSB.getAttribute(FONT_STYLE);
      if (fontS == null) {
        fontS=defaultLabelsStyle.getFontName();
      }
      String sizeS=(String)styleValueLabelsSB.getAttribute(SIZE_STYLE);
      String colorS=(String)styleValueLabelsSB.getAttribute(COLOR_STYLE);
      try {
        Color color=Color.BLACK;
        if (colorS != null) {
          color=Color.decode(colorS);
        }
 else {
          defaultLabelsStyle.getColor();
        }
        int size=12;
        if (sizeS != null) {
          size=Integer.valueOf(sizeS).intValue();
        }
 else {
          size=defaultLabelsStyle.getSize();
        }
        labelsValueStyle=new StyleLabel(fontS,size,color);
      }
 catch (      Exception e) {
        logger.error("Wrong style labels settings, use default");
      }
    }
 else {
      labelsValueStyle=defaultLabelsStyle;
    }
    if (isLovConfDefined == false) {
      logger.debug("Configuration in template");
      confParameters=new HashMap();
      SourceBean confSB=(SourceBean)content.getAttribute("CONF");
      List confAttrsList=confSB.getAttributeAsList("PARAMETER");
      Iterator confAttrsIter=confAttrsList.iterator();
      while (confAttrsIter.hasNext()) {
        SourceBean param=(SourceBean)confAttrsIter.next();
        String nameParam=(String)param.getAttribute("name");
        String valueParam=(String)param.getAttribute("value");
        confParameters.put(nameParam,valueParam);
      }
      if (confParameters.get(LOWER) != null) {
        String lower=(String)confParameters.get(LOWER);
        setLower(Double.valueOf(lower).doubleValue());
      }
 else {
        logger.error("lower bound not defined");
        throw new Exception("lower bound not defined");
      }
      if (confParameters.get(UPPER) != null) {
        String upper=(String)confParameters.get(UPPER);
        setUpper(Double.valueOf(upper).doubleValue());
      }
 else {
        logger.error("upper bound not defined");
        throw new Exception("upper bound not defined");
      }
      if (confParameters.get(UNITS) != null) {
        String units=(String)confParameters.get(UNITS);
        setUnits(units);
      }
      multichart=false;
      if (confParameters.get("multichart") != null && !(((String)confParameters.get(MULTICHART)).equalsIgnoreCase(""))) {
        String multiple=(String)confParameters.get(MULTICHART);
        if (multiple.equalsIgnoreCase("true"))         setMultichart(true);
      }
      orientationMultichart="horizontal";
      if (confParameters.get(ORIENTATION_MULTICHART) != null && !(((String)confParameters.get(ORIENTATION_MULTICHART)).equalsIgnoreCase(""))) {
        String ori=(String)confParameters.get(ORIENTATION_MULTICHART);
        if (ori.equalsIgnoreCase("horizontal") || ori.equalsIgnoreCase("vertical"))         setOrientationMultichart(ori);
      }
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

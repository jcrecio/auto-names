@Override public void configureChart(SourceBean content){
  logger.debug("IN");
  super.configureChart(content);
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
  if (confParameters.get(X_LABEL) != null) {
    xLabel=(String)confParameters.get(X_LABEL);
  }
 else {
    xLabel="Hours";
  }
  if (confParameters.get(Y_LABEL) != null) {
    yLabel=(String)confParameters.get(Y_LABEL);
  }
 else {
    yLabel="Time";
  }
  configureChart_extraction_1();
  SourceBean styleAnnotationSB=configureChart_extraction_2(content);
  if (styleAnnotationSB != null) {
    String fontS=(String)styleAnnotationSB.getAttribute(FONT_STYLE);
    if (fontS == null)     fontS="ARIAL";
    String sizeS=(String)styleAnnotationSB.getAttribute(SIZE_STYLE);
    if (sizeS == null)     sizeS="8";
    String colorS=(String)styleAnnotationSB.getAttribute(COLOR_STYLE);
    if (colorS == null)     colorS="#000000";
    try {
      Color color=Color.decode(colorS);
      int size=Integer.valueOf(sizeS).intValue();
      styleAnnotation=new StyleLabel(fontS,size,color);
    }
 catch (    Exception e) {
      logger.error("Wrong style Annotation settings, use default");
    }
  }
  logger.debug("OUT");
}

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
  if (confParameters.get(HOUR_MAX) != null && confParameters.get(HOUR_MIN) != null) {
    String xMaxS=(String)confParameters.get(HOUR_MAX);
    String xMinS=(String)confParameters.get(HOUR_MIN);
    hourMax=Double.valueOf(xMaxS);
    hourMin=Double.valueOf(xMinS);
  }
 else {
    hourMax=24.0;
    hourMin=0.0;
  }
  if (confParameters.get(DATE_AUTO_RANGE) != null) {
    if (confParameters.get(DATE_AUTO_RANGE).toString().equalsIgnoreCase("true"))     dateAutoRange=true;
 else     dateAutoRange=false;
  }
 else {
    dateAutoRange=false;
  }
  if (confParameters.get(ADD_TRANSPARENCY) != null) {
    if (confParameters.get(ADD_TRANSPARENCY).toString().equalsIgnoreCase("true"))     addTransparency=true;
 else     addTransparency=false;
  }
 else {
    addTransparency=false;
  }
  if (confParameters.get(VIEW_DATE_FORMAT) != null) {
    String viewFormat=(String)confParameters.get(VIEW_DATE_FORMAT);
    try {
      viewDateFormat=new SimpleDateFormat(viewFormat);
    }
 catch (    Exception e) {
      logger.error("Wrong date format " + viewFormat + ": use default");
      viewDateFormat=new SimpleDateFormat("dd/MM/yyyy");
    }
  }
 else {
    viewDateFormat=new SimpleDateFormat("dd/MM/yyyy");
  }
  if (confParameters.get(BEGIN_DATE_FORMAT) != null) {
    beginDateFormat=(String)confParameters.get(BEGIN_DATE_FORMAT);
  }
 else {
    beginDateFormat="dd/MM/yyyy";
  }
  logger.debug("begin date format " + beginDateFormat);
  if (confParameters.get(BEGIN_HOUR_FORMAT) != null) {
    beginHourFormat=(String)confParameters.get(BEGIN_HOUR_FORMAT);
  }
 else {
    beginHourFormat="dd/MM/yyyy HH:mm:ss";
  }
  logger.debug("begin hour format " + beginHourFormat);
  if (confParameters.get(DATE_MAX) != null && confParameters.get(DATE_MIN) != null) {
    String dateMaxS=(String)confParameters.get(DATE_MAX);
    String dateMinS=(String)confParameters.get(DATE_MIN);
    try {
      DateFormat myDateFormat=new SimpleDateFormat("dd/MM/yyyy");
      dateMin=myDateFormat.parse(dateMinS);
      dateMax=myDateFormat.parse(dateMaxS);
    }
 catch (    Exception e) {
      logger.error("Could not convert begin or end date");
      dateMin=null;
      dateMax=null;
    }
  }
  ranges=new ArrayList<RangeBlocks>();
  SourceBean rangesSB=(SourceBean)content.getAttribute("RANGES");
  List rangesList=rangesSB.getAttributeAsList("RANGE");
  Iterator rangesIter=rangesList.iterator();
  while (rangesIter.hasNext()) {
    SourceBean range=(SourceBean)rangesIter.next();
    String rangeLabel=(String)range.getAttribute("label");
    String rangeColor=(String)range.getAttribute("color");
    String rangePattern=(String)range.getAttribute("pattern");
    Color color=null;
    if (rangeColor != null) {
      color=new Color(Integer.decode(rangeColor).intValue());
    }
    RangeBlocks block=new RangeBlocks(rangeLabel,rangePattern,color);
    ranges.add(block);
  }
  SourceBean styleAnnotationSB=(SourceBean)content.getAttribute(STYLE_ANNOTATION_LABELS);
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

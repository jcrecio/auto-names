public void configureChart(SourceBean content){
  logger.debug("IN");
  super.configureChart(content);
  if (confParameters.get(ORIENTATION) != null) {
    String orientation=(String)confParameters.get(ORIENTATION);
    if (orientation.equalsIgnoreCase("vertical")) {
      horizontalViewConfigured=true;
      horizontalView=false;
    }
 else     if (orientation.equalsIgnoreCase("horizontal")) {
      horizontalViewConfigured=true;
      horizontalView=true;
    }
  }
  if (confParameters.get(CUMULATIVE) != null) {
    String orientation=(String)confParameters.get(CUMULATIVE);
    if (orientation.equalsIgnoreCase("true")) {
      cumulative=true;
    }
 else {
      cumulative=false;
    }
  }
  if (confParameters.get(ADD_LABELS) != null) {
    String additional=(String)confParameters.get(ADD_LABELS);
    if (additional.equalsIgnoreCase("true")) {
      additionalLabels=true;
      catSerLabels=new HashMap();
    }
 else     additionalLabels=false;
  }
 else {
    additionalLabels=false;
  }
  if (confParameters.get(MAKE_PERCENTAGE) != null) {
    String perc=(String)confParameters.get(MAKE_PERCENTAGE);
    if (perc.equalsIgnoreCase("true")) {
      makePercentage=true;
    }
 else     makePercentage=false;
  }
 else {
    makePercentage=false;
  }
  if (confParameters.get(PERCENTAGE_VALUE) != null) {
    String perc=(String)confParameters.get(PERCENTAGE_VALUE);
    if (perc.equalsIgnoreCase("true")) {
      percentageValue=true;
    }
 else     percentageValue=false;
  }
 else {
    percentageValue=false;
  }
  SourceBean drillSB=(SourceBean)content.getAttribute("DRILL");
  if (drillSB == null) {
    drillSB=(SourceBean)content.getAttribute("CONF.DRILL");
  }
  if (drillSB != null) {
    String lab=(String)drillSB.getAttribute("document");
    if (lab != null)     drillLabel=lab;
 else {
      logger.error("Drill label not found");
    }
    List parameters=drillSB.getAttributeAsList("PARAM");
    if (parameters != null) {
      drillParametersMap=new HashMap<String,DrillParameter>();
      for (Iterator iterator=parameters.iterator(); iterator.hasNext(); ) {
        SourceBean att=(SourceBean)iterator.next();
        String name=(String)att.getAttribute("name");
        String type=(String)att.getAttribute("type");
        String value=(String)att.getAttribute("value");
        if (type != null && type.equalsIgnoreCase("absolute"))         type="absolute";
 else         type="relative";
        if (name.equalsIgnoreCase("seriesurlname"))         serieUrlname=value;
 else         if (name.equalsIgnoreCase("target")) {
          if (value != null && value.equalsIgnoreCase("tab")) {
            setTarget("tab");
          }
 else {
            setTarget("self");
          }
        }
 else         if (name.equalsIgnoreCase("title")) {
          if (value != null && !value.equals("")) {
            setDrillDocTitle(value);
          }
        }
 else         if (name.equalsIgnoreCase("categoryurlname"))         categoryUrlName=value;
 else {
          if (this.getParametersObject().get(name) != null) {
            value=(String)getParametersObject().get(name);
          }
          DrillParameter drillPar=new DrillParameter(name,type,value);
          drillParametersMap.put(name,drillPar);
        }
      }
    }
  }
  SourceBean colors=(SourceBean)content.getAttribute("SERIES_COLORS");
  if (colors == null) {
    colors=(SourceBean)content.getAttribute("CONF.SERIES_COLORS");
  }
  if (colors != null) {
    colorMap=new HashMap();
    List atts=colors.getContainedAttributes();
    String colorSerie="";
    for (Iterator iterator=atts.iterator(); iterator.hasNext(); ) {
      SourceBeanAttribute object=(SourceBeanAttribute)iterator.next();
      String serieName=new String(object.getKey());
      String nameRinominated=(seriesLabelsMap != null && seriesLabelsMap.containsKey(serieName)) ? seriesLabelsMap.get(serieName).toString() : serieName;
      colorSerie=new String((String)object.getValue());
      Color col=new Color(Integer.decode(colorSerie).intValue());
      if (col != null) {
        colorMap.put(nameRinominated,col);
      }
    }
  }
  logger.debug("OUT");
}

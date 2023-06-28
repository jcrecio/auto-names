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
  configureChart_extraction_1();
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
    configureChart_extraction_2(drillSB);
  }
  SourceBean colors=(SourceBean)content.getAttribute("SERIES_COLORS");
  if (colors == null) {
    colors=(SourceBean)content.getAttribute("CONF.SERIES_COLORS");
  }
  configureChart_extraction_4(colors);
  logger.debug("OUT");
}

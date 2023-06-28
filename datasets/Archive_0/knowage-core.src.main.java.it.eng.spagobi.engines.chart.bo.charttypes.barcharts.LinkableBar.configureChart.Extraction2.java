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
        configureChart_extraction_1(name,type,value);
      }
    }
  }
  logger.debug("OUT");
}

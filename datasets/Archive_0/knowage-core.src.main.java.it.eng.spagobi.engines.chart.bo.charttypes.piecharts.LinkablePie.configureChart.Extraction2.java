public void configureChart(SourceBean content){
  super.configureChart(content);
  if (confParameters.get("dimensions") != null) {
    String orientation=(String)confParameters.get("dimensions");
    if (orientation.equalsIgnoreCase("3D")) {
      threeD=true;
      isThreedViewConfigured=true;
    }
 else     if (orientation.equalsIgnoreCase("2D")) {
      threeD=false;
      isThreedViewConfigured=true;
    }
  }
  if (confParameters.get("values") != null) {
    configureChart_extraction_1();
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
        configureChart_extraction_2(name,type,value);
      }
    }
  }
}

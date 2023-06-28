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
  logger.debug("OUT");
}

public void configureChart(SourceBean content){
  super.configureChart(content);
  logger.debug("IN");
  if (confParameters.get("add_labels") != null) {
    String additional=(String)confParameters.get("add_labels");
    if (additional.equalsIgnoreCase("true")) {
      additionalLabels=true;
      catSerLabels=new LinkedHashMap();
    }
 else     additionalLabels=false;
  }
 else {
    additionalLabels=false;
  }
  if (confParameters.get("add_labels") != null) {
    String additional=(String)confParameters.get("add_labels");
    if (additional.equalsIgnoreCase("true")) {
      additionalLabels=true;
      catSerLabels=new LinkedHashMap();
    }
 else     additionalLabels=false;
  }
 else {
    additionalLabels=false;
  }
  SourceBean draws=(SourceBean)content.getAttribute("SERIES_DRAW");
  if (draws == null) {
    draws=(SourceBean)content.getAttribute("CONF.SERIES_DRAW");
  }
  seriesDraw=new LinkedHashMap();
  if (draws != null) {
    List atts=draws.getContainedAttributes();
    String serieName="";
    String serieDraw="";
    for (Iterator iterator=atts.iterator(); iterator.hasNext(); ) {
      SourceBeanAttribute object=(SourceBeanAttribute)iterator.next();
      serieName=new String(object.getKey());
      serieDraw=new String((String)object.getValue());
      if (serieDraw.equalsIgnoreCase("line")) {
        seriesDraw.put(serieName,"line");
      }
 else       if (serieDraw.equalsIgnoreCase("line_no_shapes")) {
        seriesDraw.put(serieName,"line_no_shapes");
      }
 else {
        seriesDraw.put(serieName,"bar");
      }
    }
  }
  if (confParameters.get("second_axis_label") != null) {
    secondAxisLabel=(String)confParameters.get("second_axis_label");
  }
  SourceBean scales=(SourceBean)content.getAttribute("SERIES_SCALES");
  if (scales == null) {
    scales=(SourceBean)content.getAttribute("CONF.SERIES_SCALES");
  }
  seriesScale=new LinkedHashMap();
  if (scales != null) {
    List attsScales=scales.getContainedAttributes();
    String serieName="";
    Integer serieScale=1;
    for (Iterator iterator=attsScales.iterator(); iterator.hasNext(); ) {
      SourceBeanAttribute object=(SourceBeanAttribute)iterator.next();
      serieName=new String(object.getKey());
      configureChart_extraction_2(serieName,object);
    }
  }
  logger.debug("OUT");
}

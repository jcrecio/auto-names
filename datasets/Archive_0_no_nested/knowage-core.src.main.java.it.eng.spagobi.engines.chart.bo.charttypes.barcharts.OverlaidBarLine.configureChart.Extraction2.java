public void configureChart(SourceBean content){
  super.configureChart(content);
  logger.debug("IN");
  configureChart_extraction_1();
  if (confParameters.get("stacked_bar_renderer_2") != null) {
    String stacked=(String)confParameters.get("stacked_bar_renderer_2");
    if (stacked.equalsIgnoreCase("true")) {
      stackedBarRenderer_2=true;
    }
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
 else {
    useBars=true;
  }
  if (confParameters.get(SECOND_AXIS_LABEL) != null && !confParameters.get(SECOND_AXIS_LABEL).equals("")) {
    secondAxis=true;
    configureChart_extraction_2(content);
  }
  logger.debug("OUT");
}

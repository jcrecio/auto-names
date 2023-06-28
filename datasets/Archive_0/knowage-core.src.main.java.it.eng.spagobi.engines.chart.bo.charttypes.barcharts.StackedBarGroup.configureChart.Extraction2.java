@Override public void configureChart(SourceBean content){
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
  if (confParameters.get(SUBCATEGORY_LABEL) != null) {
    subCategoryLabel=(String)confParameters.get(SUBCATEGORY_LABEL);
  }
 else {
    subCategoryLabel="";
  }
  configureChart_extraction_1();
  if (confParameters.get("n_groups") != null) {
    numGroups=Integer.valueOf((String)confParameters.get("n_groups"));
  }
 else {
    numGroups=new Integer("1");
  }
  configureChart_extraction_2(content);
  SourceBean subcatLabels=(SourceBean)content.getAttribute("SUBCATEGORY_LABELS");
  if (subcatLabels == null) {
    subcatLabels=(SourceBean)content.getAttribute("CONF.SUBCATEGORY_LABELS");
  }
  if (subcatLabels != null) {
    subCatLabelsMap=new LinkedHashMap();
    List atts=subcatLabels.getContainedAttributes();
    String label="";
    for (Iterator iterator=atts.iterator(); iterator.hasNext(); ) {
      SourceBeanAttribute object=(SourceBeanAttribute)iterator.next();
      String subcatName=new String(object.getKey());
      label=new String((String)object.getValue());
      if (label != null) {
        subCatLabelsMap.put(subcatName,label);
      }
    }
  }
  logger.debug("OUT");
}

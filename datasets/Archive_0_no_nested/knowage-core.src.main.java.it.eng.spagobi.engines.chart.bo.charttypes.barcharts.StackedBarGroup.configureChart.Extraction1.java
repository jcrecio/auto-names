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
  if (confParameters.get(ADD_LABELS) != null) {
    String additional=(String)confParameters.get(ADD_LABELS);
    if (additional.equalsIgnoreCase("true")) {
      additionalLabels=true;
      catSerLabels=new LinkedHashMap();
    }
 else     additionalLabels=false;
  }
 else {
    additionalLabels=false;
  }
  if (confParameters.get("percentage_value") != null) {
    String perc=(String)confParameters.get("percentage_value");
    if (perc.equalsIgnoreCase("true")) {
      percentageValue=true;
    }
 else     percentageValue=false;
  }
 else {
    percentageValue=false;
  }
  if (confParameters.get("n_serie_for_group") != null) {
    numSerieForGroup=Integer.valueOf((String)confParameters.get("n_serie_for_group"));
  }
 else {
    numSerieForGroup=new Integer("1");
  }
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

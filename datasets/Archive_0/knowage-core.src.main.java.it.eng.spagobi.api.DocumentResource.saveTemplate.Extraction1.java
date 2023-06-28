private void saveTemplate(String docLabel,String xml){
  AnalyticalModelDocumentManagementAPI documentManager=new AnalyticalModelDocumentManagementAPI(getUserProfile());
  ObjTemplate template=new ObjTemplate();
  template.setName("Template.json");
  template.setContent(xml.getBytes());
  template.setDimension(Long.toString(xml.getBytes().length / 1000) + " KByte");
  ArrayList<String> categoriesNames=new ArrayList<String>();
  String[] allSpecificChartTypes={"PARALLEL","WORDCLOUD","CHORD","GAUGE"};
  List specificChartTypes=Arrays.asList(allSpecificChartTypes);
  String specificChartType="";
  try {
    JSONObject obj=new JSONObject(Xml.xml2json(xml));
    Iterator keys=obj.keys();
    JSONArray jaCategories=new JSONArray();
    while (keys.hasNext()) {
      String key=(String)keys.next();
      if (key.equals("CHART") || key.equals("VALUES")) {
        obj=(JSONObject)obj.opt(key);
        keys=obj.keys();
        if (key.equals("CHART")) {
          if (specificChartTypes.indexOf(obj.opt("type").toString()) >= 0) {
            specificChartType=(String)specificChartTypes.get(specificChartTypes.indexOf(obj.opt("type").toString()));
            break;
          }
 else           if (!obj.opt("type").toString().equals("SUNBURST")) {
            break;
          }
        }
      }
 else       if (key.equals("CATEGORY")) {
        jaCategories=obj.optJSONArray(key);
        saveTemplate_extraction_2(categoriesNames,jaCategories);
        logger.info("Category names for the SUNBURST document are: " + categoriesNames);
      }
    }
  }
 catch (  JSONException e1) {
    e1.printStackTrace();
  }
catch (  TransformerFactoryConfigurationError e1) {
    e1.printStackTrace();
  }
catch (  TransformerException e1) {
    e1.printStackTrace();
  }
  try {
    IBIObjectDAO biObjectDao;
    BIObject document;
    biObjectDao=DAOFactory.getBIObjectDAO();
    try {
      int docId=Integer.parseInt(docLabel);
      document=biObjectDao.loadBIObjectById(docId);
      if (document == null) {
        logger.debug("The document identifier is an Integer, but no document is found with such identifier. Trying with with it as a String.");
        document=biObjectDao.loadBIObjectByLabel(docLabel);
      }
    }
 catch (    NumberFormatException e) {
      logger.debug("The document identifier is not an Integer.");
      document=biObjectDao.loadBIObjectByLabel(docLabel);
    }
    Assert.assertNotNull(document,"Document identifier or label cannot be null");
    if (!categoriesNames.isEmpty()) {
      documentManager.saveDocument(document,template,categoriesNames);
    }
 else     if (!specificChartType.equals("")) {
      documentManager.saveDocument(document,template,specificChartType);
    }
 else {
      documentManager.saveDocument(document,template);
    }
  }
 catch (  EMFUserError e) {
    logger.error("Error saving JSON Template to XML...",e);
    throw new SpagoBIServiceException(this.request.getPathInfo(),"An unexpected error occured while executing service",e);
  }
}

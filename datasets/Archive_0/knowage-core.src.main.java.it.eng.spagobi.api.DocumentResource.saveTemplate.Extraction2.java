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
    specificChartType=saveTemplate_extraction_1(xml,categoriesNames,specificChartTypes,specificChartType);
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

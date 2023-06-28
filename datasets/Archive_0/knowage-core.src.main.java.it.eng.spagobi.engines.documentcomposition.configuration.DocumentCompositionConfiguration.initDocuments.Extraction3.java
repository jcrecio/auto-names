private void initDocuments(SourceBean documentsConfigurationSB){
  logger.debug("IN");
  Document document;
  String attributeValue;
  List documentList;
  List refreshDocList;
  List paramList;
  List styleList;
  SourceBean styleSB;
  SourceBean documentSB;
  SourceBean refreshSB;
  SourceBean dimensionSB;
  SourceBean parametersSB;
  SourceBean paramSB;
  SourceBean refreshDocLinkedSB;
  try {
    documentList=documentsConfigurationSB.getAttributeAsList(Constants.DOCUMENT);
    String videoWidthS=(documentsConfigurationSB.getAttribute(Constants.VIDEO_WIGTH) != null) ? documentsConfigurationSB.getAttribute(Constants.VIDEO_WIGTH).toString() : null;
    String videoHeightS=(documentsConfigurationSB.getAttribute(Constants.VIDEO_HEIGHT) != null) ? documentsConfigurationSB.getAttribute(Constants.VIDEO_HEIGHT).toString() : null;
    if (videoWidthS != null & videoHeightS != null) {
      videoWidth=Integer.valueOf(videoWidthS);
      videoHeight=Integer.valueOf(videoHeightS);
    }
 else {
      videoWidth=DEFAULT_WIDTH;
      videoHeight=DEFAULT_HEIGHT;
    }
    for (int i=0; i < documentList.size(); i++) {
      documentSB=(SourceBean)documentList.get(i);
      document=new Document();
      document.setNumOrder(i);
      attributeValue=(String)documentSB.getAttribute(Constants.SBI_OBJ_LABEL);
      document.setSbiObjLabel(attributeValue);
      String snap=(String)documentSB.getAttribute(Constants.SNAPSHOT);
      document.setSnapshot(Boolean.valueOf(snap));
      BIObject obj=DAOFactory.getBIObjectDAO().loadBIObjectByLabel(attributeValue);
      if (obj == null) {
        logger.error("Document with label " + attributeValue + " doesn't exist in SpagoBI. Check the label!");
        continue;
      }
      String typeCD=obj.getBiObjectTypeCode();
      document.setType(typeCD);
      attributeValue=initDocuments_extraction_1(document,documentSB);
      document.setActiveExport(attributeValue);
      Integer width=(documentsConfigurationSB.getAttribute(Constants.VIDEO_WIGTH) == null) ? DEFAULT_WIDTH : Integer.valueOf((String)documentsConfigurationSB.getAttribute(Constants.VIDEO_WIGTH));
      Integer height=(documentsConfigurationSB.getAttribute(Constants.VIDEO_HEIGHT) == null) ? DEFAULT_HEIGHT : Integer.valueOf((String)documentsConfigurationSB.getAttribute(Constants.VIDEO_HEIGHT));
      document.setVideoWidth(getVideoDimensions("width",width));
      document.setVideoHeight(getVideoDimensions("height",height));
      dimensionSB=(SourceBean)documentSB.getAttribute(Constants.STYLE);
      attributeValue=(String)dimensionSB.getAttribute(Constants.DIMENSION_STYLE);
      document.setStyle(attributeValue);
      parametersSB=(SourceBean)documentSB.getAttribute(Constants.PARAMETERS);
      if (parametersSB != null) {
        initDocuments_extraction_2(document,parametersSB,i);
      }
      addDocument(document);
    }
  }
 catch (  Exception e) {
    logger.error("Error while initializing the document. ",e);
  }
  logger.debug("OUT");
}

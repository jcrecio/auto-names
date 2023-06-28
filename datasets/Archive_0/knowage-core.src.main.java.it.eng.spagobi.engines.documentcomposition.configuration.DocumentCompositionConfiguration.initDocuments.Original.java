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
      attributeValue=(documentSB.getAttribute(Constants.TITLE) == null) ? "" : (String)documentSB.getAttribute(Constants.TITLE);
      document.setTitle(attributeValue);
      attributeValue=(documentSB.getAttribute(Constants.EXPORT) == null) ? null : (String)documentSB.getAttribute(Constants.EXPORT);
      if (attributeValue == null) {
        attributeValue=(documentSB.getAttribute(Constants.EXPORT_DS) == null) ? "false" : (String)documentSB.getAttribute(Constants.EXPORT_DS);
      }
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
        paramList=parametersSB.getAttributeAsList(Constants.PARAMETER);
        Properties param=new Properties();
        for (int j=0; j < paramList.size(); j++) {
          paramSB=(SourceBean)paramList.get(j);
          String sbiParLabel=(paramSB.getAttribute(Constants.SBI_PAR_LABEL) == null) ? "" : (String)paramSB.getAttribute(Constants.SBI_PAR_LABEL);
          param.setProperty("sbi_par_label_param_" + i + "_"+ j,sbiParLabel);
          String typePar=(paramSB.getAttribute(Constants.TYPE) == null) ? "" : (String)paramSB.getAttribute(Constants.TYPE);
          param.setProperty("type_par_" + i + "_"+ j,typePar);
          String defaultValuePar=(paramSB.getAttribute(Constants.DEFAULT_VALUE) == null) ? "" : (String)paramSB.getAttribute(Constants.DEFAULT_VALUE);
          param.setProperty("default_value_param_" + i + "_"+ j,defaultValuePar);
          refreshSB=(SourceBean)paramSB.getAttribute(Constants.REFRESH);
          if (refreshSB != null) {
            refreshDocList=refreshSB.getAttributeAsList(Constants.REFRESH_DOC_LINKED);
            if (refreshDocList != null) {
              Properties paramRefreshLinked=new Properties();
              int k=0;
              for (k=0; k < refreshDocList.size(); k++) {
                refreshDocLinkedSB=(SourceBean)refreshDocList.get(k);
                String labelDoc=(refreshDocLinkedSB.getAttribute(Constants.LABEL_DOC) == null) ? "" : (String)refreshDocLinkedSB.getAttribute(Constants.LABEL_DOC);
                paramRefreshLinked.setProperty("refresh_doc_linked",labelDoc);
                String labelPar=(refreshDocLinkedSB.getAttribute(Constants.LABEL_PARAM) == null) ? "" : (String)refreshDocLinkedSB.getAttribute(Constants.LABEL_PARAM);
                paramRefreshLinked.setProperty("refresh_par_linked",labelPar);
                String defaultValueLinked=(paramSB.getAttribute(Constants.DEFAULT_VALUE) == null) ? "" : (String)paramSB.getAttribute(Constants.DEFAULT_VALUE);
                paramRefreshLinked.setProperty("default_value_linked",defaultValueLinked);
                String typeCrossPar=(refreshDocLinkedSB.getAttribute(Constants.TYPE_CROSS) == null) ? Constants.CROSS_INTERNAL : (String)refreshDocLinkedSB.getAttribute(Constants.TYPE_CROSS);
                paramRefreshLinked.setProperty("type_cross_linked",typeCrossPar);
                param.setProperty("param_linked_" + i + "_"+ j+ "_"+ k,paramRefreshLinked.toString());
              }
              param.setProperty("num_doc_linked_param_" + i + "_"+ j,new Integer(k).toString());
            }
          }
        }
        document.setParams(param);
      }
      addDocument(document);
    }
  }
 catch (  Exception e) {
    logger.error("Error while initializing the document. ",e);
  }
  logger.debug("OUT");
}

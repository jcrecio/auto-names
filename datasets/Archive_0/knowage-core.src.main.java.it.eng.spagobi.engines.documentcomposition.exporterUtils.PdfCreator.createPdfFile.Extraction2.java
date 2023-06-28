public FileOutputStream createPdfFile(FileOutputStream fileOutputStream,Map<String,DocumentContainer> documentsMap,boolean defaultStyle) throws MalformedURLException, IOException, DocumentException {
  logger.debug("IN");
  Document document=new Document(PageSize.A4.rotate());
  Rectangle rect=document.getPageSize();
  docWidth=rect.getWidth();
  docHeight=rect.getHeight();
  logger.debug("document size width: " + docWidth + " height: "+ docHeight);
  PdfWriter writer=PdfWriter.getInstance(document,fileOutputStream);
  document.open();
  int documentsNumber=documentsMap.keySet().size();
  int columnnsNumber=2;
  if (defaultStyle == true) {
    logger.debug("use default style");
    int cellsCounter=0;
    PdfPTable table=new PdfPTable(columnnsNumber);
    table.setWidthPercentage(100);
    for (Iterator iterator=documentsMap.keySet().iterator(); iterator.hasNext(); ) {
      String label=(String)iterator.next();
      DocumentContainer docContainer=documentsMap.get(label);
      byte[] content=docContainer.getContent();
      if (content != null) {
        Image img=null;
        try {
          img=Image.getInstance(content);
          table.addCell(img);
        }
 catch (        Exception e) {
          logger.debug("Trying to evaluate response as a PDF file... ");
          table.addCell("");
        }
      }
      cellsCounter++;
    }
    if (cellsCounter % 2 != 0) {
      table.addCell("");
    }
    document.add(table);
  }
 else {
    logger.debug("No default style");
    for (Iterator iterator=documentsMap.keySet().iterator(); iterator.hasNext(); ) {
      String label=(String)iterator.next();
      logger.debug("document with label " + label);
      DocumentContainer docContainer=documentsMap.get(label);
      MetadataStyle style=docContainer.getStyle();
      PdfPTable table=new PdfPTable(1);
      int widthStyle=style.getWidth();
      int heightStyle=style.getHeight();
      logger.debug("style for document width: " + widthStyle + " height: "+ heightStyle);
      int tableWidth=calculatePxSize(docWidth,widthStyle,videoWidth);
      int tableHeight=calculatePxSize(docHeight,heightStyle,videoHeight);
      logger.debug("table for document width: " + tableWidth + " height: "+ tableHeight);
      int yStyle=style.getY();
      int xStyle=style.getX();
      int xPos=(calculatePxPos(docWidth,xStyle,videoWidth));
      int yPos=(int)docHeight - (calculatePxPos(docHeight,yStyle,videoHeight));
      logger.debug("Table position at x: " + xPos + " y: "+ yPos);
      byte[] content=docContainer.getContent();
      createPdfFile_extraction_1(writer,label,docContainer,table,tableWidth,tableHeight,xPos,yPos,content);
    }
  }
  document.close();
  logger.debug("OUT");
  return fileOutputStream;
}

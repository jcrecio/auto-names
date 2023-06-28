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
      if (content != null) {
        Image img=null;
        try {
          img=Image.getInstance(content);
        }
 catch (        Exception e) {
          logger.debug("Trying to evaluate response as a PDF file... ");
          try {
            PdfReader reader=new PdfReader(content);
            PdfContentByte cb=writer.getDirectContent();
            PdfImportedPage page=writer.getImportedPage(reader,1);
            float[] tm=getTransformationMatrix(page,xPos,yPos,tableWidth,tableHeight);
            cb.addTemplate(page,tm[0],tm[1],tm[2],tm[3],tm[4],tm[5]);
          }
 catch (          Exception x) {
            logger.error("Error in inserting image for document " + label,e);
            logger.error("Error in inserting pdf file for document " + label,x);
          }
          continue;
        }
        if (docContainer.getDocumentType().equals("REPORT")) {
          boolean cutImageWIdth=isToCutWidth(img,tableWidth);
          boolean cutImageHeight=isToCutHeight(img,tableWidth);
          if (cutImageWIdth == true || cutImageHeight == true) {
            logger.debug("Report will be cut to width " + tableWidth + " and height "+ tableHeight);
            try {
              img=cutImage(content,cutImageHeight,cutImageWIdth,tableHeight,tableWidth,(int)img.getWidth(),(int)img.getHeight());
            }
 catch (            Exception e) {
              logger.error("Error in image cut, cutt will be ignored and image will be drawn anyway ",e);
            }
          }
        }
        int percToResize=percentageToResize((int)img.getWidth(),(int)img.getHeight(),tableWidth,tableHeight);
        logger.debug("image will be scaled of percentage " + percToResize);
        img.scalePercent(percToResize);
        PdfPCell cell=new PdfPCell(img);
        cell.setNoWrap(true);
        cell.setFixedHeight(tableHeight);
        cell.setBorderWidth(0);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        table.setTotalWidth(tableWidth);
        table.setLockedWidth(true);
      }
 else {
      }
      logger.debug("Add table");
      table.writeSelectedRows(0,-1,xPos,yPos,writer.getDirectContent());
      logger.debug("Document added");
    }
  }
  document.close();
  logger.debug("OUT");
  return fileOutputStream;
}

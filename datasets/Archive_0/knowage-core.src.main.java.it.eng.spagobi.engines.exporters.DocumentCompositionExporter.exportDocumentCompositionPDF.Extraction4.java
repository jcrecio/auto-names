public File exportDocumentCompositionPDF(File tmpFile,DocumentCompositionConfiguration dcConf,BIObject document,IEngUserProfile profile,Map<String,CurrentConfigurationDocComp> currentConfs,Map<String,DocumentContainer> documentsMap,boolean defaultStyle) throws Exception {
  logger.debug("IN");
  String output=null;
  InputStream svgInputStream=null;
  InputStream pngInputStream=null;
  OutputStream svgOutputStream=null;
  try {
    Map docMap=dcConf.getDocumentsMap();
    for (Iterator iterator=docMap.keySet().iterator(); iterator.hasNext(); ) {
      Object key=iterator.next();
      Document doc=(Document)docMap.get(key);
      String label=doc.getSbiObjLabel();
      logger.debug("Document " + label);
      DocumentContainer documentContainer=documentsMap.get(label);
      if (documentContainer == null)       continue;
      IBIObjectDAO dao=DAOFactory.getBIObjectDAO();
      BIObject objectID=dao.loadBIObjectByLabel(label);
      BIObject object=null;
      Collection roles=null;
      roles=((UserProfile)profile).getRolesForUse();
      object=exportDocumentCompositionPDF_extraction_1(dao,objectID,object,roles);
      logger.debug("fill parameters from URL");
      fillBIObjectWithParameterValues(object,currentConfs.get(label));
      byte[] returnByteArray=null;
      if (currentConfs.get("SVG_" + label) != null) {
        Map tmpSvg=currentConfs.get("SVG_" + label).getParameters();
        String tmpContent=tmpSvg.get("SVG_" + label).toString();
        String svg=tmpContent;
        svgInputStream=new ByteArrayInputStream(svg.getBytes("UTF-8"));
        File dir=new File(System.getProperty("java.io.tmpdir"));
        Random generator=new Random();
        int randomInt=generator.nextInt();
        File pdfFile=File.createTempFile(Integer.valueOf(randomInt).toString(),".pdf",dir);
        svgOutputStream=new FileOutputStream(pdfFile);
        ExportHighCharts.transformSVGIntoPDF(svgInputStream,svgOutputStream);
        pngInputStream=new FileInputStream(pdfFile);
        long length=pdfFile.length();
        if (length > Integer.MAX_VALUE) {
          logger.error("file too large");
          return null;
        }
        returnByteArray=new byte[(int)length];
        int offset=0;
        exportDocumentCompositionPDF_extraction_2(pngInputStream,returnByteArray,offset);
      }
 else       returnByteArray=exportDocumentCompositionPDF_extraction_3(profile,object,returnByteArray);
      logger.debug("add content retrieved to Document Container");
      if (returnByteArray != null) {
        if (returnByteArray.length == 0)         logger.warn("empty byte array retrieved for document " + label);
        documentContainer.setContent(returnByteArray);
        documentContainer.setDocumentLabel(label);
        documentContainer.setDocumentType(object.getBiObjectTypeCode());
      }
    }
    FileOutputStream fileOutputStream=new FileOutputStream(tmpFile);
    PdfCreator pdfCreator=new PdfCreator();
    logger.debug("Call PDF Creation");
    pdfCreator.setVideoHeight(dcConf.getVideoHeight());
    pdfCreator.setVideoWidth(dcConf.getVideoWidth());
    FileOutputStream pdfFile=pdfCreator.createPdfFile(fileOutputStream,documentsMap,defaultStyle);
    pdfFile.flush();
    pdfFile.close();
    logger.debug("OUT");
    return tmpFile;
  }
 catch (  Exception e) {
    logger.error(e);
    return null;
  }
 finally {
    exportDocumentCompositionPDF_extraction_4(svgInputStream,pngInputStream,svgOutputStream);
  }
}

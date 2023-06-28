/** 
 * Read template.
 * @param user     the user
 * @param document the document
 * @return the content
 * @throws SecurityException the security exception
 * @throws EMFUserError      the EMF user error
 * @throws EMFInternalError  the EMF internal error
 */
public Content readTemplate(String user,String document,Map<String,?> parameters) throws SecurityException, EMFUserError, EMFInternalError {
  Content content;
  BIObject biobj;
  logger.debug("IN");
  logger.debug("user: [" + user + "]");
  logger.debug("document: [" + document + "]");
  if (parameters == null) {
    logger.debug("Input parameters map is null. It will be considered as an empty map");
    parameters=new HashMap();
  }
  content=new Content();
  try {
    Integer id=new Integer(document);
    biobj=DAOFactory.getBIObjectDAO().loadBIObjectById(id);
    boolean checkNeeded=true;
    boolean modContained=parameters.containsKey("SBI_READ_ONLY_TEMPLATE");
    if (modContained) {
      boolean onlytemplate=parameters.containsValue("true");
      if (onlytemplate) {
        checkNeeded=false;
      }
    }
    if (checkNeeded && !UserProfile.isSchedulerUser(user) && !isSubReportCall(biobj,parameters)) {
      checkRequestCorrectness(user,biobj,parameters);
    }
    IObjTemplateDAO tempdao=DAOFactory.getObjTemplateDAO();
    ObjTemplate temp=tempdao.getBIObjectActiveTemplate(biobj.getId());
    if (temp == null) {
      logger.warn("The template dor document [" + id + "] is NULL");
      if (biobj.getEngine().getDriverName().equals(ChartDriver.class.getName()) || biobj.getEngine().getDriverName().equals(KpiDriver.class.getName())) {
        temp=new ObjTemplate();
      }
 else {
        throw new SecurityException("The template for document [" + id + "] is NULL");
      }
    }
    byte[] template=temp.getContent();
    if (biobj.getEngine().getUrl() != null && !"".equals(biobj.getEngine().getUrl())) {
      try {
        template=readTemplate_extraction_2(parameters,biobj,template);
      }
 catch (      Exception ex) {
        logger.error("Error while getting template: " + ex);
        return null;
      }
    }
    Base64.Encoder bASE64Encoder=Base64.getEncoder();
    if (template != null) {
      content.setContent(bASE64Encoder.encodeToString(template));
    }
 else {
      content.setContent("");
    }
    logger.debug("template read");
    content.setFileName(temp.getName());
  }
 catch (  NumberFormatException e) {
    logger.error("NumberFormatException",e);
    throw e;
  }
catch (  EMFUserError e) {
    logger.error("EMFUserError",e);
    throw e;
  }
catch (  EMFInternalError e) {
    logger.error("EMFUserError",e);
    throw e;
  }
 finally {
    logger.debug("OUT");
  }
  return content;
}

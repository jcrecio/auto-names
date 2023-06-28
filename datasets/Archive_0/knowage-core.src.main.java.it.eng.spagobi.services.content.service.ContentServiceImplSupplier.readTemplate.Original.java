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
        String driverClassName=biobj.getEngine().getDriverName();
        logger.warn("The driver used is [" + driverClassName + "]");
        IEngineDriver aEngineDriver=(IEngineDriver)Class.forName(driverClassName).newInstance();
        String language=(String)parameters.get(SpagoBIConstants.SBI_LANGUAGE);
        String country=(String)parameters.get(SpagoBIConstants.SBI_COUNTRY);
        if (language == null || country == null) {
          logger.debug("Not locale informations found in parameters... Not setted it at this time.");
        }
 else {
          logger.debug("Language retrieved: [" + language + "]; country retrieved: ["+ country+ "]");
          Locale locale=new Locale(language,country);
          aEngineDriver.applyLocale(locale);
        }
        logger.warn("Calling elaborateTemplate method defined into the driver ... ");
        if (biobj.getEngine().getDriverName().equals(ChartDriver.class.getName()) && template == null) {
          String emptyString="";
          template=emptyString.getBytes();
        }
        byte[] elabTemplate=aEngineDriver.ElaborateTemplate(template);
        logger.warn("Finished elaborateTemplate method defined into the driver. ");
        template=elabTemplate;
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

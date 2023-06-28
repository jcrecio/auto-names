public byte[] executeChart(String token,String userId,String label,HashMap parameters){
  logger.debug("IN");
  Monitor monitor=MonitorFactory.start("spagobi.service.execute.executeChart");
  logger.debug("Getting profile");
  byte[] returnImage=null;
  IEngUserProfile userProfile=null;
  try {
    userProfile=it.eng.spagobi.commons.utilities.GeneralUtilities.createNewUserProfile(userId);
  }
 catch (  Exception e2) {
    logger.error("Error recovering profile",e2);
    return "".getBytes();
  }
  logger.debug("Getting the chart object");
  IBIObjectDAO dao;
  BIObject obj=null;
  try {
    dao=DAOFactory.getBIObjectDAO();
    if (label != null)     obj=dao.loadBIObjectByLabel(label);
  }
 catch (  EMFUserError e) {
    logger.error("Error in recovering object",e);
    return "".getBytes();
  }
  if (obj != null) {
    logger.debug("Getting template");
    SourceBean content=null;
    byte[] contentBytes=null;
    try {
      ObjTemplate template=DAOFactory.getObjTemplateDAO().getBIObjectActiveTemplate(obj.getId());
      if (template == null)       throw new Exception("Active Template null");
      contentBytes=template.getContent();
      if (contentBytes == null)       throw new Exception("Content of the Active template null");
      String contentStr=new String(contentBytes);
      content=SourceBean.fromXMLString(contentStr);
    }
 catch (    Exception e) {
      logger.error("Error in reading template",e);
      return "".getBytes();
    }
    String type=content.getName();
    String subtype=(String)content.getAttribute("type");
    String data="";
    try {
      if (obj.getDataSetId() != null) {
        data=obj.getDataSetId().toString();
      }
 else {
        throw new Exception("Data Set not defined");
      }
    }
 catch (    Exception e) {
      logger.error("Error in reading dataset",e);
      return "".getBytes();
    }
    logger.debug("Getting parameters");
    HashMap parametersMap=null;
    List parametersList=null;
    try {
      parametersList=DAOFactory.getBIObjectDAO().getBIObjectParameters(obj);
    }
 catch (    EMFUserError e1) {
      logger.error("Error in retrieving parameters",e1);
      return "".getBytes();
    }
    parametersMap=executeChart_extraction_3(parameters,parametersList);
    logger.debug("Creating the chart");
    ChartImpl sbi=null;
    sbi=ChartImpl.createChart(type,subtype);
    sbi.setProfile(userProfile);
    sbi.setType(type);
    sbi.setSubtype(subtype);
    sbi.setData(data);
    sbi.setParametersObject(parametersMap);
    sbi.configureChart(content);
    DatasetMap datasets=null;
    try {
      datasets=sbi.calculateValue();
    }
 catch (    Exception e) {
      logger.error("Error in reading the value, check the dataset",e);
      return "".getBytes();
    }
    JFreeChart chart=null;
    chart=sbi.createChart(datasets);
    ByteArrayOutputStream out=null;
    try {
      logger.debug("Write PNG Image");
      out=new ByteArrayOutputStream();
      ChartUtilities.writeChartAsPNG(out,chart,sbi.getWidth(),sbi.getHeight());
      returnImage=out.toByteArray();
    }
 catch (    Exception e) {
      logger.error("Error while creating the image",e);
      return "".getBytes();
    }
 finally {
      executeChart_extraction_4(out);
      monitor.stop();
    }
  }
  return returnImage;
}

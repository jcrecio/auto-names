private void runInternal(){
  logger.debug("IN");
  ProgressThreadManager progressThreadManager=null;
  IObjMetadataDAO metaDAO=null;
  IObjMetacontentDAO contentDAO=null;
  Thread thread=Thread.currentThread();
  Long threadId=thread.getId();
  logger.debug("Started thread Id " + threadId + " from user id: "+ ((UserProfile)userProfile).getUserId());
  Integer totalDocs=documents.size();
  logger.debug("# of documents: " + totalDocs);
  progressThreadManager=new ProgressThreadManager();
  progressThreadManager.setStatusStarted(progressThreadId);
  try {
    metaDAO=DAOFactory.getObjMetadataDAO();
    contentDAO=DAOFactory.getObjMetacontentDAO();
  }
 catch (  Exception e) {
    logger.error("Error setting DAO");
    progressThreadManager.deleteThread(progressThreadId);
    throw new SpagoBIServiceException("Error setting DAO",e);
  }
  BIObject biObject=null;
  try {
    String userUniqueIdentifier=(String)userProfile.getUserUniqueIdentifier();
    ObjectMapper objectMapper=new ObjectMapper();
    String dossierTemplateJson=objectMapper.writeValueAsString(dossierTemplate);
    Map<String,String> imagesMap=null;
    Set<String> executedDocuments=new HashSet<String>();
    String path=SpagoBIUtilities.getResourcePath() + File.separator + "dossierExecution"+ File.separator;
    this.validImage(dossierTemplate.getReports());
    ISbiDossierActivityDAO daoAct=DAOFactory.getDossierActivityDao();
    daoAct.setUserProfile(userProfile);
    DossierActivity activity=null;
    while (activity == null) {
      activity=daoAct.loadActivityByProgressThreadId(progressThreadId);
    }
    String dbArray=activity.getConfigContent();
    JSONArray jsonArray=null;
    HashMap<String,String> paramMap=new HashMap<String,String>();
    JSONObject jsonObject=new JSONObject();
    jsonObject.put("TYPE","DOC_TEMPLATE");
    jsonObject.put("MESSAGE",dossierTemplate.getDocTemplate().getName());
    if (dbArray != null && !dbArray.isEmpty()) {
      jsonArray=new org.json.JSONArray(dbArray);
    }
 else {
      jsonArray=new JSONArray();
    }
    jsonArray.put(jsonObject);
    for (    Report reportToUse : dossierTemplate.getReports()) {
      String cockpitDocument=reportToUse.getLabel();
      String imageName=reportToUse.getImageName();
      logger.debug("executing post service to execute documents");
      biObject=DAOFactory.getBIObjectDAO().loadBIObjectByLabel(cockpitDocument);
      if (biObject == null) {
        throw new SpagoBIRuntimeException("Template error: the cockpit " + cockpitDocument + " doesn't exist, check the template");
      }
      Integer docId=biObject.getId();
      imagesMap=runInternal_extraction_1(progressThreadManager,biObject,userUniqueIdentifier,dossierTemplateJson,imagesMap,executedDocuments,path,jsonArray,paramMap,reportToUse,cockpitDocument,imageName,docId);
    }
    imageNames.clear();
    ParametersDecoder decoder=new ParametersDecoder();
    for (    Map.Entry<String,String> entry : paramMap.entrySet()) {
      String metadataMessage=entry.getKey() + "=" + decoder.decodeParameter(entry.getValue());
      jsonObject=new JSONObject();
      jsonObject.put("TYPE","PARAMETER");
      jsonObject.put("MESSAGE",metadataMessage);
      jsonArray.put(jsonObject);
    }
    activity.setConfigContent(jsonArray.toString());
    daoAct.updateActivity(activity);
    progressThreadManager.setStatusDownload(progressThreadId);
    logger.debug("Thread row in database set as download state");
    logger.debug("OUT");
  }
 catch (  Exception e) {
    progressThreadManager.setStatusError(progressThreadId);
    createErrorFile(biObject,e);
    logger.error("Error while creating dossier activity",e);
    throw new SpagoBIRuntimeException(e.getMessage(),e);
  }
 finally {
    logger.debug("OUT");
  }
}

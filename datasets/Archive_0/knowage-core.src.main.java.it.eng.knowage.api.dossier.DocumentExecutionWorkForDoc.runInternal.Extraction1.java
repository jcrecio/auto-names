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
      Collection<String> roles=userProfile.getRoles();
      for (      String role : roles) {
        if (!ObjectsAccessVerifier.canExec(biObject,userProfile)) {
          String message="user " + ((UserProfile)userProfile).getUserName() + " cannot execute document "+ biObject.getName();
          throw new SpagoBIRuntimeException(message);
        }
        String docName=biObject.getName();
        String hostUrl=getServiceHostUrl();
        StringBuilder serviceUrlBuilder=new StringBuilder();
        serviceUrlBuilder.append(hostUrl);
        serviceUrlBuilder.append("/knowagecockpitengine/api/1.0/pages/execute?");
        serviceUrlBuilder.append("user_id=");
        serviceUrlBuilder.append(userUniqueIdentifier);
        serviceUrlBuilder.append("&DOCUMENT_LABEL=");
        serviceUrlBuilder.append(cockpitDocument);
        serviceUrlBuilder.append("&DOCUMENT_OUTPUT_PARAMETERS=%5B%5D&DOCUMENT_IS_VISIBLE=true&SBI_EXECUTION_ROLE=");
        serviceUrlBuilder.append(URLEncoder.encode(role,StandardCharsets.UTF_8.toString()));
        serviceUrlBuilder.append("&DOCUMENT_DESCRIPTION=&document=");
        serviceUrlBuilder.append(docId);
        serviceUrlBuilder.append("&IS_TECHNICAL_USER=true&DOCUMENT_NAME=");
        serviceUrlBuilder.append(docName);
        serviceUrlBuilder.append("&NEW_SESSION=TRUE&SBI_ENVIRONMENT=DOCBROWSER&IS_FOR_EXPORT=true&documentMode=VIEW&export=true&outputType=PNG");
        RenderOptions renderOptions=RenderOptions.defaultOptions();
        runInternal_extraction_2(reportToUse,serviceUrlBuilder);
        if (reportToUse.getDeviceScaleFactor() != null && !reportToUse.getDeviceScaleFactor().isEmpty()) {
          serviceUrlBuilder.append("&pdfDeviceScaleFactor=" + Double.valueOf(reportToUse.getDeviceScaleFactor()));
        }
 else {
          serviceUrlBuilder.append("&pdfDeviceScaleFactor=" + Double.valueOf(renderOptions.getDimensions().getDeviceScaleFactor()));
        }
        String serviceUrl=addParametersToServiceUrl(progressThreadId,biObject,reportToUse,serviceUrlBuilder,jsonArray,paramMap);
        if (executedDocuments.contains(serviceUrl)) {
          progressThreadManager.incrementPartial(progressThreadId);
          break;
        }
        Response images=executePostService(null,serviceUrl,userUniqueIdentifier,MediaType.TEXT_HTML,dossierTemplateJson);
        byte[] responseAsByteArray=images.readEntity(byte[].class);
        List<Object> list=images.getMetadata().get("Content-Type");
        Iterator<Object> it=list.iterator();
        boolean isZipped=false;
        while (it.hasNext()) {
          String contentType=(String)it.next();
          isZipped=contentType.contains("application/zip");
          break;
        }
        imagesMap=new HashMap<String,String>();
        if (isZipped) {
          String message="Document has more than one single sheet. Screenshot is replaced with an empty image.";
          logger.debug(message);
          handleAllPicturesFromZipFile(responseAsByteArray,randomKey,imagesMap,reportToUse);
        }
 else {
          File f=FileUtilities.createFile(imageName,".png",randomKey,new ArrayList<PlaceHolder>());
          FileOutputStream outputStream=new FileOutputStream(f);
          outputStream.write(responseAsByteArray);
          outputStream.close();
          imagesMap.put(imageName,path);
        }
        progressThreadManager.incrementPartial(progressThreadId);
        logger.debug("progress Id incremented");
        executedDocuments.add(serviceUrl);
        break;
      }
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

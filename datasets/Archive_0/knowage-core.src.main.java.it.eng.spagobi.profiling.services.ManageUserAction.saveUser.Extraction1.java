protected void saveUser(ISbiUserDAO userDao){
  UserProfile profile=(UserProfile)this.getUserProfile();
  boolean insertModality=true;
  HashMap<String,String> logParam=new HashMap();
  try {
    Integer id=getAttributeAsInteger(ID);
    if (id != null && id > 0) {
      insertModality=false;
      SbiUser user=userDao.loadSbiUserById(id);
      if (user != null) {
        this.checkIfCurrentUserIsAbleToSaveOrModifyUser(user);
      }
 else {
        logParam.put("USER ID",id.toString());
        AuditLogUtilities.updateAudit(getHttpRequest(),profile,"PROF_USERS.MODIFY",logParam,"KO");
        throw new SpagoBIServiceException(SERVICE_NAME,"User with id = " + user + " does not exists or he belongs to another tenant");
      }
    }
    String userId=getAttributeAsString(USER_ID);
    String fullName=getAttributeAsString(FULL_NAME);
    String password=getAttributeAsString(PASSWORD);
    logParam.put("FULLNAME",fullName);
    if (userId == null) {
      AuditLogUtilities.updateAudit(getHttpRequest(),profile,"PROF_USERS." + ((insertModality) ? "ADD" : "MODIFY"),logParam,"KO");
      logger.error("User name missing");
      throw new SpagoBIServiceException(SERVICE_NAME,"User name missing");
    }
    SbiUser user=new SbiUser();
    if (id != null) {
      user.setId(id);
    }
    user.setUserId(userId);
    saveUser_extraction_2(profile,insertModality,logParam,fullName,password,user);
    checkUserId(userId,id);
    try {
      id=userDao.fullSaveOrUpdateSbiUser(user);
      CommunityManager cm=new CommunityManager();
      String commName=getCommunityAttr(user);
      if (commName != null && !commName.equals("")) {
        SbiCommunity community=DAOFactory.getCommunityDAO().loadSbiCommunityByName(commName);
        cm.saveCommunity(community,commName,userId,getHttpRequest());
      }
      logger.debug("User updated or Inserted");
    }
 catch (    Throwable t) {
      AuditLogUtilities.updateAudit(getHttpRequest(),profile,"PROF_USERS." + ((insertModality) ? "ADD" : "MODIFY"),logParam,"KO");
      logger.error("Exception occurred while saving user",t);
      throw new SpagoBIServiceException(SERVICE_NAME,"Exception occurred while saving user",t);
    }
    try {
      JSONObject attributesResponseSuccessJSON=new JSONObject();
      attributesResponseSuccessJSON.put("success",true);
      attributesResponseSuccessJSON.put("responseText","Operation succeded");
      attributesResponseSuccessJSON.put("id",id);
      writeBackToClient(new JSONSuccess(attributesResponseSuccessJSON));
      AuditLogUtilities.updateAudit(getHttpRequest(),profile,"PROF_USERS." + ((insertModality) ? "ADD" : "MODIFY"),logParam,"OK");
    }
 catch (    Exception e) {
      AuditLogUtilities.updateAudit(getHttpRequest(),profile,"PROF_USERS." + ((insertModality) ? "ADD" : "MODIFY"),logParam,"KO");
      throw new SpagoBIServiceException(SERVICE_NAME,"Impossible to write back the responce to the client",e);
    }
  }
 catch (  SpagoBIServiceException e) {
    try {
      AuditLogUtilities.updateAudit(getHttpRequest(),profile,"PROF_USERS." + ((insertModality) ? "ADD" : "MODIFY"),logParam,"ERR");
    }
 catch (    Exception e1) {
      e1.printStackTrace();
    }
    throw e;
  }
catch (  Throwable e) {
    try {
      AuditLogUtilities.updateAudit(getHttpRequest(),profile,"PROF_USERS" + ((insertModality) ? "ADD" : "MODIFY"),logParam,"ERR");
    }
 catch (    Exception e1) {
      e1.printStackTrace();
    }
    logger.error("Exception occurred while saving user",e);
    throw new SpagoBIServiceException(SERVICE_NAME,"Exception occurred while saving user",e);
  }
}

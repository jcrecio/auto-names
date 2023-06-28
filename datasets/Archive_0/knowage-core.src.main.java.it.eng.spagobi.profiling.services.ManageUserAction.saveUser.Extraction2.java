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
    saveUser_extraction_1(userDao,profile,insertModality,logParam,id);
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

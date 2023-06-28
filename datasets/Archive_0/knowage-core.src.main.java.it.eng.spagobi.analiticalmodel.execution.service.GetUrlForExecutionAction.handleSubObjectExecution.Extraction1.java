protected JSONObject handleSubObjectExecution(Integer subObjectId,boolean isFromCross){
  logger.debug("IN");
  ExecutionInstance executionInstance=getContext().getExecutionInstance(ExecutionInstance.class.getName());
  UserProfile userProfile=(UserProfile)this.getUserProfile();
  BIObject obj=executionInstance.getBIObject();
  HashMap<String,String> logParam=new HashMap();
  logParam.put("DOCUMENT NAME",obj.getName());
  logParam.put("PARAMS",this.getAttributeAsString(PARAMETERS));
  JSONObject response=new JSONObject();
  ISubObjectDAO dao=DAOFactory.getSubObjectDAO();
  SubObject subObject=null;
  try {
    subObject=dao.getSubObject(subObjectId);
  }
 catch (  EMFUserError e) {
    try {
      logParam.put("SUBOBJECT ID",subObjectId.toString());
      AuditLogUtilities.updateAudit(getHttpRequest(),userProfile,"DOCUMENT.GET_URL_FOR_SUBOBJ",logParam,"ERR");
    }
 catch (    Exception e1) {
      e1.printStackTrace();
    }
    logger.error("SubObject with id = " + subObjectId + " not found",e);
    throw new SpagoBIServiceException(SERVICE_NAME,"Customized view not found",e);
  }
  logParam.put("SUBOBJECT NAME",subObject.getName());
  try {
    executionInstance=getContext().getExecutionInstance(ExecutionInstance.class.getName());
    Assert.assertNotNull(executionInstance,"Execution instance cannot be null in order to properly generate execution url");
    executionInstance.setSnapshot(null);
    Locale locale=this.getLocale();
    List errors=null;
    JSONObject executionInstanceJSON=this.getAttributeAsJSONObject(PARAMETERS);
    executionInstance.refreshParametersValues(executionInstanceJSON,false);
    try {
      errors=executionInstance.getParametersErrors();
    }
 catch (    Exception e) {
      throw new SpagoBIServiceException(SERVICE_NAME,"Cannot evaluate errors on parameters validation",e);
    }
    if (errors != null && errors.size() > 0) {
      JSONArray errorsArray=new JSONArray();
      Iterator errorsIt=errors.iterator();
      while (errorsIt.hasNext()) {
        EMFUserError error=(EMFUserError)errorsIt.next();
        errorsArray.put(error.getDescription());
      }
      try {
        response.put("errors",errorsArray);
      }
 catch (      JSONException e) {
        throw new SpagoBIServiceException(SERVICE_NAME,"Cannot serialize errors to the client",e);
      }
    }
 else {
      if (obj.getId().equals(subObject.getBiobjId())) {
        boolean canExecuteSubObject=false;
        if (userProfile.isAbleToExecuteAction(SpagoBIConstants.DOCUMENT_MANAGEMENT_ADMIN)) {
          canExecuteSubObject=true;
        }
 else {
          if (subObject.getIsPublic() || subObject.getOwner().equals(userProfile.getUserId().toString())) {
            canExecuteSubObject=true;
          }
        }
        handleSubObjectExecution_extraction_2(isFromCross,executionInstance,userProfile,logParam,response,subObject,locale,canExecuteSubObject);
      }
 else {
        try {
          AuditLogUtilities.updateAudit(getHttpRequest(),userProfile,"DOCUMENT.GET_URL_FOR_SUBOBJ",logParam,"KO");
        }
 catch (        Exception e1) {
          e1.printStackTrace();
        }
        throw new SpagoBIServiceException(SERVICE_NAME,"Required subobject is not relevant to current document");
      }
    }
  }
 catch (  EMFInternalError e) {
    try {
      AuditLogUtilities.updateAudit(getHttpRequest(),userProfile,"DOCUMENT.GET_URL_FOR_SUBOBJ",logParam,"ERR");
    }
 catch (    Exception e1) {
      e1.printStackTrace();
    }
    throw new SpagoBIServiceException(SERVICE_NAME,"An internal error has occured",e);
  }
 finally {
    logger.debug("OUT");
  }
  try {
    AuditLogUtilities.updateAudit(getHttpRequest(),userProfile,"DOCUMENT.GET_URL_FOR_SUBOBJ",logParam,"OK");
  }
 catch (  Exception e) {
    e.printStackTrace();
  }
  return response;
}

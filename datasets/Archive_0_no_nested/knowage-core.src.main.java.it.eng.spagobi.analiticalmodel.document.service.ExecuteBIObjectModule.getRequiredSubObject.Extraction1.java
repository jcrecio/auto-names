/** 
 * Find the subobject with the name specified by the attribute "LABEL_SUB_OBJECT" or "SUBOBJECT_ID" on request among the list of subobjects in input (that must be the current document's subobjects list); if those attributes are missing, null is returned. If such a subobject does not exist or the current user is not able to see that subobject, an error is added into the Error Handler and null is returned.
 * @param request The service request
 * @param subObjects The list of all existing subobjects for the current document
 * @return the required subobject
 */
private SubObject getRequiredSubObject(SourceBean request,List subObjects){
  logger.debug("IN");
  SubObject subObj=null;
  String subObjectName=(String)request.getAttribute(SpagoBIConstants.SUBOBJECT_NAME);
  String subObjectIdStr=(String)request.getAttribute(SpagoBIConstants.SUBOBJECT_ID);
  if (subObjectName == null && subObjectIdStr == null) {
    logger.debug("Neither LABEL_SUB_OBJECT nor SUBOBJECT_ID attribute in request are specified. Returning null.");
    return null;
  }
  if (subObjectName != null) {
    logger.debug("Looking for subobject with name [" + subObjectName + "] ...");
    if (subObjects != null && subObjects.size() > 0) {
      Iterator iterSubs=subObjects.iterator();
      while (iterSubs.hasNext() && subObj == null) {
        SubObject sd=(SubObject)iterSubs.next();
        if (sd.getName().equals(subObjectName.trim())) {
          subObj=sd;
          break;
        }
      }
    }
  }
 else {
    logger.debug("Looking for subobject with id [" + subObjectIdStr + "] ...");
    Integer subObjId=new Integer(subObjectIdStr);
    if (subObjects != null && subObjects.size() > 0) {
      subObj=getRequiredSubObject_extraction_2(subObjects,subObj,subObjId);
    }
  }
  if (subObj == null) {
    logger.error("Subobject not found.");
    List l=new ArrayList();
    l.add(subObjectName);
    EMFUserError userError=new EMFUserError(EMFErrorSeverity.ERROR,1080,l);
    errorHandler.addError(userError);
  }
 else {
    boolean canSeeSubobject=canSeeSubobject(getUserProfile(),subObj);
    if (!canSeeSubobject) {
      List l=new ArrayList();
      l.add(subObj.getName());
      EMFUserError userError=new EMFUserError(EMFErrorSeverity.ERROR,1079,l);
      errorHandler.addError(userError);
      subObj=null;
    }
  }
  logger.debug("OUT");
  return subObj;
}

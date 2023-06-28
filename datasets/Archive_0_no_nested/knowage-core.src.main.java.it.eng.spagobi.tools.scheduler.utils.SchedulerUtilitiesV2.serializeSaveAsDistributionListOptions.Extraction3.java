private static String serializeSaveAsDistributionListOptions(DispatchContext dispatchContext,String uniqueDispatchContextName,JobTrigger triggerInfo,boolean runImmediately,IEngUserProfile profile) throws EMFUserError {
  String saveOptString="";
  JobInfo jobInfo=triggerInfo.getJobInfo();
  if (dispatchContext.isDistributionListDispatchChannelEnabled()) {
    String xml="";
    if (!runImmediately) {
      xml=serializeSaveAsDistributionListOptions_extraction_1(triggerInfo,jobInfo,xml);
      xml+=">";
      String params="<PARAMETERS>";
      List biObjects=jobInfo.getDocuments();
      Iterator iterbiobj=biObjects.iterator();
      int index=0;
      while (iterbiobj.hasNext()) {
        index++;
        BIObject biobj=(BIObject)iterbiobj.next();
        String objpref=biobj.getId().toString() + "__" + new Integer(index).toString();
        if (uniqueDispatchContextName.equals(objpref)) {
          String queryString=serializeSaveAsDistributionListOptions_extraction_2(biobj);
          params+="<PARAMETER name=\"" + biobj.getLabel() + "__"+ index+ "\" value=\""+ queryString+ "\" />";
        }
 else {
          continue;
        }
      }
      params+="</PARAMETERS>";
      xml+=params;
      xml+="</SCHEDULE>";
    }
    saveOptString+="sendtodl=true%26";
    List l=dispatchContext.getDlIds();
    if (!l.isEmpty()) {
      String dlIds="dlId=";
      int objId=dispatchContext.getBiobjId();
      dlIds=serializeSaveAsDistributionListOptions_extraction_3(runImmediately,profile,xml,l,dlIds,objId);
      saveOptString+=dlIds + "%26";
    }
  }
  return saveOptString;
}

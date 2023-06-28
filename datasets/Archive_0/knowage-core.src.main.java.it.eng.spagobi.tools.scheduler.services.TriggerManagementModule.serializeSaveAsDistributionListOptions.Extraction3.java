private String serializeSaveAsDistributionListOptions(DispatchContext dispatchContext,String uniqueDispatchContextName,TriggerInfo triggerInfo,boolean runImmediately,IEngUserProfile profile) throws EMFUserError {
  String saveOptString="";
  JobInfo jobInfo=triggerInfo.getJobInfo();
  if (dispatchContext.isDistributionListDispatchChannelEnabled()) {
    String xml="";
    if (!runImmediately) {
      xml+="<SCHEDULE ";
      xml+=" jobName=\"" + jobInfo.getJobName() + "\" ";
      xml+=" triggerName=\"" + triggerInfo.getTriggerName() + "\" ";
      xml+=" startDate=\"" + triggerInfo.getStartDate() + "\" ";
      xml+=" startTime=\"" + triggerInfo.getStartTime() + "\" ";
      xml+=" chronString=\"" + triggerInfo.getChronString() + "\" ";
      String enddate=triggerInfo.getEndDate();
      xml=serializeSaveAsDistributionListOptions_extraction_1(uniqueDispatchContextName,triggerInfo,jobInfo,xml,enddate);
    }
    saveOptString+="sendtodl=true%26";
    List l=dispatchContext.getDlIds();
    if (!l.isEmpty()) {
      String dlIds="dlId=";
      int objId=dispatchContext.getBiobjId();
      Iterator iter=l.iterator();
      saveOptString=serializeSaveAsDistributionListOptions_extraction_3(runImmediately,profile,saveOptString,xml,dlIds,objId,iter);
    }
  }
  return saveOptString;
}

private static String serializeSaveAsDistributionListOptions(DispatchContext dispatchContext,String uniqueDispatchContextName,JobTrigger triggerInfo,boolean runImmediately,IEngUserProfile profile) throws EMFUserError {
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
      xml+=" chronString=\"" + triggerInfo.getChrono() + "\" ";
      String enddate=triggerInfo.getEndDate();
      String endtime=triggerInfo.getEndTime();
      if (!enddate.trim().equals("")) {
        xml+=" endDate=\"" + enddate + "\" ";
        if (!endtime.trim().equals("")) {
          xml+=" endTime=\"" + endtime + "\" ";
        }
      }
      String zonedStartTime=triggerInfo.getZonedStartTime();
      if (zonedStartTime != null) {
        xml+=" zonedStartTime=\"" + zonedStartTime + "\" ";
      }
      String zonedEndTime=triggerInfo.getZonedEndTime();
      if (zonedEndTime != null) {
        xml+=" zonedEndTime=\"" + zonedEndTime + "\" ";
      }
      String repeatinterval=triggerInfo.getRepeatInterval();
      if (!repeatinterval.trim().equals("")) {
        xml+=" repeatInterval=\"" + repeatinterval + "\" ";
      }
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

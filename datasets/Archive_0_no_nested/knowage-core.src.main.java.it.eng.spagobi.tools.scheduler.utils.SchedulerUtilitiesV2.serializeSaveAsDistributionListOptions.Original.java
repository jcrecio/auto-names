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
          List pars=biobj.getDrivers();
          Iterator iterPars=pars.iterator();
          String queryString="";
          while (iterPars.hasNext()) {
            BIObjectParameter biobjpar=(BIObjectParameter)iterPars.next();
            String concatenatedValue="";
            List values=biobjpar.getParameterValues();
            if (values != null) {
              Iterator itervalues=values.iterator();
              while (itervalues.hasNext()) {
                String value=(String)itervalues.next();
                concatenatedValue+=value + ",";
              }
              if (concatenatedValue.length() > 0) {
                concatenatedValue=concatenatedValue.substring(0,concatenatedValue.length() - 1);
                queryString+=biobjpar.getParameterUrlName() + "=" + concatenatedValue+ "%26";
              }
            }
          }
          if (queryString.length() > 0) {
            queryString=queryString.substring(0,queryString.length() - 3);
          }
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
      Iterator iter=l.iterator();
      while (iter.hasNext()) {
        Integer dlId=(Integer)iter.next();
        try {
          if (!runImmediately) {
            IDistributionListDAO dao=DAOFactory.getDistributionListDAO();
            dao.setUserProfile(profile);
            DistributionList dl=dao.loadDistributionListById(dlId);
            dao.insertDLforDocument(dl,objId,xml);
          }
        }
 catch (        Exception ex) {
          throw new EMFUserError(EMFErrorSeverity.ERROR,100);
        }
        if (iter.hasNext()) {
          dlIds+=dlId.intValue() + ",";
        }
 else {
          dlIds+=dlId.intValue();
        }
      }
      saveOptString+=dlIds + "%26";
    }
  }
  return saveOptString;
}

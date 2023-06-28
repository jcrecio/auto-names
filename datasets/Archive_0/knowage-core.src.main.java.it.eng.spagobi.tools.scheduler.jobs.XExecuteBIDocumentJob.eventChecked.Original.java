private boolean eventChecked(JobExecutionContext jobExecutionContext) throws SchedulerException, JSONException {
  Boolean eventSolved=true;
  if (jobExecutionContext.getMergedJobDataMap().containsKey("event_info")) {
    eventSolved=false;
    String triggerName=jobExecutionContext.getTrigger().getName();
    if (jobExecutionContext.getMergedJobDataMap().containsKey("originalTriggerName")) {
      triggerName=jobExecutionContext.getMergedJobDataMap().getString("originalTriggerName");
    }
    JSONObject jo=new JSONObject(jobExecutionContext.getMergedJobDataMap().getString("event_info"));
    String typeEvent=jo.getString("type");
    if (typeEvent.equals("rest")) {
      SbiWsEventsDao wsEventsDao=DAOFactory.getWsEventsDao();
      List<SbiWsEvent> sbiWsEvents=wsEventsDao.loadSbiWsEvents(triggerName);
      if (sbiWsEvents.size() != 0) {
        Date attDate=new Date();
        for (        SbiWsEvent sb : sbiWsEvents) {
          if (sb.getTakeChargeDate() == null) {
            sb.setTakeChargeDate(attDate);
            wsEventsDao.updateEvent(sb);
          }
        }
        eventSolved=true;
      }
    }
 else     if (typeEvent.equals("dataset")) {
      IDataSetDAO d=DAOFactory.getDataSetDAO();
      d.setUserProfile(UserProfile.createSchedulerUserProfile());
      IDataSet dataSet=d.loadDataSetById(jo.getInt("dataset"));
      if (dataSet != null) {
        dataSet.loadData();
        IDataStore dataStore=dataSet.getDataStore();
        if (dataStore != null && dataStore.getRecordsCount() > 0) {
          IRecord returnVal=dataStore.getRecordAt(0);
          if (returnVal != null) {
            Object value=returnVal.getFieldAt(0).getValue();
            String execFlag=jobExecutionContext.getTrigger().getJobDataMap().getString("execFlag");
            Boolean exf=execFlag == null ? false : Boolean.parseBoolean(execFlag);
            boolean validDS=(value.toString().equals("1") || value.toString().equals("true")) ? true : false;
            if (validDS && !exf) {
              jobExecutionContext.getTrigger().getJobDataMap().put("execFlag","true");
              StdSchedulerFactory.getDefaultScheduler().rescheduleJob(jobExecutionContext.getTrigger().getName(),jobExecutionContext.getTrigger().getGroup(),jobExecutionContext.getTrigger());
              eventSolved=true;
            }
 else             if (!validDS && exf) {
              jobExecutionContext.getTrigger().getJobDataMap().put("execFlag","false");
              StdSchedulerFactory.getDefaultScheduler().rescheduleJob(jobExecutionContext.getTrigger().getName(),jobExecutionContext.getTrigger().getGroup(),jobExecutionContext.getTrigger());
            }
          }
        }
      }
    }
  }
  return eventSolved;
}

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
          eventSolved=eventChecked_extraction_2(jobExecutionContext,eventSolved,dataStore);
        }
      }
    }
  }
  return eventSolved;
}

public static JobTrigger getJobTriggerFromJsonRequest(JSONObject jsonObject,JSONArray jerr) throws Exception {
  JobTrigger jobTrigger=new JobTrigger();
  ISchedulerServiceSupplier schedulerService=SchedulerServiceSupplierFactory.getSupplier();
  String jobDetail=schedulerService.getJobDefinition((String)jsonObject.opt(JobTrigger.JOB_NAME),(String)jsonObject.opt(JobTrigger.JOB_GROUP));
  SourceBean jobDetailSB=SchedulerUtilities.getSBFromWebServiceResponse(jobDetail);
  if (jobDetailSB == null) {
    throw new Exception("Cannot recover job " + (String)jsonObject.opt(JobTrigger.JOB_NAME));
  }
  JobInfo jobInfo=SchedulerUtilities.getJobInfoFromJobSourceBean(jobDetailSB);
  jobTrigger.setJobInfo(jobInfo);
  jobTrigger.setTriggerName((String)jsonObject.opt(JobTrigger.TRIGGER_NAME));
  if (jobTrigger.getTriggerName() == null || jobTrigger.getTriggerName().trim().isEmpty()) {
    jerr.put("Empty name");
  }
  jobTrigger.setTriggerDescription((String)jsonObject.opt(JobTrigger.TRIGGER_DESCRIPTION));
  jobTrigger.setStartDate(jsonObject.optString(JobTrigger.START_DATE));
  jobTrigger.setStartTime(jsonObject.optString(JobTrigger.START_TIME));
  boolean validStartDate=true;
  boolean hasZonedStartTime=jsonObject.has(JobTrigger.ZONED_START_TIME);
  if (!hasZonedStartTime) {
    if (jobTrigger.getStartDate() == null || jobTrigger.getStartDate().trim().isEmpty()) {
      jerr.put("Null or not Valid Start date");
      validStartDate=false;
    }
    validStartDate=getJobTriggerFromJsonRequest_extraction_1(jerr,jobTrigger,validStartDate);
  }
  jobTrigger.setEndDate(jsonObject.optString(JobTrigger.END_DATE));
  jobTrigger.setEndTime(jsonObject.optString(JobTrigger.END_TIME));
  boolean hasZonedEndTime=jsonObject.has(JobTrigger.ZONED_END_TIME);
  if (!hasZonedEndTime) {
    if (validStartDate && (jobTrigger.getEndDate() != null && !jobTrigger.getEndDate().equals(""))) {
      boolean validTime=true;
      String[] tp=jobTrigger.getEndTime().split(":");
      int h=Integer.parseInt(tp[0]);
      getJobTriggerFromJsonRequest_extraction_2(jerr,jobTrigger,validTime,tp,h);
    }
  }
  jobTrigger.setZonedStartTime(jsonObject.optString(JobTrigger.ZONED_START_TIME));
  try {
    jobTrigger.setZonedEndTime(jsonObject.getString(JobTrigger.ZONED_END_TIME));
  }
 catch (  JSONException e) {
  }
  jobTrigger.setChrono(((JSONObject)jsonObject.opt(JobTrigger.CHRONO)).toString().replaceAll("\"","'"));
  JSONArray ja=(JSONArray)jsonObject.opt(JobTrigger.DOCUMENTS);
  Map<String,DispatchContext> saveOptions=getSaveOptionsFromRequest(ja,jerr);
  jobTrigger.setSaveOptions(saveOptions);
  return jobTrigger;
}

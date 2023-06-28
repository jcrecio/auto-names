/** 
 * Serialize trigger.
 * @param trigger the trigger
 * @return the string
 * @throws SourceBeanException the source bean exception
 */
public String serializeTrigger(Trigger trigger) throws SourceBeanException {
  StringBuffer buffer=new StringBuffer("<TRIGGER_DETAILS ");
  buffer.append(" ");
  String triggerName=trigger.getName();
  String triggerDescription=trigger.getDescription();
  JobDataMap jdm=trigger.getJobDataMap();
  Date triggerStartTime=trigger.getStartTime();
  String triggerStartDateStr="";
  String triggerStartTimeStr="";
  if (triggerStartTime != null) {
    Calendar startCal=new GregorianCalendar();
    startCal.setTime(triggerStartTime);
    int day=startCal.get(Calendar.DAY_OF_MONTH);
    int month=startCal.get(Calendar.MONTH);
    int year=startCal.get(Calendar.YEAR);
    triggerStartDateStr=((day < 10) ? "0" : "") + day + "/"+ ((month + 1 < 10) ? "0" : "")+ (month + 1)+ "/"+ year;
    int hour=startCal.get(Calendar.HOUR_OF_DAY);
    int minute=startCal.get(Calendar.MINUTE);
    triggerStartTimeStr=((hour < 10) ? "0" : "") + hour + ":"+ ((minute < 10) ? "0" : "")+ minute;
  }
  Date triggerEndTime=trigger.getEndTime();
  String triggerEndDateStr="";
  String triggerEndTimeStr="";
  if (triggerEndTime != null) {
    Calendar endCal=new GregorianCalendar();
    endCal.setTime(triggerEndTime);
    int day=endCal.get(Calendar.DAY_OF_MONTH);
    int month=endCal.get(Calendar.MONTH);
    int year=endCal.get(Calendar.YEAR);
    triggerEndDateStr=((day < 10) ? "0" : "") + day + "/"+ ((month + 1 < 10) ? "0" : "")+ (month + 1)+ "/"+ year;
    int hour=endCal.get(Calendar.HOUR_OF_DAY);
    int minute=endCal.get(Calendar.MINUTE);
    triggerEndTimeStr=((hour < 10) ? "0" : "") + hour + ":"+ ((minute < 10) ? "0" : "")+ minute;
  }
  buffer.append(" triggerName=\"" + (triggerName != null ? triggerName : "") + "\"");
  buffer.append(" triggerDescription=\"" + (triggerDescription != null ? triggerDescription : "") + "\"");
  buffer.append(" triggerStartDate=\"" + triggerStartDateStr + "\"");
  buffer.append(" triggerStartTime=\"" + triggerStartTimeStr + "\"");
  buffer.append(" triggerEndDate=\"" + triggerEndDateStr + "\"");
  buffer.append(" triggerEndTime=\"" + triggerEndTimeStr + "\"");
  String chronStr=jdm.getString("chronString");
  if ((chronStr == null) || (chronStr.trim().equals(""))) {
    chronStr="single{}";
  }
  buffer.append(" triggerChronString=\"" + chronStr + "\"");
  buffer.append(" >");
  buffer.append("<JOB_PARAMETERS>");
  if (jdm != null && !jdm.isEmpty()) {
    String[] keys=jdm.getKeys();
    serializeTrigger_extraction_2(buffer,jdm,keys);
  }
  buffer.append("</JOB_PARAMETERS>");
  buffer.append("</TRIGGER_DETAILS>");
  return buffer.toString();
}

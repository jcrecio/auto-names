public static JSONObject isValidJobTrigger(JobTrigger jobt) throws JSONException {
  JSONArray ja=new JSONArray();
  if (jobt.getTriggerName() == null || jobt.getTriggerName().trim().isEmpty()) {
    ja.put("Empty name");
  }
  boolean validStartDate=true;
  if (jobt.getStartDate() == null || jobt.getStartDate().trim().isEmpty()) {
    ja.put("Null or not Valid Start date");
    validStartDate=false;
  }
  if (jobt.getStartTime() == null || jobt.getStartTime().trim().isEmpty()) {
    ja.put("Null start time");
    validStartDate=false;
  }
 else {
    String[] tp=jobt.getStartTime().split(":");
    int h=Integer.parseInt(tp[0]);
    int m=Integer.parseInt(tp[1]);
    if (h < 0 || h > 23) {
      ja.put(" start time hours not valid ");
      validStartDate=false;
    }
    if (m < 0 || m > 59) {
      ja.put(" start time minutes not valid ");
      validStartDate=false;
    }
  }
  if (validStartDate && (jobt.getEndDate() != null && !jobt.getEndDate().equals(""))) {
    boolean validTime=true;
    String[] tp=jobt.getEndTime().split(":");
    int h=Integer.parseInt(tp[0]);
    int m=Integer.parseInt(tp[1]);
    if (h < 0 || h > 23) {
      ja.put(" end time hours not valid ");
      validTime=false;
    }
    if (m < 0 || m > 59) {
      ja.put(" end time minutes not valid ");
      validTime=false;
    }
    if (validTime) {
      SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy hh:mm");
      try {
        Date dateStart=sdf.parse(jobt.getStartDate() + " " + jobt.getStartTime());
        Date dateEnd=sdf.parse(jobt.getEndDate() + " " + jobt.getEndTime());
        if (dateEnd.before(dateStart)) {
          ja.put(" End time is before Start time  ");
        }
      }
 catch (      ParseException e) {
        e.printStackTrace();
      }
    }
  }
  DateTimeFormatter dateTime=ISODateTimeFormat.dateTime();
  String zonedStartTime=jobt.getZonedStartTime();
  DateTime parsedStartTime=null;
  JSONObject jo=isValidJobTrigger_extraction_2(jobt,ja,dateTime,zonedStartTime,parsedStartTime);
  return jo;
}

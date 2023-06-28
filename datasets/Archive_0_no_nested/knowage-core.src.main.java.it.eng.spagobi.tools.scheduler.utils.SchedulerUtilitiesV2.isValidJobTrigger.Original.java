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
  if (zonedStartTime != null) {
    try {
      parsedStartTime=dateTime.parseDateTime(zonedStartTime);
    }
 catch (    IllegalArgumentException e) {
      ja.put(" Zoned start time is not valid ");
    }
  }
  String zonedEndTime=jobt.getZonedEndTime();
  DateTime parsedEndTime=null;
  if (zonedEndTime != null) {
    try {
      parsedEndTime=dateTime.parseDateTime(zonedEndTime);
    }
 catch (    IllegalArgumentException e) {
      ja.put(" Zoned end time is not valid ");
    }
  }
  if (parsedStartTime != null && parsedEndTime != null && parsedEndTime.isBefore(parsedStartTime)) {
    ja.put(" Zoned end time is before zoned start time ");
  }
  JSONObject jo=new JSONObject();
  if (ja.length() >= 0) {
    jo.put("Status","NON OK");
    jo.put("Errors",ja);
  }
 else {
    jo.put("Status","OK");
  }
  return jo;
}

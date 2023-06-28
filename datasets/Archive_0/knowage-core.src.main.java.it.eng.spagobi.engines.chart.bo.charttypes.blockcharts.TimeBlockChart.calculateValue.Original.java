@Override public DatasetMap calculateValue() throws Exception {
  logger.debug("IN");
  super.calculateValue();
  DatasetMap datasetMap=new DatasetMap();
  String res=DataSetAccessFunctions.getDataSetResultFromId(profile,getData(),parametersObject);
  ArrayList<Activity> activities=new ArrayList<Activity>();
  RegularTimePeriod timePeriod=null;
  SourceBean sbRows=SourceBean.fromXMLString(res);
  List listAtts=sbRows.getAttributeAsList("ROW");
  if (listAtts == null) {
    logger.error("Null rows retrieved from dataset");
    return null;
  }
  int j=0;
  logger.debug("retrieved number rows: " + listAtts.size());
  for (Iterator iterator=listAtts.iterator(); iterator.hasNext(); ) {
    SourceBean row=(SourceBean)iterator.next();
    Activity activity=new Activity(row,beginDateFormat,beginHourFormat);
    activities.add(activity);
    logger.debug("Activity built from " + activity.getBeginDate() + " minutes"+ activity.getMinutes() != null ? activity.getMinutes().toString() : "");
    if (maxDateFound != null && !activity.getBeginDate().after(maxDateFound)) {
    }
 else {
      maxDateFound=activity.getBeginDate();
    }
    if (minDateFound != null && !activity.getBeginDate().before(minDateFound)) {
    }
 else {
      minDateFound=activity.getBeginDate();
    }
  }
  long daysBetween;
  if (dateMin != null && dateMax != null) {
    logger.debug("use date limit defined in template: from " + dateMin.toString() + " to "+ dateMax.toString());
    daysBetween=daysBetween(dateMin,dateMax);
  }
 else {
    logger.debug("use date limit found: from " + minDateFound.toString() + " to "+ maxDateFound.toString());
    daysBetween=daysBetween(minDateFound,maxDateFound);
  }
  logger.debug("Days between the two dates " + daysBetween);
  long minutesBetweenLong=daysBetween * 24 * 60* 2;
  int mbl=Long.valueOf(minutesBetweenLong).intValue();
  DefaultXYZDataset dataset=new DefaultXYZDataset();
  ArrayList<Long> xValuesList=new ArrayList<Long>();
  ArrayList<Double> yValuesList=new ArrayList<Double>();
  ArrayList<Double> zValuesList=new ArrayList<Double>();
  annotations=new HashMap<String,AnnotationBlock>();
  for (Iterator iterator=activities.iterator(); iterator.hasNext(); ) {
    Activity activity=(Activity)iterator.next();
    if (dateMin != null && dateMax != null && (activity.getBeginDate().after(dateMax) || activity.getBeginDate().before(dateMin))) {
      logger.debug("Activity discarde because starting out of selected bounds in " + activity.getBeginDate());
    }
 else {
      logger.debug("Inserting activity with begin date " + activity.getBeginDate() + " adn pattern "+ activity.getPattern());
      RegularTimePeriod rtp=new Day(activity.getBeginDate());
      long secondmills=rtp.getFirstMillisecond();
      Minute minute=activity.getMinutes();
      for (int i=0; i < activity.getDuration(); i++) {
        Integer hour=Integer.valueOf(minute.getHourValue());
        Integer minuteValue=Integer.valueOf(minute.getMinute());
        Double doubleMinuteValue=Double.valueOf((minuteValue.intValue()));
        double convertedMinuteValue=(doubleMinuteValue * 100) / 60.0;
        double convertedMinuteValueCent=convertedMinuteValue / 100;
        double hourD=hour.intValue();
        double converted=hourD + convertedMinuteValueCent;
        String yVal=Double.valueOf(converted).toString();
        xValuesList.add(new Long(secondmills));
        yValuesList.add(Double.valueOf(yVal));
        Object cosa=patternRangeIndex.get(activity.getPattern());
        if (cosa != null) {
          zValuesList.add(Double.valueOf(patternRangeIndex.get(activity.getPattern())).doubleValue() + 0.5);
        }
 else {
          zValuesList.add(-1.0);
        }
        if (annotations.get(activity.getCode()) == null) {
          AnnotationBlock annotation=new AnnotationBlock(activity.getCode());
          annotation.setXPosition(xValuesList.get(j).doubleValue());
          annotation.setYPosition(yValuesList.get(j).doubleValue());
          annotations.put(annotation.getAnnotation(),annotation);
        }
        minute=(Minute)minute.next();
        j++;
      }
    }
  }
  double[] xvalues=new double[xValuesList.size()];
  double[] yvalues=new double[yValuesList.size()];
  double[] zvalues=new double[zValuesList.size()];
  for (int i=0; i < zValuesList.size(); i++) {
    Long l=xValuesList.get(i);
    xvalues[i]=l;
    Double d2=yValuesList.get(i);
    yvalues[i]=d2;
    Double d=zValuesList.get(i);
    zvalues[i]=d;
  }
  logger.debug("adding the serie");
  dataset.addSeries("Series 1",new double[][]{xvalues,yvalues,zvalues});
  datasetMap.getDatasets().put("1",dataset);
  logger.debug("OUT");
  return datasetMap;
}

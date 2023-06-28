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
  calculateValue_extraction_1(activities,listAtts);
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
      j=calculateValue_extraction_2(j,xValuesList,yValuesList,zValuesList,activity,secondmills);
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

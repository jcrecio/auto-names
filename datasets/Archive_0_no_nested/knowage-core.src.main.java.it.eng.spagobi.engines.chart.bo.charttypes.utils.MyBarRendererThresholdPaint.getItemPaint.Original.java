public Paint getItemPaint(int row,int column){
  logger.debug("IN");
  String columnKey=(String)dataset.getColumnKey(column);
  int separator=columnKey.indexOf('-');
  String month=columnKey.substring(0,separator);
  String year=columnKey.substring(separator + 1);
  Number value=dataset.getValue(row,column);
  Month currentMonth=new Month(Integer.valueOf(month),Integer.valueOf(year));
  TimeSeriesDataItem item=timeSeries.getDataItem(currentMonth);
  if (nullValues.contains(columnKey)) {
    return background;
  }
  if (item == null || item.getValue() == null) {
    return background;
  }
  Double currentValue=(Double)item.getValue();
  TreeSet<Double> orderedThresholds=new TreeSet<Double>(thresholds.keySet());
  Double thresholdGiveColor=null;
  if (useTargets) {
    boolean stop=false;
    for (Iterator iterator=orderedThresholds.iterator(); iterator.hasNext() && stop == false; ) {
      Double currentThres=(Double)iterator.next();
      if (currentValue >= currentThres) {
        thresholdGiveColor=currentThres;
      }
 else {
        stop=true;
      }
    }
  }
 else   if (!useTargets) {
    boolean stop=false;
    for (Iterator iterator=orderedThresholds.iterator(); iterator.hasNext() && stop == false; ) {
      Double currentThres=(Double)iterator.next();
      if (currentValue > currentThres) {
      }
 else {
        stop=true;
        thresholdGiveColor=currentThres;
      }
    }
    if (stop == false) {
      thresholdGiveColor=null;
    }
  }
  Color colorToReturn=null;
  if (thresholdGiveColor == null) {
    if (bottomThreshold != null && bottomThreshold.getColor() != null) {
      colorToReturn=bottomThreshold.getColor();
    }
    if (colorToReturn == null) {
      colorToReturn=Color.BLACK;
    }
  }
 else {
    if (thresholds.get(thresholdGiveColor) != null && thresholds.get(thresholdGiveColor).getColor() != null)     colorToReturn=thresholds.get(thresholdGiveColor).getColor();
    if (colorToReturn == null) {
      colorToReturn=Color.BLACK;
    }
  }
  logger.debug("OUT");
  return colorToReturn;
}

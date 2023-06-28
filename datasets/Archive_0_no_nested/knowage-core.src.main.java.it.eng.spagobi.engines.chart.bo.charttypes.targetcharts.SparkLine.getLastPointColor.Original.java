public Paint getLastPointColor(){
  logger.debug("IN");
  Color colorToReturn=null;
  try {
    final int last=lastIndexMonth;
    TimeSeriesDataItem item=timeSeries.getDataItem(new Month(last,Integer.valueOf(lastYear).intValue()));
    if (item == null || item.getValue() == null) {
      return Color.WHITE;
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
 else     if (!useTargets) {
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
    if (thresholdGiveColor == null) {
      if (bottomThreshold != null && bottomThreshold.getColor() != null)       colorToReturn=bottomThreshold.getColor();
 else       colorToReturn=Color.GREEN;
    }
 else {
      TargetThreshold currThreshold=thresholds.get(thresholdGiveColor);
      colorToReturn=currThreshold.getColor();
      if (colorToReturn == null) {
        colorToReturn=Color.BLACK;
      }
    }
  }
 catch (  Exception e) {
    logger.error("Exception while deifning last ponter color: set default green",e);
    return Color.GREEN;
  }
  logger.debug("OUT");
  return colorToReturn;
}

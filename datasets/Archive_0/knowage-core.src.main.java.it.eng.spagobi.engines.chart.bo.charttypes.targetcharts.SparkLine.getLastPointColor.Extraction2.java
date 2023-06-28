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
      thresholdGiveColor=getLastPointColor_extraction_1(currentValue,orderedThresholds,thresholdGiveColor,stop);
    }
 else     if (!useTargets) {
      thresholdGiveColor=getLastPointColor_extraction_2(currentValue,orderedThresholds,thresholdGiveColor);
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

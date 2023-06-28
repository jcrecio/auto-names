/** 
 * Creates a chart for the specified dataset.
 * @param dataset  the dataset.
 * @return A chart instance.
 */
public JFreeChart createChart(DatasetMap datasets){
  logger.debug("IN");
  XYZDataset dataset=(XYZDataset)datasets.getDatasets().get("1");
  JFreeChart chart=null;
  NumberAxis xAxis=new NumberAxis(xLabel);
  xAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
  if (xLowerBound != null && xUpperBound != null) {
    xAxis.setLowerBound(xLowerBound);
    xAxis.setUpperBound(xUpperBound);
  }
 else {
    xAxis.setAutoRange(true);
  }
  xAxis.setAxisLinePaint(Color.white);
  xAxis.setTickMarkPaint(Color.white);
  if (addLabelsStyle != null && addLabelsStyle.getFont() != null) {
    xAxis.setLabelFont(addLabelsStyle.getFont());
    xAxis.setLabelPaint(addLabelsStyle.getColor());
  }
  NumberAxis yAxis=new NumberAxis(yLabel);
  PaintScale paintScale;
  XYBlockRenderer renderer=createChart_extraction_1(yAxis);
  if (grayPaintScale) {
    paintScale=new GrayPaintScale(minScaleValue,maxScaleValue);
  }
 else {
    if (scaleLowerBound != null && scaleUpperBound != null) {
      paintScale=new LookupPaintScale(scaleLowerBound,scaleUpperBound,Color.gray);
    }
 else {
      paintScale=new LookupPaintScale(minScaleValue,maxScaleValue,Color.gray);
    }
    for (int i=0; i < zRangeArray.length; i++) {
      ZRange zRange=zRangeArray[i];
      ((LookupPaintScale)paintScale).add(zRange.getValue().doubleValue(),zRange.getColor());
    }
  }
  renderer.setPaintScale(paintScale);
  XYPlot plot=new XYPlot(dataset,xAxis,yAxis,renderer);
  plot.setBackgroundPaint(Color.lightGray);
  plot.setDomainGridlinesVisible(false);
  plot.setRangeGridlinePaint(Color.white);
  plot.setAxisOffset(new RectangleInsets(5,5,5,5));
  plot.setForegroundAlpha(0.66f);
  chart=new JFreeChart(plot);
  TextTitle title=setStyleTitle(name,styleTitle);
  chart.setTitle(title);
  if (subName != null && !subName.equals("")) {
    TextTitle subTitle=setStyleTitle(subName,styleSubTitle);
    chart.addSubtitle(subTitle);
  }
  chart.removeLegend();
  NumberAxis scaleAxis=new NumberAxis(zLabel);
  scaleAxis.setAxisLinePaint(Color.white);
  scaleAxis.setTickMarkPaint(Color.white);
  createChart_extraction_2(chart,renderer,paintScale,scaleAxis);
  logger.debug("OUT");
  return chart;
}

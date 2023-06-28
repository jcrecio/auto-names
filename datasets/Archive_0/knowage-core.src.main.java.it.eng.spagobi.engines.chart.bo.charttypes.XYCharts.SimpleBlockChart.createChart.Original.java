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
  yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
  if (yLowerBound != null && yUpperBound != null) {
    yAxis.setLowerBound(yLowerBound);
    yAxis.setUpperBound(yUpperBound);
  }
 else   yAxis.setAutoRange(true);
  yAxis.setAxisLinePaint(Color.white);
  yAxis.setTickMarkPaint(Color.white);
  if (addLabelsStyle != null && addLabelsStyle.getFont() != null) {
    yAxis.setLabelFont(addLabelsStyle.getFont());
    yAxis.setLabelPaint(addLabelsStyle.getColor());
  }
  XYBlockRenderer renderer=new XYBlockRenderer();
  PaintScale paintScale=null;
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
  scaleAxis.setTickLabelFont(new Font("Dialog",Font.PLAIN,7));
  if (scaleLowerBound != null && scaleUpperBound != null) {
    scaleAxis.setLowerBound(scaleLowerBound);
    scaleAxis.setUpperBound(scaleUpperBound);
  }
 else   scaleAxis.setAutoRange(true);
  if (addLabelsStyle != null && addLabelsStyle.getFont() != null) {
    scaleAxis.setLabelFont(addLabelsStyle.getFont());
    scaleAxis.setLabelPaint(addLabelsStyle.getColor());
  }
  if (blockHeight != null && blockWidth != null) {
    renderer.setBlockWidth(blockWidth.doubleValue());
    renderer.setBlockHeight(blockHeight.doubleValue());
  }
  PaintScaleLegend legend=new PaintScaleLegend(paintScale,scaleAxis);
  legend.setAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
  legend.setAxisOffset(5.0);
  legend.setMargin(new RectangleInsets(5,5,5,5));
  legend.setFrame(new BlockBorder(Color.black));
  legend.setPadding(new RectangleInsets(10,10,10,10));
  legend.setStripWidth(10);
  legend.setPosition(RectangleEdge.RIGHT);
  legend.setBackgroundPaint(color);
  chart.addSubtitle(legend);
  chart.setBackgroundPaint(color);
  logger.debug("OUT");
  return chart;
}

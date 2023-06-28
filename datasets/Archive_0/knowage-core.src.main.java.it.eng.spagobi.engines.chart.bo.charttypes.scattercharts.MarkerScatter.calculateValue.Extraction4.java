/** 
 * Inherited by IChart: calculates chart value.
 * @return the dataset
 * @throws Exception the exception
 */
public DatasetMap calculateValue() throws Exception {
  logger.debug("IN");
  String res=DataSetAccessFunctions.getDataSetResultFromId(profile,getData(),parametersObject);
  DefaultXYDataset dataset=new DefaultXYDataset();
  SourceBean sbRows=SourceBean.fromXMLString(res);
  List listAtts=sbRows.getAttributeAsList("ROW");
  series=new Vector();
  boolean firstX=true;
  boolean firstY=true;
  double xTempMax=0.0;
  double xTempMin=0.0;
  double yTempMax=0.0;
  double yTempMin=0.0;
  boolean first=true;
  for (Iterator iterator=listAtts.iterator(); iterator.hasNext(); ) {
    SourceBean serie=(SourceBean)iterator.next();
    List atts=serie.getContainedAttributes();
    String catValue="";
    String serValue="";
    if (first) {
      if (name.indexOf("$F{") >= 0) {
        setTitleParameter(atts);
      }
      if (getSubName() != null && getSubName().indexOf("$F") >= 0) {
        setSubTitleParameter(atts);
      }
      if (yMarkerLabel != null && yMarkerLabel.indexOf("$F{") >= 0) {
        setYMarkerLabel(atts);
      }
      first=false;
    }
    int numAttsX=0;
    int numAttsY=0;
    for (Iterator iterator2=atts.iterator(); iterator2.hasNext(); ) {
      SourceBeanAttribute object=(SourceBeanAttribute)iterator2.next();
      String name=new String(object.getKey());
      if (!name.equalsIgnoreCase("x")) {
        if (String.valueOf(name.charAt(0)).equalsIgnoreCase("x"))         numAttsX++;
 else         if (String.valueOf(name.charAt(0)).equalsIgnoreCase("y"))         numAttsY++;
      }
    }
    int maxNumAtts=(numAttsX < numAttsY) ? numAttsY : numAttsX;
    double[] x=new double[maxNumAtts];
    double[] y=new double[maxNumAtts];
    String name="";
    String value="";
    for (Iterator iterator2=atts.iterator(); iterator2.hasNext(); ) {
      SourceBeanAttribute object=(SourceBeanAttribute)iterator2.next();
      name=new String(object.getKey());
      value=calculateValue_extraction_3(object);
      if (name.equalsIgnoreCase("x")) {
        catValue=value;
      }
 else       if (String.valueOf(name.charAt(0)).equalsIgnoreCase("x") || String.valueOf(name.charAt(0)).equalsIgnoreCase("y")) {
        String pos=String.valueOf(name.charAt(0));
        String numS=name.substring(1);
        int num=Integer.valueOf(numS).intValue();
        double valueD=0.0;
        valueD=calculateValue_extraction_4(value);
        if (pos.equalsIgnoreCase("x")) {
          firstX=calculateValue_extraction_5(firstX,x,num,valueD);
        }
 else         firstY=calculateValue_extraction_6(firstY,y,pos,num,valueD);
      }
    }
    double[][] seriesT=new double[][]{y,x};
    dataset.addSeries(catValue,seriesT);
    series.add(catValue);
    if (viewAnnotations != null && viewAnnotations.equalsIgnoreCase("true")) {
      double tmpx=seriesT[1][0];
      double tmpy=seriesT[0][0];
      annotationMap.put(catValue,String.valueOf(tmpx) + "__" + String.valueOf(tmpy));
    }
  }
  logger.debug("OUT");
  DatasetMap datasets=new DatasetMap();
  datasets.addDataset("1",dataset);
  return datasets;
}

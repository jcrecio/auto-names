/** 
 * set parameters for the creation of the chart getting them from template or from LOV.
 * @param content the content of the template.
 * @return A chart that displays a value as a dial.
 */
public void configureChart(SourceBean content){
  super.configureChart(content);
  logger.debug("IN");
  if (!isLovConfDefined) {
    logger.debug("Configuration set in template");
    if (confParameters.get("increment") != null) {
      String increment=(String)confParameters.get("increment");
      setIncrement(Double.valueOf(increment).doubleValue());
    }
 else {
      logger.error("increment not defined");
      return;
    }
    if (confParameters.get("minor_tick") != null) {
      String minorTickCount=(String)confParameters.get("minor_tick");
      setMinorTickCount(Integer.valueOf(minorTickCount).intValue());
    }
 else {
      setMinorTickCount(10);
    }
    if (confParameters.get("dialtextuse") != null) {
      String dialtextusetemp=(String)confParameters.get("dialtextuse");
      if (dialtextusetemp.equalsIgnoreCase("true")) {
        dialtextuse=true;
      }
 else       dialtextuse=false;
    }
    if (dialtextuse && confParameters.get("dialtext") != null) {
      dialtext=(String)confParameters.get("dialtext");
    }
    SourceBean intervalsSB=(SourceBean)content.getAttribute("INTERVALS");
    if (intervalsSB == null) {
      intervalsSB=(SourceBean)content.getAttribute("CONF.INTERVALS");
    }
    List intervalsAttrsList=null;
    if (intervalsSB != null) {
      intervalsAttrsList=intervalsSB.getContainedSourceBeanAttributes();
    }
    if (intervalsAttrsList == null || intervalsAttrsList.isEmpty()) {
      logger.warn("intervals not defined; default settings");
      KpiInterval interval=new KpiInterval();
      interval.setMin(getLower());
      interval.setMax(getUpper());
      interval.setColor(Color.WHITE);
      addInterval(interval);
    }
 else {
      Iterator intervalsAttrsIter=intervalsAttrsList.iterator();
      while (intervalsAttrsIter.hasNext()) {
        SourceBeanAttribute paramSBA=(SourceBeanAttribute)intervalsAttrsIter.next();
        SourceBean param=(SourceBean)paramSBA.getValue();
        String min=(String)param.getAttribute("min");
        String max=(String)param.getAttribute("max");
        String col=(String)param.getAttribute("color");
        KpiInterval interval=new KpiInterval();
        interval.setMin(Double.valueOf(min).doubleValue());
        interval.setMax(Double.valueOf(max).doubleValue());
        Color color=new Color(Integer.decode(col).intValue());
        if (color != null) {
          interval.setColor(color);
        }
 else {
          interval.setColor(Color.WHITE);
        }
        addInterval(interval);
      }
    }
  }
 else {
    logger.debug("configuration defined in LOV" + confDataset);
    String increment=(String)sbRow.getAttribute("increment");
    String minorTickCount=(String)sbRow.getAttribute("minor_tick");
    setIncrement(Double.valueOf(increment).doubleValue());
    setMinorTickCount(Integer.valueOf(minorTickCount).intValue());
    String intervalsNumber=(String)sbRow.getAttribute("intervals_number");
    if (intervalsNumber == null || intervalsNumber.equals("") || intervalsNumber.equals("0")) {
      KpiInterval interval=new KpiInterval();
      interval.setMin(getLower());
      interval.setMax(getUpper());
      interval.setColor(Color.WHITE);
      addInterval(interval);
    }
 else {
      for (int i=1; i <= Integer.valueOf(intervalsNumber).intValue(); i++) {
        KpiInterval interval=new KpiInterval();
        String min=(String)sbRow.getAttribute("min" + (new Integer(i)).toString());
        String max=(String)sbRow.getAttribute("max" + (new Integer(i)).toString());
        String col=(String)sbRow.getAttribute("color" + (new Integer(i)).toString());
        interval.setMin(Double.valueOf(min).doubleValue());
        interval.setMax(Double.valueOf(max).doubleValue());
        Color color=new Color(Integer.decode(col).intValue());
        interval.setColor(color);
        addInterval(interval);
      }
    }
  }
  logger.debug("out");
}

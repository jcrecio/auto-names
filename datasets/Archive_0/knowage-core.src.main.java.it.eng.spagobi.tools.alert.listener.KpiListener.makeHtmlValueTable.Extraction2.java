private String makeHtmlValueTable(List<Integer> thresholdValues){
  for (  Integer thresholdId : thresholdValues) {
    List<KpiValue> values=valueMap.get(thresholdId);
    boolean showYear=false;
    boolean showQuarter=false;
    boolean showMonth=false;
    boolean showWeek=false;
    boolean showDay=false;
    if (values != null && !values.isEmpty()) {
      for (      KpiValue sbiKpiValue : values) {
        if (!ALL.equals(sbiKpiValue.getTheYear())) {
          showYear=true;
        }
        if (!ALL.equals(sbiKpiValue.getTheQuarter())) {
          showQuarter=true;
        }
        if (!ALL.equals(sbiKpiValue.getTheMonth())) {
          showMonth=true;
        }
        if (!ALL.equals(sbiKpiValue.getTheWeek())) {
          showWeek=true;
        }
        if (!ALL.equals(sbiKpiValue.getTheDay())) {
          showDay=true;
        }
      }
      StringBuffer sb=new StringBuffer();
      String tableStyle=" style=\"border:1px solid;border-collapse:collapse;\" ";
      makeHtmlValueTable_extraction_2(thresholdId,showYear,showQuarter,showMonth,sb,tableStyle);
      if (showWeek) {
        sb.append("<th>The Week</th>");
      }
      if (showDay) {
        sb.append("<th>The Day</th>");
      }
      sb.append("<th>Computed Value</th>");
      sb.append("<th>Manual Value</th>");
      sb.append("</tr>");
      for (      KpiValue sbiKpiValue : values) {
        sb.append("<tr>");
        sb.append("<td>" + clean(sbiKpiValue.getLogicalKey()) + "</td>");
        makeHtmlValueTable_extraction_3(showYear,showQuarter,showMonth,showWeek,showDay,sb,sbiKpiValue);
        sb.append("<td>" + clean(sbiKpiValue.getComputedValue()) + "</td>");
        sb.append("<td>" + clean(sbiKpiValue.getManualValue()) + "</td>");
        sb.append("</tr>");
      }
      sb.append("</table>");
      return sb.toString();
    }
  }
  return null;
}

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
      for (      ThresholdValue tValue : thresholds) {
        if (tValue.getId().equals(thresholdId)) {
          sb.append("<table " + tableStyle + "></tr>");
          sb.append("<th>Threshold label</th>");
          sb.append("<th>Color</th>");
          sb.append("<th>Severity</th>");
          sb.append("<th>Min value</th>");
          sb.append("<th>Max value</th>");
          sb.append("</tr><tr>");
          sb.append("<td>" + clean(tValue.getLabel()) + "</td>");
          sb.append(clean(tValue.getColor()).isEmpty() ? "<td></td>" : "<td style=\"background-color:" + clean(tValue.getColor()) + ";\"></td>");
          sb.append("<td>" + clean(tValue.getSeverityCd()) + "</td>");
          sb.append("<td>" + clean(tValue.getMinValue()) + "</td>");
          sb.append("<td>" + clean(tValue.getMaxValue()) + "</td>");
          sb.append("</tr></table>");
          break;
        }
      }
      sb.append("<table" + tableStyle + "><tr>");
      sb.append("<th>Logical Key</th>");
      if (showYear) {
        sb.append("<th>The Year</th>");
      }
      if (showQuarter) {
        sb.append("<th>The Quarter</th>");
      }
      if (showMonth) {
        sb.append("<th>The Month</th>");
      }
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

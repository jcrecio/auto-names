private String makeHtmlValueTable(List<Integer> thresholdValues){
  for (  Integer thresholdId : thresholdValues) {
    List<KpiValue> values=valueMap.get(thresholdId);
    boolean showYear=false;
    boolean showQuarter=false;
    boolean showMonth=false;
    boolean showWeek=false;
    boolean showDay=false;
    if (values != null && !values.isEmpty()) {
      return makeHtmlValueTable_extraction_1(thresholdId,values,showYear,showQuarter,showMonth,showWeek,showDay);
    }
  }
  return null;
}

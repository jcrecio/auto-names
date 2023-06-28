private void checkConflictsWithKpi(JSError jsError,Rule rule,Map<Kpi,List<String>> kpimap,IKpiDAO kpiDao) throws EMFUserError {
  if (rule.getId() != null && rule.getVersion() != null) {
    Collection<String> measureAndKpi=new HashSet<>();
    Set<String> usedMeasureList=new HashSet<>();
    for (    List<String> m : kpimap.values()) {
      usedMeasureList.addAll(m);
    }
    Set<String> newRuleOutputList=new HashSet<>();
    for (    RuleOutput ro : rule.getRuleOutputs()) {
      newRuleOutputList.add(ro.getAlias());
    }
    usedMeasureList.removeAll(newRuleOutputList);
    for (    String name : usedMeasureList) {
      for (      Entry<Kpi,List<String>> kpi : kpimap.entrySet()) {
        if (kpi.getValue().contains(name)) {
          measureAndKpi.add("\"" + name + "\" used by \""+ kpi.getKey().getName()+ "\"");
          break;
        }
      }
    }
    if (!measureAndKpi.isEmpty()) {
      jsError.addErrorKey("newKpi.rule.usedByKpi.save.error",StringUtils.join(measureAndKpi,", "));
    }
    if (!kpimap.isEmpty()) {
      Set<String> usedAttributes=new HashSet<>();
      Map<String,List<String>> kpis=new HashMap<>();
      checkConflictsWithKpi_extraction_2(jsError,kpimap,newRuleOutputList,usedAttributes,kpis);
    }
    if (rule.getPlaceholders() != null && !rule.getPlaceholders().isEmpty()) {
      List<String> placeholderNames=new ArrayList<>();
      for (      Placeholder placeholder : rule.getPlaceholders()) {
        placeholderNames.add(placeholder.getName());
      }
      List<String> kpiNames=new ArrayList<>();
      boolean anyScheduler=false;
      checkConflictsWithKpi_extraction_4(jsError,kpimap,kpiDao,placeholderNames,kpiNames,anyScheduler);
    }
  }
}

private void checkConflictsWithKpi(JSError jsError,Rule rule,Map<Kpi,List<String>> kpimap,IKpiDAO kpiDao) throws EMFUserError {
  if (rule.getId() != null && rule.getVersion() != null) {
    Collection<String> measureAndKpi=new HashSet<>();
    Set<String> newRuleOutputList=checkConflictsWithKpi_extraction_1(jsError,rule,kpimap,measureAndKpi);
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

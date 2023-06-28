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
      for (      Kpi kpi : kpimap.keySet()) {
        try {
          JSONArray measures=new JSONObject(kpi.getCardinality()).getJSONArray("measureList");
          for (int i=0; i < measures.length(); i++) {
            Iterator<String> attributesIterator=measures.getJSONObject(i).getJSONObject("attributes").keys();
            while (attributesIterator.hasNext()) {
              String attribute=attributesIterator.next();
              usedAttributes.add(attribute);
              if (kpis.get(kpi.getName()) == null) {
                kpis.put(kpi.getName(),new ArrayList<String>());
              }
              kpis.get(kpi.getName()).add(attribute);
            }
          }
        }
 catch (        JSONException e) {
          logger.error("Error while trying to read measureList/attributes from kpi [id=" + kpi.getId() + "|version="+ kpi.getVersion()+ "]",e);
        }
      }
      if (!usedAttributes.isEmpty()) {
        Collection<String> attributesError=new HashSet<>();
        usedAttributes.removeAll(newRuleOutputList);
        for (        String name : usedAttributes) {
          for (          Entry<String,List<String>> kpi : kpis.entrySet()) {
            if (kpi.getValue().contains(name)) {
              attributesError.add("\"" + name + "\" used by \""+ kpi.getKey()+ "\"");
              break;
            }
          }
        }
        if (!attributesError.isEmpty()) {
          jsError.addErrorKey("newKpi.rule.attributeUsedByKpi.save.error",StringUtils.join(attributesError,", "));
        }
      }
    }
    if (rule.getPlaceholders() != null && !rule.getPlaceholders().isEmpty()) {
      List<String> placeholderNames=new ArrayList<>();
      for (      Placeholder placeholder : rule.getPlaceholders()) {
        placeholderNames.add(placeholder.getName());
      }
      List<String> kpiNames=new ArrayList<>();
      boolean anyScheduler=false;
      for (      Kpi kpi : kpimap.keySet()) {
        List<KpiScheduler> schedulerList=kpiDao.listSchedulerAndFiltersByKpi(kpi.getId(),kpi.getVersion(),true);
        for (        KpiScheduler kpiScheduler : schedulerList) {
          for (          SchedulerFilter filter : kpiScheduler.getFilters()) {
            placeholderNames.remove(filter.getPlaceholderName());
          }
        }
        kpiNames.add(kpi.getName());
        if (schedulerList.size() > 0) {
          anyScheduler=true;
        }
      }
      if (!placeholderNames.isEmpty() && anyScheduler) {
        jsError.addWarningKey("newKpi.rule.placeholdersMustBeSet.save.error",StringUtils.join(placeholderNames,", "),StringUtils.join(kpiNames,", "));
      }
    }
  }
}

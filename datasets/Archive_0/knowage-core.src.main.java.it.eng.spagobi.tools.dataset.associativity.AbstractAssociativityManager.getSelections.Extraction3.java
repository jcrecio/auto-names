@Override public Map<String,Map<String,Set<Tuple>>> getSelections(){
  Map<String,Map<String,Set<Tuple>>> selections=new HashMap<>();
  for (  String dataset : result.getDatasetToEdgeGroup().keySet()) {
    Set<EdgeGroup> groups=result.getDatasetToEdgeGroup().get(dataset);
    Map<String,Set<Tuple>> groupToValues=new HashMap<>(groups.size());
    for (    EdgeGroup group : groups) {
      Set<Tuple> values=result.getEdgeGroupValues().get(group);
      if (values != null) {
        List<String> columns=getColumnNames(group.getOrderedEdgeNames(),dataset);
        String columnsString=StringUtils.join(columns,",");
        groupToValues.put(columnsString,values);
      }
    }
    for (    String edgeName : datasetToAssociations.get(dataset).keySet()) {
      for (      EdgeGroup edgeGroup : groups) {
        if (!edgeGroup.getEdgeNames().contains(edgeName)) {
          String missingColumn=datasetToAssociations.get(dataset).get(edgeName);
          getSelections_extraction_1(dataset,groups,groupToValues,edgeName,missingColumn);
        }
      }
    }
    selections.put(dataset,groupToValues);
  }
  return selections;
}

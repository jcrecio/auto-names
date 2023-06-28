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
          if (ParametersUtilities.isParameter(missingColumn)) {
            String missingParameter=ParametersUtilities.getParameterName(missingColumn);
            if (associativeDatasetContainers.get(dataset) != null) {
              String value=associativeDatasetContainers.get(dataset).getParameters().get(missingParameter);
              HashSet<Tuple> tuples=new HashSet<Tuple>();
              if (value != null) {
                if (value.startsWith("'") && value.endsWith("'")) {
                  value=value.substring(1,value.length() - 1);
                }
                String[] valueArray=value.split("','");
                List<String> finalVals=new ArrayList<String>();
                Tuple tupleToAdd=getSelections_extraction_2(valueArray,finalVals);
                tuples.add(tupleToAdd);
                groupToValues.put(missingColumn,tuples);
              }
            }
 else {
              if (datasetToAssociations.get(dataset) != null) {
                getSelections_extraction_3(dataset,groups,groupToValues,edgeName,missingColumn);
              }
            }
          }
        }
      }
    }
    selections.put(dataset,groupToValues);
  }
  return selections;
}

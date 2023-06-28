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
                for (int i=0; i < valueArray.length; i++) {
                  String val=valueArray[i];
                  val=val.replaceAll("''","\'");
                  finalVals.add(val);
                }
                Tuple tupleToAdd=new Tuple(finalVals);
                tuples.add(tupleToAdd);
                groupToValues.put(missingColumn,tuples);
              }
            }
 else {
              if (datasetToAssociations.get(dataset) != null) {
                Map<String,String> parametersByEdgeGroup=datasetToAssociations.get(dataset);
                for (                String param : parametersByEdgeGroup.keySet()) {
                  if (parametersByEdgeGroup.get(param).equals(missingColumn)) {
                    if (edgeName.equals(param)) {
                      for (                      EdgeGroup edgeGr : groups) {
                        if (edgeGr.getEdgeNames().contains(edgeName)) {
                          Set<Tuple> tuples=result.getEdgeGroupValues().get(edgeGr);
                          groupToValues.put(missingColumn,tuples);
                        }
                      }
                    }
                  }
                }
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

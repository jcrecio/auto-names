private JSONArray getChildrenForTreeLov(ILovDetail lovProvDet,List rows,String mode,int treeLovNodeLevel,String treeLovNodeValue){
  String valueColumn;
  String descriptionColumn;
  boolean addNode;
  String treeLovNodeName="";
  String treeLovParentNodeName="";
  try {
    if (treeLovNodeValue == "lovroot") {
      treeLovNodeName=lovProvDet.getTreeLevelsColumns().get(0).getFirst();
      treeLovParentNodeName="lovroot";
      treeLovNodeLevel=-1;
    }
 else     if (lovProvDet.getTreeLevelsColumns().size() > treeLovNodeLevel + 1) {
      treeLovNodeName=lovProvDet.getTreeLevelsColumns().get(treeLovNodeLevel + 1).getFirst();
      treeLovParentNodeName=lovProvDet.getTreeLevelsColumns().get(treeLovNodeLevel).getFirst();
    }
    Set<JSONObject> valuesDataJSON=new LinkedHashSet<JSONObject>();
    valueColumn=lovProvDet.getValueColumnName();
    descriptionColumn=lovProvDet.getDescriptionColumnName();
    for (int q=0; q < rows.size(); q++) {
      SourceBean row=(SourceBean)rows.get(q);
      JSONObject valueJSON=null;
      addNode=false;
      List columns=row.getContainedAttributes();
      valueJSON=new JSONObject();
      boolean notNullNode=false;
      for (int i=0; i < columns.size(); i++) {
        SourceBeanAttribute attribute=(SourceBeanAttribute)columns.get(i);
        if ((treeLovParentNodeName == "lovroot") || (attribute.getKey().equalsIgnoreCase(treeLovParentNodeName) && (attribute.getValue().toString()).equalsIgnoreCase(treeLovNodeValue))) {
          addNode=true;
        }
        if (lovProvDet.getTreeLevelsColumns().size() == treeLovNodeLevel + 2) {
          notNullNode=getChildrenForTreeLov_extraction_2(treeLovNodeLevel,valueColumn,descriptionColumn,valueJSON,notNullNode,attribute);
        }
 else         if (attribute.getKey().equalsIgnoreCase(treeLovNodeName)) {
          valueJSON=new JSONObject();
          valueJSON.put("description",attribute.getValue());
          valueJSON.put("value",attribute.getValue());
          valueJSON.put("id",attribute.getValue() + NODE_ID_SEPARATOR + (treeLovNodeLevel + 1));
          notNullNode=true;
        }
      }
      if (addNode && notNullNode) {
        valuesDataJSON.add(valueJSON);
      }
    }
    JSONArray valuesDataJSONArray=new JSONArray();
    for (Iterator iterator=valuesDataJSON.iterator(); iterator.hasNext(); ) {
      JSONObject jsonObject=(JSONObject)iterator.next();
      valuesDataJSONArray.put(jsonObject);
    }
    return valuesDataJSONArray;
  }
 catch (  Exception e) {
    throw new SpagoBIServiceException("Impossible to serialize response",e);
  }
}

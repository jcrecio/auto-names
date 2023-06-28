/** 
 * Get the corresponding general config for the dimension's hierarchy
 * @param dimension the dimension name
 * @return the hashmap config
 */
public HashMap<String,Object> getConfig(String dimension){
  HashMap<String,Object> toReturn=new HashMap<String,Object>();
  SourceBean sb=getTemplate();
  SourceBean dimensions=(SourceBean)sb.getAttribute(HierarchyConstants.DIMENSIONS);
  List lst=dimensions.getAttributeAsList(HierarchyConstants.DIMENSION);
  for (Iterator iterator=lst.iterator(); iterator.hasNext(); ) {
    JSONObject hierarchy=new JSONObject();
    SourceBean sbRow=(SourceBean)iterator.next();
    String label=sbRow.getAttribute(HierarchyConstants.LABEL) != null ? sbRow.getAttribute(HierarchyConstants.LABEL).toString() : null;
    if (label.equalsIgnoreCase(dimension)) {
      List lstConfigFields=sbRow.getAttributeAsList(HierarchyConstants.CONFIGS + "." + HierarchyConstants.CONFIG);
      getConfig_extraction_1(toReturn,lstConfigFields);
    }
  }
  return toReturn;
}

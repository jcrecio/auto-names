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
      for (Iterator iter=lstConfigFields.iterator(); iter.hasNext(); ) {
        SourceBean sbConfig=(SourceBean)iter.next();
        if (null != sbConfig.getAttribute(HierarchyConstants.NUM_LEVELS))         toReturn.put(HierarchyConstants.NUM_LEVELS,sbConfig.getAttribute(HierarchyConstants.NUM_LEVELS));
        if (null != sbConfig.getAttribute(HierarchyConstants.ALLOW_DUPLICATE))         toReturn.put(HierarchyConstants.ALLOW_DUPLICATE,sbConfig.getAttribute(HierarchyConstants.ALLOW_DUPLICATE));
        if (null != sbConfig.getAttribute(HierarchyConstants.NODE))         toReturn.put(HierarchyConstants.NODE,sbConfig.getAttribute(HierarchyConstants.NODE));
        if (null != sbConfig.getAttribute(HierarchyConstants.LEAF))         toReturn.put(HierarchyConstants.LEAF,sbConfig.getAttribute(HierarchyConstants.LEAF));
        if (null != sbConfig.getAttribute(HierarchyConstants.ORIG_NODE))         toReturn.put(HierarchyConstants.ORIG_NODE,sbConfig.getAttribute(HierarchyConstants.ORIG_NODE));
        getConfig_extraction_2(toReturn,sbConfig);
      }
    }
  }
  return toReturn;
}

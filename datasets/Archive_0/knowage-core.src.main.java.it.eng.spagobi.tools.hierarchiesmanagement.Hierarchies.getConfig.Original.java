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
        if (null != sbConfig.getAttribute(HierarchyConstants.TREE_NODE_ID))         toReturn.put(HierarchyConstants.TREE_NODE_ID,sbConfig.getAttribute(HierarchyConstants.TREE_NODE_ID));
        if (null != sbConfig.getAttribute(HierarchyConstants.TREE_NODE_CD))         toReturn.put(HierarchyConstants.TREE_NODE_CD,sbConfig.getAttribute(HierarchyConstants.TREE_NODE_CD));
        if (null != sbConfig.getAttribute(HierarchyConstants.TREE_NODE_NM))         toReturn.put(HierarchyConstants.TREE_NODE_NM,sbConfig.getAttribute(HierarchyConstants.TREE_NODE_NM));
        if (null != sbConfig.getAttribute(HierarchyConstants.TREE_LEAF_ID))         toReturn.put(HierarchyConstants.TREE_LEAF_ID,sbConfig.getAttribute(HierarchyConstants.TREE_LEAF_ID));
        if (null != sbConfig.getAttribute(HierarchyConstants.TREE_LEAF_CD))         toReturn.put(HierarchyConstants.TREE_LEAF_CD,sbConfig.getAttribute(HierarchyConstants.TREE_LEAF_CD));
        if (null != sbConfig.getAttribute(HierarchyConstants.TREE_LEAF_NM))         toReturn.put(HierarchyConstants.TREE_LEAF_NM,sbConfig.getAttribute(HierarchyConstants.TREE_LEAF_NM));
        if (null != sbConfig.getAttribute(HierarchyConstants.DIMENSION_ID))         toReturn.put(HierarchyConstants.DIMENSION_ID,sbConfig.getAttribute(HierarchyConstants.DIMENSION_ID));
        if (null != sbConfig.getAttribute(HierarchyConstants.DIMENSION_CD))         toReturn.put(HierarchyConstants.DIMENSION_CD,sbConfig.getAttribute(HierarchyConstants.DIMENSION_CD));
        if (null != sbConfig.getAttribute(HierarchyConstants.DIMENSION_NM))         toReturn.put(HierarchyConstants.DIMENSION_NM,sbConfig.getAttribute(HierarchyConstants.DIMENSION_NM));
        if (null != sbConfig.getAttribute(HierarchyConstants.FILL_EMPTY))         toReturn.put(HierarchyConstants.FILL_EMPTY,sbConfig.getAttribute(HierarchyConstants.FILL_EMPTY));
        if (null != sbConfig.getAttribute(HierarchyConstants.FILL_VALUE))         toReturn.put(HierarchyConstants.FILL_VALUE,sbConfig.getAttribute(HierarchyConstants.FILL_VALUE));
        if (null != sbConfig.getAttribute(HierarchyConstants.UNIQUE_NODE))         toReturn.put(HierarchyConstants.UNIQUE_NODE,sbConfig.getAttribute(HierarchyConstants.UNIQUE_NODE));
        if (null != sbConfig.getAttribute(HierarchyConstants.FORCE_NAME_AS_LEVEL))         toReturn.put(HierarchyConstants.FORCE_NAME_AS_LEVEL,sbConfig.getAttribute(HierarchyConstants.FORCE_NAME_AS_LEVEL));
      }
    }
  }
  return toReturn;
}

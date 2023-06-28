/** 
 * Create HierarchyTreeNode tree from datastore with leafs informations
 * @param dataStore
 * @param dimension
 * @param metadata
 * @return
 */
private HierarchyTreeNode createHierarchyTreeStructure(IDataStore dataStore,String dimension,IMetaData metadata){
  Set<String> allNodeCodes=new HashSet<String>();
  metadata=dataStore.getMetaData();
  Hierarchies hierarchies=HierarchiesSingleton.getInstance();
  String prefix=hierarchies.getPrefix(dimension);
  HashMap hierConfig=hierarchies.getConfig(dimension);
  int numLevels=Integer.parseInt((String)hierConfig.get(HierarchyConstants.NUM_LEVELS));
  String rootCode=null;
  IMetaData dsMeta=dataStore.getMetaData();
  HierarchyTreeNode root=null;
  Map<String,HierarchyTreeNode> attachedNodesMap=new HashMap<String,HierarchyTreeNode>();
  for (Iterator iterator=dataStore.iterator(); iterator.hasNext(); ) {
    String lastLevelCodeFound=null;
    String lastLevelNameFound=null;
    IRecord record=(IRecord)iterator.next();
    List<IField> recordFields=record.getFields();
    IField maxDepthField=record.getFieldAt(dsMeta.getFieldIndex(HierarchyConstants.MAX_DEPTH));
    int maxDepth=0;
    if (maxDepthField.getValue() instanceof Integer) {
      Integer maxDepthValue=(Integer)maxDepthField.getValue();
      maxDepth=maxDepthValue;
    }
 else     if (maxDepthField.getValue() instanceof Long) {
      Long maxDepthValue=(Long)maxDepthField.getValue();
      maxDepth=(int)(long)maxDepthValue;
    }
 else     if (maxDepthField.getValue() instanceof java.math.BigDecimal) {
      BigDecimal maxDepthValue=(BigDecimal)maxDepthField.getValue();
      maxDepth=maxDepthValue.intValue();
    }
    logger.debug("maxDepth: " + maxDepth);
    int lastValorizedLevel=0;
    Map<Integer,String> map=new TreeMap<Integer,String>();
    for (int i=numLevels; i > 0; i--) {
      String value=(String)record.getFieldAt(dsMeta.getFieldIndex(prefix + HierarchyConstants.SUFFIX_NM_LEV + i)).getValue();
      if (value != null && !value.isEmpty()) {
        map.put(i,value);
      }
    }
    HierarchyTreeNodeData data=null;
    IField codeField=record.getFieldAt(dsMeta.getFieldIndex((String)hierConfig.get(HierarchyConstants.TREE_NODE_CD) + 1));
    IField nameField=record.getFieldAt(dsMeta.getFieldIndex((String)hierConfig.get(HierarchyConstants.TREE_NODE_NM) + 1));
    String nodeCode=(String)codeField.getValue();
    String nodeName=(String)nameField.getValue();
    root=createHierarchyTreeStructure_extraction_1(dimension,metadata,allNodeCodes,hierarchies,prefix,hierConfig,dsMeta,root,attachedNodesMap,lastLevelCodeFound,record,maxDepth,lastValorizedLevel,map);
  }
  if (root != null)   logger.debug(TreeString.toString(root));
  return root;
}

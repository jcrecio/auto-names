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
    if (root == null) {
      HashMap rootAttrs=new HashMap();
      ArrayList<Field> generalFields=hierarchies.getHierarchy(dimension).getMetadataGeneralFields();
      for (int f=0, lf=generalFields.size(); f < lf; f++) {
        Field fld=generalFields.get(f);
        IField fldValue=record.getFieldAt(metadata.getFieldIndex(fld.getId() + ((fld.isSingleValue()) ? "" : 1)));
        rootAttrs.put(fld.getId(),(fld.getFixValue() != null) ? fld.getFixValue() : fldValue.getValue());
      }
      rootCode=(String)rootAttrs.get(HierarchyConstants.HIER_CD);
      nodeCode=String.valueOf(rootAttrs.get(HierarchyConstants.HIER_CD));
      nodeName=String.valueOf(rootAttrs.get(HierarchyConstants.HIER_NM));
      data=new HierarchyTreeNodeData(nodeCode,nodeName);
      root=new HierarchyTreeNode(data,rootCode,rootAttrs);
      if (!allNodeCodes.contains(nodeCode)) {
        allNodeCodes.add(nodeCode);
      }
    }
    Iterator<Entry<Integer,String>> it=map.entrySet().iterator();
    while (it.hasNext()) {
      Entry<Integer,String> entry=it.next();
      HashMap mapAttrs=null;
      int i=entry.getKey();
      codeField=record.getFieldAt(dsMeta.getFieldIndex((String)hierConfig.get(HierarchyConstants.TREE_NODE_CD) + i));
      nameField=record.getFieldAt(dsMeta.getFieldIndex((String)hierConfig.get(HierarchyConstants.TREE_NODE_NM) + i));
      nodeCode=(String)codeField.getValue();
      nodeName=(String)nameField.getValue();
      data=new HierarchyTreeNodeData(nodeCode,nodeName);
      if (it.hasNext()) {
        ArrayList<Field> nodeFields=hierarchies.getHierarchy(dimension).getMetadataNodeFields();
        mapAttrs=data.getAttributes();
        mapAttrs.put(HierarchyConstants.LEVEL,i);
        mapAttrs.put(HierarchyConstants.MAX_DEPTH,maxDepth);
        data.setAttributes(mapAttrs);
        for (int f=0; f < nodeFields.size(); f++) {
          Field fld=nodeFields.get(f);
          IField fldValue=record.getFieldAt(metadata.getFieldIndex(fld.getId() + ((fld.isSingleValue()) ? "" : i)));
          if (fld.isOrderField()) {
            Object value=(fld.getFixValue() != null) ? fld.getFixValue() : fldValue.getValue();
            logger.debug("id: [" + (fld.getId() + i) + "], value: ["+ value+ "]");
            mapAttrs.put(fld.getId() + i,value);
          }
          Object value=(fld.getFixValue() != null) ? fld.getFixValue() : fldValue.getValue();
          mapAttrs.put(fld.getId(),value);
          logger.debug("id: [" + (fld.getId()) + "], value: ["+ value+ "]");
        }
        data.setAttributes(mapAttrs);
        attachNodeToLevel(root,nodeCode,lastLevelCodeFound,lastValorizedLevel,data,allNodeCodes,false,record,dsMeta,prefix,attachedNodesMap);
        lastValorizedLevel=i;
        lastLevelCodeFound=nodeCode;
        lastLevelNameFound=nodeName;
      }
 else {
        data=HierarchyUtils.setDataValues(dimension,nodeCode,data,record,metadata);
        mapAttrs=data.getAttributes();
        mapAttrs.put(HierarchyConstants.LEVEL,i);
        mapAttrs.put(HierarchyConstants.MAX_DEPTH,maxDepth);
        data.setAttributes(mapAttrs);
        attachNodeToLevel(root,nodeCode,lastLevelCodeFound,lastValorizedLevel,data,allNodeCodes,true,record,dsMeta,prefix,attachedNodesMap);
        lastValorizedLevel=i;
        lastLevelCodeFound=nodeCode;
        lastLevelNameFound=nodeName;
      }
    }
  }
  if (root != null)   logger.debug(TreeString.toString(root));
  return root;
}

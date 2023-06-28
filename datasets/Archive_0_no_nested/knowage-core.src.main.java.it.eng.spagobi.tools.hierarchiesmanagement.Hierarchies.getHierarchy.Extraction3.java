/** 
 * Get the hierarchy properties of the passed dimension
 * @param dimension the dimension name
 * @return the hierarchy object linked to the input dimension with all attributes
 */
public Hierarchy getHierarchy(String dimension){
  Hierarchy toReturn=new Hierarchy(dimension);
  SourceBean sb=getTemplate();
  SourceBean dimensions=(SourceBean)sb.getAttribute(HierarchyConstants.DIMENSIONS);
  List lst=dimensions.getAttributeAsList(HierarchyConstants.DIMENSION);
  for (Iterator iterator=lst.iterator(); iterator.hasNext(); ) {
    SourceBean sbRow=(SourceBean)iterator.next();
    String dimensionLabel=sbRow.getAttribute(HierarchyConstants.LABEL) != null ? sbRow.getAttribute(HierarchyConstants.LABEL).toString() : null;
    if (dimensionLabel.equalsIgnoreCase(dimension)) {
      List lstGeneralFields=sbRow.getAttributeAsList(HierarchyConstants.HIER_FIELDS + "." + HierarchyConstants.GENERAL_FIELDS+ "."+ HierarchyConstants.FIELD);
      ArrayList<Field> metadataGeneralHierarchy=new ArrayList<Field>();
      for (Iterator iter=lstGeneralFields.iterator(); iter.hasNext(); ) {
        Field field=getHierarchy_extraction_1(iter);
        metadataGeneralHierarchy.add(field);
      }
      toReturn.setMetadataGeneralFields(metadataGeneralHierarchy);
      List lstNodeFields=sbRow.getAttributeAsList(HierarchyConstants.HIER_FIELDS + "." + HierarchyConstants.NODE_FIELDS+ "."+ HierarchyConstants.FIELD);
      ArrayList<Field> metadataNodeHierarchy=new ArrayList<Field>();
      for (Iterator iter=lstNodeFields.iterator(); iter.hasNext(); ) {
        SourceBean sbField=(SourceBean)iter.next();
        getHierarchy_extraction_2(metadataNodeHierarchy,sbField);
      }
      toReturn.setMetadataNodeFields(metadataNodeHierarchy);
      List lstLeafFields=sbRow.getAttributeAsList(HierarchyConstants.HIER_FIELDS + "." + HierarchyConstants.LEAF_FIELDS+ "."+ HierarchyConstants.FIELD);
      ArrayList<Field> metadataLeafHierarchy=new ArrayList<Field>();
      for (Iterator iter=lstLeafFields.iterator(); iter.hasNext(); ) {
        Field field=getHierarchy_extraction_3(iter);
        metadataLeafHierarchy.add(field);
      }
      toReturn.setMetadataLeafFields(metadataLeafHierarchy);
    }
  }
  return toReturn;
}

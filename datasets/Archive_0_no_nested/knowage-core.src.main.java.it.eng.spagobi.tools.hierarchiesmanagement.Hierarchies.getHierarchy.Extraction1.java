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
        SourceBean sbField=(SourceBean)iter.next();
        String fieldId=sbField.getAttribute(HierarchyConstants.FIELD_ID) != null ? sbField.getAttribute(HierarchyConstants.FIELD_ID).toString() : null;
        String fieldName=sbField.getAttribute(HierarchyConstants.FIELD_NAME) != null ? sbField.getAttribute(HierarchyConstants.FIELD_NAME).toString() : null;
        String fieldType=sbField.getAttribute(HierarchyConstants.FIELD_TYPE) != null ? sbField.getAttribute(HierarchyConstants.FIELD_TYPE).toString() : null;
        String fieldFixValue=sbField.getAttribute(HierarchyConstants.FIELD_FIX_VALUE) != null ? sbField.getAttribute(HierarchyConstants.FIELD_FIX_VALUE).toString() : null;
        boolean fieldIsVisible=sbField.getAttribute(HierarchyConstants.FIELD_VISIBLE) != null ? Boolean.parseBoolean((String)sbField.getAttribute(HierarchyConstants.FIELD_VISIBLE)) : false;
        boolean fieldIsEditable=sbField.getAttribute(HierarchyConstants.FIELD_EDITABLE) != null ? Boolean.parseBoolean((String)sbField.getAttribute(HierarchyConstants.FIELD_EDITABLE)) : false;
        boolean fieldIsRequired=sbField.getAttribute(HierarchyConstants.FIELD_REQUIRED) != null ? Boolean.parseBoolean((String)sbField.getAttribute(HierarchyConstants.FIELD_REQUIRED)) : false;
        boolean fieldIsSingleValue=sbField.getAttribute(HierarchyConstants.FIELD_SINGLE_VALUE) != null ? Boolean.parseBoolean((String)sbField.getAttribute(HierarchyConstants.FIELD_SINGLE_VALUE)) : true;
        boolean fieldIsParent=sbField.getAttribute(HierarchyConstants.FIELD_PARENT) != null ? Boolean.parseBoolean((String)sbField.getAttribute(HierarchyConstants.FIELD_PARENT)) : false;
        boolean fieldIsUnique=sbField.getAttribute(HierarchyConstants.FIELD_UNIQUE_CODE) != null ? Boolean.parseBoolean((String)sbField.getAttribute(HierarchyConstants.FIELD_UNIQUE_CODE)) : false;
        boolean fieldIsOrder=sbField.getAttribute(HierarchyConstants.FIELD_IS_ORDER) != null ? Boolean.parseBoolean((String)sbField.getAttribute(HierarchyConstants.FIELD_IS_ORDER)) : false;
        Field field=new Field(fieldId,fieldName,fieldType,fieldFixValue,fieldIsVisible,fieldIsEditable,fieldIsRequired,fieldIsSingleValue,fieldIsParent,fieldIsUnique,fieldIsOrder);
        metadataGeneralHierarchy.add(field);
      }
      toReturn.setMetadataGeneralFields(metadataGeneralHierarchy);
      List lstNodeFields=sbRow.getAttributeAsList(HierarchyConstants.HIER_FIELDS + "." + HierarchyConstants.NODE_FIELDS+ "."+ HierarchyConstants.FIELD);
      ArrayList<Field> metadataNodeHierarchy=new ArrayList<Field>();
      for (Iterator iter=lstNodeFields.iterator(); iter.hasNext(); ) {
        SourceBean sbField=(SourceBean)iter.next();
        String fieldId=sbField.getAttribute(HierarchyConstants.FIELD_ID) != null ? sbField.getAttribute(HierarchyConstants.FIELD_ID).toString() : null;
        String fieldName=sbField.getAttribute(HierarchyConstants.FIELD_NAME) != null ? sbField.getAttribute(HierarchyConstants.FIELD_NAME).toString() : null;
        String fieldType=sbField.getAttribute(HierarchyConstants.FIELD_TYPE) != null ? sbField.getAttribute(HierarchyConstants.FIELD_TYPE).toString() : null;
        String fieldFixValue=sbField.getAttribute(HierarchyConstants.FIELD_FIX_VALUE) != null ? sbField.getAttribute(HierarchyConstants.FIELD_FIX_VALUE).toString() : null;
        boolean fieldIsVisible=sbField.getAttribute(HierarchyConstants.FIELD_VISIBLE) != null ? Boolean.parseBoolean((String)sbField.getAttribute(HierarchyConstants.FIELD_VISIBLE)) : false;
        boolean fieldIsEditable=sbField.getAttribute(HierarchyConstants.FIELD_EDITABLE) != null ? Boolean.parseBoolean((String)sbField.getAttribute(HierarchyConstants.FIELD_EDITABLE)) : false;
        boolean fieldIsRequired=sbField.getAttribute(HierarchyConstants.FIELD_REQUIRED) != null ? Boolean.parseBoolean((String)sbField.getAttribute(HierarchyConstants.FIELD_REQUIRED)) : false;
        boolean fieldIsSingleValue=sbField.getAttribute(HierarchyConstants.FIELD_SINGLE_VALUE) != null ? Boolean.parseBoolean((String)sbField.getAttribute(HierarchyConstants.FIELD_SINGLE_VALUE)) : true;
        boolean fieldIsParent=sbField.getAttribute(HierarchyConstants.FIELD_PARENT) != null ? Boolean.parseBoolean((String)sbField.getAttribute(HierarchyConstants.FIELD_PARENT)) : false;
        boolean fieldIsUnique=sbField.getAttribute(HierarchyConstants.FIELD_UNIQUE_CODE) != null ? Boolean.parseBoolean((String)sbField.getAttribute(HierarchyConstants.FIELD_UNIQUE_CODE)) : false;
        boolean fieldIsOrder=sbField.getAttribute(HierarchyConstants.FIELD_IS_ORDER) != null ? Boolean.parseBoolean((String)sbField.getAttribute(HierarchyConstants.FIELD_IS_ORDER)) : false;
        Field field=new Field(fieldId,fieldName,fieldType,fieldFixValue,fieldIsVisible,fieldIsEditable,fieldIsRequired,fieldIsSingleValue,fieldIsParent,fieldIsUnique,fieldIsOrder);
        metadataNodeHierarchy.add(field);
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

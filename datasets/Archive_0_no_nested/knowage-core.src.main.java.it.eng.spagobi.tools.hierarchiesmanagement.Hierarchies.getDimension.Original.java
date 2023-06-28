/** 
 * Get the dimension properties of the passed dimension
 * @param dimension the dimension name
 * @return the dimension object with all attributes
 */
public Dimension getDimension(String dimension){
  Dimension toReturn=new Dimension(dimension);
  SourceBean sb=getTemplate();
  SourceBean dimensions=(SourceBean)sb.getAttribute(HierarchyConstants.DIMENSIONS);
  List lst=dimensions.getAttributeAsList(HierarchyConstants.DIMENSION);
  for (Iterator iterator=lst.iterator(); iterator.hasNext(); ) {
    SourceBean sbRow=(SourceBean)iterator.next();
    String dimensionLabel=sbRow.getAttribute(HierarchyConstants.LABEL) != null ? sbRow.getAttribute(HierarchyConstants.LABEL).toString() : null;
    if (dimensionLabel.equalsIgnoreCase(dimension)) {
      toReturn.setName(sbRow.getAttribute(HierarchyConstants.NAME) != null ? sbRow.getAttribute(HierarchyConstants.NAME).toString() : null);
      List lstFields=sbRow.getAttributeAsList(HierarchyConstants.DIM_FIELDS + "." + HierarchyConstants.FIELD);
      ArrayList<Field> metadataDimension=new ArrayList<Field>();
      for (Iterator iter=lstFields.iterator(); iter.hasNext(); ) {
        SourceBean sbField=(SourceBean)iter.next();
        String fieldId=sbField.getAttribute(HierarchyConstants.FIELD_ID) != null ? sbField.getAttribute(HierarchyConstants.FIELD_ID).toString() : null;
        String fieldName=sbField.getAttribute(HierarchyConstants.FIELD_NAME) != null ? sbField.getAttribute(HierarchyConstants.FIELD_NAME).toString() : null;
        String fieldType=sbField.getAttribute(HierarchyConstants.FIELD_TYPE) != null ? sbField.getAttribute(HierarchyConstants.FIELD_TYPE).toString() : null;
        String fixValue=sbField.getAttribute(HierarchyConstants.FIELD_FIX_VALUE) != null ? sbField.getAttribute(HierarchyConstants.FIELD_FIX_VALUE).toString() : null;
        boolean fieldIsVisible=sbField.getAttribute(HierarchyConstants.FIELD_VISIBLE) != null ? Boolean.parseBoolean((String)sbField.getAttribute(HierarchyConstants.FIELD_VISIBLE)) : false;
        boolean fieldIsEditable=sbField.getAttribute(HierarchyConstants.FIELD_EDITABLE) != null ? Boolean.parseBoolean((String)sbField.getAttribute(HierarchyConstants.FIELD_EDITABLE)) : false;
        boolean fieldIsRequired=sbField.getAttribute(HierarchyConstants.FIELD_REQUIRED) != null ? Boolean.parseBoolean((String)sbField.getAttribute(HierarchyConstants.FIELD_REQUIRED)) : false;
        boolean fieldIsSingleValue=sbField.getAttribute(HierarchyConstants.FIELD_SINGLE_VALUE) != null ? Boolean.parseBoolean((String)sbField.getAttribute(HierarchyConstants.FIELD_SINGLE_VALUE)) : true;
        boolean fieldIsParent=sbField.getAttribute(HierarchyConstants.FIELD_PARENT) != null ? Boolean.parseBoolean((String)sbField.getAttribute(HierarchyConstants.FIELD_PARENT)) : false;
        boolean fieldIsUnique=sbField.getAttribute(HierarchyConstants.FIELD_UNIQUE_CODE) != null ? Boolean.parseBoolean((String)sbField.getAttribute(HierarchyConstants.FIELD_UNIQUE_CODE)) : false;
        boolean fieldIsOrder=sbField.getAttribute(HierarchyConstants.FIELD_IS_ORDER) != null ? Boolean.parseBoolean((String)sbField.getAttribute(HierarchyConstants.FIELD_IS_ORDER)) : false;
        Field field=new Field(fieldId,fieldName,fieldType,fixValue,fieldIsVisible,fieldIsEditable,fieldIsRequired,fieldIsSingleValue,fieldIsParent,fieldIsUnique,fieldIsOrder);
        metadataDimension.add(field);
      }
      toReturn.setMetadataFields(metadataDimension);
      List lstFilters=sbRow.getAttributeAsList(HierarchyConstants.DIM_FILTERS + "." + HierarchyConstants.FILTER);
      ArrayList<Filter> metadataFilterDim=new ArrayList<Filter>();
      for (Iterator iter=lstFilters.iterator(); iter.hasNext(); ) {
        SourceBean sbFilter=(SourceBean)iter.next();
        String filterName=sbFilter.getAttribute(HierarchyConstants.FILTER_NAME) != null ? sbFilter.getAttribute(HierarchyConstants.FILTER_NAME).toString() : null;
        String filterType=sbFilter.getAttribute(HierarchyConstants.FILTER_TYPE) != null ? sbFilter.getAttribute(HierarchyConstants.FILTER_TYPE).toString() : null;
        String filterDefault=sbFilter.getAttribute(HierarchyConstants.FILTER_DEFAULT) != null ? sbFilter.getAttribute(HierarchyConstants.FILTER_DEFAULT).toString() : null;
        boolean checkCondition=true;
        int idx=0;
        LinkedHashMap<String,String> conditions=new LinkedHashMap<String,String>();
        while (checkCondition) {
          idx++;
          if (sbFilter.getAttribute(HierarchyConstants.FILTER_CONDITION + idx) != null) {
            String condition=sbFilter.getAttribute(HierarchyConstants.FILTER_CONDITION + idx).toString();
            conditions.put(HierarchyConstants.FILTER_CONDITION + idx,condition);
          }
 else {
            if (conditions.size() == 0) {
              logger.error("The dimension has the filter " + filterName + " without valid conditions! No optional filter will be added on the GUI. Check the template!! ");
            }
            break;
          }
        }
        Filter filter=new Filter(filterName,filterType,filterDefault,conditions);
        metadataFilterDim.add(filter);
      }
      toReturn.setMetadataFilters(metadataFilterDim);
    }
  }
  return toReturn;
}

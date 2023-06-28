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
        Field field=getDimension_extraction_1(iter);
        metadataDimension.add(field);
      }
      toReturn.setMetadataFields(metadataDimension);
      List lstFilters=sbRow.getAttributeAsList(HierarchyConstants.DIM_FILTERS + "." + HierarchyConstants.FILTER);
      ArrayList<Filter> metadataFilterDim=new ArrayList<Filter>();
      for (Iterator iter=lstFilters.iterator(); iter.hasNext(); ) {
        getDimension_extraction_2(metadataFilterDim,iter);
      }
      toReturn.setMetadataFilters(metadataFilterDim);
    }
  }
  return toReturn;
}

/** 
 * Filters the list with a list of filtering values.
 * @param list The list to be filtered
 * @param valuesfilter The list of filtering values
 * @param valuetypefilter The type of the value of the filter (STRING/NUM/DATE)
 * @param columnfilter The column to be filtered
 * @param typeFilter The type of the filter
 * @param errorHandler The EMFErrorHandler object, in which errors are stored if they occurs
 * @return the filtered list
 */
public static ListIFace filterList(ListIFace list,List valuesfilter,String valuetypefilter,String columnfilter,String typeFilter,EMFErrorHandler errorHandler){
  if ((valuesfilter == null) || (valuesfilter.size() == 0)) {
    return list;
  }
  if ((columnfilter == null) || (columnfilter.trim().equals(""))) {
    return list;
  }
  if ((typeFilter == null) || (typeFilter.trim().equals(""))) {
    return list;
  }
  if ((valuetypefilter == null) || (valuetypefilter.trim().equals(""))) {
    return list;
  }
  if (typeFilter.equalsIgnoreCase(SpagoBIConstants.LESS_FILTER) || typeFilter.equalsIgnoreCase(SpagoBIConstants.LESS_OR_EQUAL_FILTER) || typeFilter.equalsIgnoreCase(SpagoBIConstants.GREATER_FILTER)|| typeFilter.equalsIgnoreCase(SpagoBIConstants.GREATER_OR_EQUAL_FILTER)) {
    TracerSingleton.log(Constants.NOME_MODULO,TracerSingleton.WARNING,"DelegatedBasicListService::filterList with a list of filtering values: the filter type " + typeFilter + " is not applicable for multi-values filtering.");
    String labelTypeFilter="";
    if (typeFilter.equalsIgnoreCase(SpagoBIConstants.LESS_FILTER))     labelTypeFilter=PortletUtilities.getMessage("SBIListLookPage.isLessThan","messages");
 else     if (typeFilter.equalsIgnoreCase(SpagoBIConstants.LESS_OR_EQUAL_FILTER))     labelTypeFilter=PortletUtilities.getMessage("SBIListLookPage.isLessOrEqualThan","messages");
 else     if (typeFilter.equalsIgnoreCase(SpagoBIConstants.GREATER_FILTER))     labelTypeFilter=PortletUtilities.getMessage("SBIListLookPage.isGreaterThan","messages");
 else     if (typeFilter.equalsIgnoreCase(SpagoBIConstants.GREATER_OR_EQUAL_FILTER))     labelTypeFilter=PortletUtilities.getMessage("SBIListLookPage.isGreaterOrEqualThan","messages");
    HashMap params=new HashMap();
    params.put(Constants.NOME_MODULO,"DelegatedBasicListService::filterList with a list of filtering values");
    Vector v=new Vector();
    v.add(labelTypeFilter);
    EMFValidationError error=new EMFValidationError(EMFErrorSeverity.WARNING,SpagoBIConstants.TYPE_FILTER,"1069",v,params);
    errorHandler.addError(error);
    return list;
  }
  boolean filterConditionsAreCorrect=verifyFilterConditions(valuetypefilter,typeFilter,errorHandler);
  if (!filterConditionsAreCorrect)   return list;
  PaginatorIFace newPaginator=new GenericPaginator();
  newPaginator.setPageSize(list.getPaginator().getPageSize());
  SourceBean allrowsSB=list.getPaginator().getAll();
  List rows=allrowsSB.getAttributeAsList("ROW");
  Iterator iterRow=rows.iterator();
  while (iterRow.hasNext()) {
    SourceBean row=(SourceBean)iterRow.next();
    boolean doesRowSatisfyCondition=false;
    Iterator valuesfilterIt=valuesfilter.iterator();
    while (valuesfilterIt.hasNext()) {
      String valuefilter=(String)valuesfilterIt.next();
      try {
        if (valuefilter != null && !valuefilter.equals(""))         doesRowSatisfyCondition=doesRowSatisfyCondition(row,valuefilter,valuetypefilter,columnfilter,typeFilter);
 else         doesRowSatisfyCondition=true;
      }
 catch (      EMFValidationError error) {
        errorHandler.addError(error);
        return list;
      }
      if (doesRowSatisfyCondition)       break;
    }
    filterList_extraction_2(newPaginator,row,doesRowSatisfyCondition);
  }
  ListIFace newList=new GenericList();
  newList.setPaginator(newPaginator);
  return newList;
}

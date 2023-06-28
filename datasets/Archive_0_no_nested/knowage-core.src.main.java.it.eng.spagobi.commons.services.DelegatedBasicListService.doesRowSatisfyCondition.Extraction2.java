private static boolean doesRowSatisfyCondition(SourceBean row,String valuefilter,String valuetypefilter,String columnfilter,String typeFilter) throws EMFValidationError {
  Object attribute=row.getAttribute(columnfilter);
  if (attribute == null)   return false;
  String value=attribute.toString();
  if (value == null)   value="";
  if (valuetypefilter.equalsIgnoreCase(SpagoBIConstants.STRING_TYPE_FILTER)) {
    valuefilter=valuefilter.toUpperCase();
    value=value.toUpperCase();
    if (typeFilter.equalsIgnoreCase(SpagoBIConstants.START_FILTER)) {
      return value.trim().startsWith(valuefilter);
    }
 else     if (typeFilter.equalsIgnoreCase(SpagoBIConstants.END_FILTER)) {
      return value.trim().endsWith(valuefilter);
    }
 else     if (typeFilter.equalsIgnoreCase(SpagoBIConstants.CONTAIN_FILTER)) {
      return value.indexOf(valuefilter) != -1;
    }
 else     if (typeFilter.equalsIgnoreCase(SpagoBIConstants.EQUAL_FILTER)) {
      return value.equals(valuefilter) || value.trim().equals(valuefilter);
    }
 else     if (typeFilter.equalsIgnoreCase(SpagoBIConstants.LESS_FILTER)) {
      return value.trim().compareToIgnoreCase(valuefilter) < 0;
    }
 else     if (typeFilter.equalsIgnoreCase(SpagoBIConstants.LESS_OR_EQUAL_FILTER)) {
      return value.trim().compareToIgnoreCase(valuefilter) <= 0;
    }
 else     if (typeFilter.equalsIgnoreCase(SpagoBIConstants.GREATER_FILTER)) {
      return value.trim().compareToIgnoreCase(valuefilter) > 0;
    }
 else     if (typeFilter.equalsIgnoreCase(SpagoBIConstants.GREATER_OR_EQUAL_FILTER)) {
      return value.trim().compareToIgnoreCase(valuefilter) >= 0;
    }
 else {
      TracerSingleton.log(Constants.NOME_MODULO,TracerSingleton.WARNING,"DelegatedBasicListService::filterList: the filter type '" + typeFilter + "' is not a valid filter type");
      HashMap params=new HashMap();
      params.put(Constants.NOME_MODULO,"DelegatedBasicListService::filterList: the filter type '" + typeFilter + "' is not a valid filter type");
      EMFValidationError error=new EMFValidationError(EMFErrorSeverity.ERROR,SpagoBIConstants.TYPE_FILTER,"100",null,params);
      throw error;
    }
  }
 else   if (valuetypefilter.equalsIgnoreCase(SpagoBIConstants.NUMBER_TYPE_FILTER)) {
    return doesRowSatisfyCondition_extraction_1(valuefilter,typeFilter,value);
  }
 else   return doesRowSatisfyCondition_extraction_2(valuefilter,valuetypefilter,typeFilter,value);
}

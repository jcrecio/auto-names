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
    Double valueDouble=null;
    Double valueFilterDouble=null;
    try {
      valueDouble=new Double(value);
    }
 catch (    Exception e) {
      TracerSingleton.log(Constants.NOME_MODULO,TracerSingleton.WARNING,"DelegatedBasicListService::filterList: the string value is not a recognizable number representations: value to be filtered = " + value,e);
      HashMap params=new HashMap();
      params.put(Constants.NOME_MODULO,"DelegatedBasicListService::filterList");
      Vector v=new Vector();
      v.add(value);
      EMFValidationError error=new EMFValidationError(EMFErrorSeverity.WARNING,SpagoBIConstants.TYPE_VALUE_FILTER,"1051",v,params);
      throw error;
    }
    try {
      valueFilterDouble=new Double(valuefilter);
    }
 catch (    Exception e) {
      TracerSingleton.log(Constants.NOME_MODULO,TracerSingleton.WARNING,"DelegatedBasicListService::filterList: input string value is not a recognizable number representations: filter value = " + valuefilter,e);
      HashMap params=new HashMap();
      params.put(Constants.NOME_MODULO,"DelegatedBasicListService::filterList");
      Vector v=new Vector();
      v.add(valuefilter);
      EMFValidationError error=new EMFValidationError(EMFErrorSeverity.WARNING,SpagoBIConstants.VALUE_FILTER,"1052",v,params);
      throw error;
    }
    if (typeFilter.equalsIgnoreCase(SpagoBIConstants.EQUAL_FILTER)) {
      return valueDouble.doubleValue() == valueFilterDouble.doubleValue();
    }
 else     if (typeFilter.equalsIgnoreCase(SpagoBIConstants.LESS_FILTER)) {
      return valueDouble.doubleValue() < valueFilterDouble.doubleValue();
    }
 else     if (typeFilter.equalsIgnoreCase(SpagoBIConstants.LESS_OR_EQUAL_FILTER)) {
      return valueDouble.doubleValue() <= valueFilterDouble.doubleValue();
    }
 else     if (typeFilter.equalsIgnoreCase(SpagoBIConstants.GREATER_FILTER)) {
      return valueDouble.doubleValue() > valueFilterDouble.doubleValue();
    }
 else     if (typeFilter.equalsIgnoreCase(SpagoBIConstants.GREATER_OR_EQUAL_FILTER)) {
      return valueDouble.doubleValue() >= valueFilterDouble.doubleValue();
    }
 else {
      TracerSingleton.log(Constants.NOME_MODULO,TracerSingleton.WARNING,"DelegatedBasicListService::filterList: the filter type '" + typeFilter + "' is not a valid filter type");
      HashMap params=new HashMap();
      params.put(Constants.NOME_MODULO,"DelegatedBasicListService::filterList: the filter type '" + typeFilter + "' is not a valid filter type");
      EMFValidationError error=new EMFValidationError(EMFErrorSeverity.ERROR,SpagoBIConstants.TYPE_FILTER,"100",null,params);
      throw error;
    }
  }
 else   if (valuetypefilter.equalsIgnoreCase(SpagoBIConstants.DATE_TYPE_FILTER)) {
    String format=SingletonConfig.getInstance().getConfigValue("SPAGOBI.DATE-FORMAT-SERVER.format");
    TracerSingleton.log(Constants.NOME_MODULO,TracerSingleton.WARNING,"DelegatedBasicListService::filterList: applying date format " + format + " for filtering.");
    Date valueDate=null;
    Date valueFilterDate=null;
    try {
      valueDate=toDate(value,format);
    }
 catch (    Exception e) {
      TracerSingleton.log(Constants.NOME_MODULO,TracerSingleton.WARNING,"DelegatedBasicListService::filterList: the string value is not a valid date representation according to the format " + format + ": value to be filtered = "+ value,e);
      HashMap params=new HashMap();
      params.put(Constants.NOME_MODULO,"DelegatedBasicListService::filterList");
      Vector v=new Vector();
      v.add(value);
      v.add(format);
      EMFValidationError error=new EMFValidationError(EMFErrorSeverity.WARNING,SpagoBIConstants.TYPE_VALUE_FILTER,"1054",v,params);
      throw error;
    }
    try {
      valueFilterDate=toDate(valuefilter,format);
    }
 catch (    Exception e) {
      TracerSingleton.log(Constants.NOME_MODULO,TracerSingleton.WARNING,"DelegatedBasicListService::filterList: input string is not a valid date representation according to the format " + format + ": filter value = "+ valuefilter,e);
      HashMap params=new HashMap();
      params.put(Constants.NOME_MODULO,"DelegatedBasicListService::filterList");
      Vector v=new Vector();
      v.add(valuefilter);
      v.add(format);
      EMFValidationError error=new EMFValidationError(EMFErrorSeverity.WARNING,SpagoBIConstants.VALUE_FILTER,"1055",v,params);
      throw error;
    }
    if (typeFilter.equalsIgnoreCase(SpagoBIConstants.EQUAL_FILTER)) {
      return valueDate.compareTo(valueFilterDate) == 0;
    }
 else     if (typeFilter.equalsIgnoreCase(SpagoBIConstants.LESS_FILTER)) {
      return valueDate.compareTo(valueFilterDate) < 0;
    }
 else     if (typeFilter.equalsIgnoreCase(SpagoBIConstants.LESS_OR_EQUAL_FILTER)) {
      return valueDate.compareTo(valueFilterDate) <= 0;
    }
 else     if (typeFilter.equalsIgnoreCase(SpagoBIConstants.GREATER_FILTER)) {
      return valueDate.compareTo(valueFilterDate) > 0;
    }
 else     if (typeFilter.equalsIgnoreCase(SpagoBIConstants.GREATER_OR_EQUAL_FILTER)) {
      return valueDate.compareTo(valueFilterDate) >= 0;
    }
 else {
      TracerSingleton.log(Constants.NOME_MODULO,TracerSingleton.WARNING,"DelegatedBasicListService::filterList: the filter type '" + typeFilter + "' is not a valid filter type");
      HashMap params=new HashMap();
      params.put(Constants.NOME_MODULO,"DelegatedBasicListService::filterList: the filter type '" + typeFilter + "' is not a valid filter type");
      EMFValidationError error=new EMFValidationError(EMFErrorSeverity.ERROR,SpagoBIConstants.TYPE_FILTER,"100",null,params);
      throw error;
    }
  }
 else {
    TracerSingleton.log(Constants.NOME_MODULO,TracerSingleton.WARNING,"DelegatedBasicListService::filterList: the filter value type '" + valuetypefilter + "' is not a valid filter value type");
    HashMap params=new HashMap();
    params.put(Constants.NOME_MODULO,"DelegatedBasicListService::filterList: the filter value type '" + valuetypefilter + "' is not a valid filter value type");
    EMFValidationError error=new EMFValidationError(EMFErrorSeverity.ERROR,SpagoBIConstants.TYPE_FILTER,"100",null,params);
    throw error;
  }
}

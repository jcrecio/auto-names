/** 
 * COnverts from BIObject Parameters to a map, in presence of multi value merge with ,
 * @param obj
 * @return
 */
public Map getParameters(BIObject obj){
  HashMap parametersMap=null;
  List parametersList=obj.getDrivers();
  logger.debug("Check for BIparameters and relative values");
  if (parametersList != null) {
    parametersMap=new HashMap();
    for (Iterator iterator=parametersList.iterator(); iterator.hasNext(); ) {
      BIObjectParameter par=(BIObjectParameter)iterator.next();
      String url=par.getParameterUrlName();
      List values=par.getParameterValues();
      if (values != null) {
        if (values.size() == 1) {
          String value=(String)values.get(0);
          Parameter parameter=par.getParameter();
          if (parameter != null) {
            String parType=parameter.getType();
            if (parType.equalsIgnoreCase(SpagoBIConstants.STRING_TYPE_FILTER) || parType.equalsIgnoreCase(SpagoBIConstants.DATE_TYPE_FILTER)) {
              value=value;
            }
          }
          parametersMap.put(url,value);
        }
 else         if (values.size() >= 1) {
          String type=(par.getParameter() != null) ? par.getParameter().getType() : SpagoBIConstants.STRING_TYPE_FILTER;
          String value="";
          value=getParameters_extraction_2(values,type);
          parametersMap.put(url,value);
        }
      }
    }
  }
  return parametersMap;
}

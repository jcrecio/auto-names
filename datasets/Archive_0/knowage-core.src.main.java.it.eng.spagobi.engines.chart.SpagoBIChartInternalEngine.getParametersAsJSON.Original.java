/** 
 * COnverts from BIObject Parameters to a map, in presence of multi value merge with ,
 * @param obj
 * @return
 */
public JSONArray getParametersAsJSON(BIObject obj){
  JSONArray JSONPars=new JSONArray();
  List parametersList=obj.getDrivers();
  logger.debug("Check for BIparameters and relative values");
  if (parametersList != null) {
    for (Iterator iterator=parametersList.iterator(); iterator.hasNext(); ) {
      BIObjectParameter par=(BIObjectParameter)iterator.next();
      String name=par.getParameterUrlName();
      String value="";
      List values=par.getParameterValues();
      if (values != null) {
        if (values.size() == 1) {
          value=(String)values.get(0);
          Parameter parameter=par.getParameter();
          if (parameter != null) {
            String parType=parameter.getType();
            if (parType.equalsIgnoreCase(SpagoBIConstants.STRING_TYPE_FILTER) || parType.equalsIgnoreCase(SpagoBIConstants.DATE_TYPE_FILTER)) {
              value=value;
            }
          }
        }
 else         if (values.size() >= 1) {
          String type=(par.getParameter() != null) ? par.getParameter().getType() : SpagoBIConstants.STRING_TYPE_FILTER;
          if (type.equalsIgnoreCase(SpagoBIConstants.STRING_TYPE_FILTER) || type.equalsIgnoreCase(SpagoBIConstants.DATE_TYPE_FILTER)) {
            value=(String)values.get(0);
            for (int k=1; k < values.size(); k++) {
              value=value + "," + (String)values.get(k);
            }
          }
 else {
            value=(String)values.get(0);
            for (int k=1; k < values.size(); k++) {
              value=value + "," + (String)values.get(k)+ "";
            }
          }
        }
        try {
          JSONObject JSONObj=new JSONObject();
          JSONObj.put("name",name);
          JSONObj.put("value",value);
          JSONPars.put(JSONObj);
        }
 catch (        Exception e) {
          logger.warn("Impossible to load parameter object " + name + " whose value is "+ value+ " to JSONObject",e);
        }
      }
    }
  }
  return JSONPars;
}

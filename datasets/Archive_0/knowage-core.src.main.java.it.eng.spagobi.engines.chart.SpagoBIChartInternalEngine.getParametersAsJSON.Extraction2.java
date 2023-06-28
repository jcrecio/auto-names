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
        value=getParametersAsJSON_extraction_1(par,value,values);
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

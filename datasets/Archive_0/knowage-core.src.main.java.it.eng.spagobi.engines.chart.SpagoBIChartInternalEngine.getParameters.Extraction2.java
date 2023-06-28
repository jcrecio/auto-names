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
      getParameters_extraction_1(parametersMap,iterator);
    }
  }
  return parametersMap;
}

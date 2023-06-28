/** 
 * General management for normal and filter parameters
 * @param requestContainer
 * @param responseContainer
 * @param parameter
 * @param inputParameters
 * @param dataConnection
 * @param isFilterParameter
 * @param statement
 * @param condizioneSql
 * @return
 */
protected static boolean handleParameter(final RequestContainer requestContainer,final ResponseContainer responseContainer,final SourceBean parameter,ArrayList inputParameters,DataConnection dataConnection,final boolean isFilterParameter,StringBuffer statement,final String condizioneSql){
  logger.debug("IN");
  boolean parameterUsed=false;
  String parameterType=(String)parameter.getAttribute("TYPE");
  String parameterValue=(String)parameter.getAttribute("VALUE");
  String parameterScope=(String)parameter.getAttribute("SCOPE");
  Object inParameterValue=null;
  boolean skipParameterInsertion=false;
  String dialect=HibernateSessionManager.getDialect();
  if (parameterType.equalsIgnoreCase("TRUE_VALUE")) {
    inParameterValue=handleParameter_extraction_1(inputParameters,dataConnection,inParameterValue,dialect);
    skipParameterInsertion=true;
    parameterUsed=true;
  }
 else   if (parameterType.equalsIgnoreCase("ABSOLUTE"))   inParameterValue=parameterValue;
 else {
    Object parameterValueObject=ContextScooping.getScopedParameter(requestContainer,responseContainer,parameterValue,parameterScope,parameter);
    if (parameterValueObject != null) {
      if (dialect.contains("PostgreSQL")) {
        inParameterValue=parameterValueObject;
      }
 else       inParameterValue=parameterValueObject.toString();
    }
  }
  if (inParameterValue == null)   inParameterValue="";
  if (!skipParameterInsertion) {
    parameterUsed=handleParameter_extraction_2(parameter,inputParameters,dataConnection,isFilterParameter,statement,condizioneSql,parameterUsed,inParameterValue,dialect);
  }
  logger.debug("OUT");
  return parameterUsed;
}

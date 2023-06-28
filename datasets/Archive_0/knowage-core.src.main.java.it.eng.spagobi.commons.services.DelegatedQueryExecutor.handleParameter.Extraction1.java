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
    if (dialect != null) {
      String dialectName=dialect;
      if (dialectName.contains("PostgreSQL")) {
        inputParameters.add(dataConnection.createDataField("",Types.BOOLEAN,true));
      }
 else {
        inParameterValue="1";
        inputParameters.add(dataConnection.createDataField("",Types.VARCHAR,inParameterValue));
      }
    }
 else {
      try {
        DatabaseMetaData dbMetadata=null;
        try {
          Connection connection=dataConnection.getInternalConnection();
          if (connection != null) {
            dbMetadata=connection.getMetaData();
          }
        }
 catch (        SQLException e) {
          logger.error("Error getting database metadata",e);
        }
        String productName=dbMetadata.getDatabaseProductName();
        if (productName.equalsIgnoreCase("oracle")) {
          inParameterValue="1";
          inputParameters.add(dataConnection.createDataField("",Types.VARCHAR,inParameterValue));
        }
 else         if (productName.contains("PostgreSQL")) {
          inputParameters.add(dataConnection.createDataField("",Types.BOOLEAN,true));
        }
 else {
          inParameterValue="1";
          inputParameters.add(dataConnection.createDataField("",Types.VARCHAR,inParameterValue));
        }
      }
 catch (      SQLException e) {
        logger.error("Error in recovering product name");
        throw new RuntimeException("Error in recovering both DB dialect product name database");
      }
    }
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
    if (!isFilterParameter) {
      if (dialect.contains("PostgreSQL")) {
        if (inParameterValue instanceof String)         inputParameters.add(dataConnection.createDataField("",Types.VARCHAR,inParameterValue));
 else         if (inParameterValue instanceof Integer)         inputParameters.add(dataConnection.createDataField("",Types.INTEGER,inParameterValue));
 else         if (inParameterValue instanceof Double)         inputParameters.add(dataConnection.createDataField("",Types.DOUBLE,inParameterValue));
 else         if (inParameterValue instanceof Float)         inputParameters.add(dataConnection.createDataField("",Types.FLOAT,inParameterValue));
 else         if (inParameterValue instanceof Boolean)         inputParameters.add(dataConnection.createDataField("",Types.BOOLEAN,inParameterValue));
      }
 else       inputParameters.add(dataConnection.createDataField("",Types.VARCHAR,inParameterValue));
      parameterUsed=true;
    }
 else {
      if (!inParameterValue.equals("")) {
        String sqlToAdd=(String)parameter.getAttribute("SQL");
        parameterUsed=handleParameter_extraction_3(inputParameters,dataConnection,statement,condizioneSql,inParameterValue,sqlToAdd);
      }
    }
  }
  logger.debug("OUT");
  return parameterUsed;
}

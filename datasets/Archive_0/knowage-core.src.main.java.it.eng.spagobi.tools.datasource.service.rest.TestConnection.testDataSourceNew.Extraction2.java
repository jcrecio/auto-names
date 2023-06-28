@POST @Path("/2.0/test") @Produces(MediaType.APPLICATION_JSON) @UserConstraint(functionalities={SpagoBIConstants.DATASOURCE_MANAGEMENT}) public String testDataSourceNew(@javax.ws.rs.core.Context HttpServletRequest req) throws Exception {
  logger.debug("IN");
  JSONObject requestBodyJSON=RestUtilities.readBodyAsJSONObject(req);
  String label=requestBodyJSON.optString("label");
  String url=requestBodyJSON.optString("urlConnection");
  String user=requestBodyJSON.optString("user");
  String pwd=requestBodyJSON.optString("pwd");
  String driver=requestBodyJSON.optString("driver");
  String schemaAttr=requestBodyJSON.optString("schemaAttribute");
  String jndi=requestBodyJSON.optString("jndi");
  String type=requestBodyJSON.getString("type");
  IEngUserProfile profile=(IEngUserProfile)req.getSession().getAttribute(IEngUserProfile.ENG_USER_PROFILE);
  String schema=(String)profile.getUserAttribute(schemaAttr);
  logger.debug("schema:" + schema);
  Connection connection=null;
  try {
    if (type.equals("JNDI")) {
      String jndiName=schema == null ? jndi : jndi + schema;
      logger.debug("Lookup JNDI name:" + jndiName);
      Context ctx=new InitialContext();
      DataSource ds=(DataSource)ctx.lookup(jndiName);
      connection=ds.getConnection();
    }
 else {
      if (driver.toLowerCase().contains("mongo")) {
        logger.debug("Checking the connection for MONGODB");
        MongoClient mongoClient=null;
        return testDataSourceNew_extraction_1(url,mongoClient);
      }
 else {
        if (!StringUtils.isEmpty(label) && StringUtils.isEmpty(pwd)) {
          pwd=testDataSourceNew_extraction_2(label,pwd);
        }
        Class.forName(driver);
        connection=DriverManager.getConnection(url,user,pwd);
      }
    }
    if (connection != null) {
      logger.debug("Connection OK");
      return new JSONObject().toString();
    }
 else {
      JSONObject toReturn=new JSONObject();
      toReturn.put("error","Connection KO");
      return toReturn.toString();
    }
  }
 catch (  Exception ex) {
    logger.error("Error testing datasources",ex);
    JSONObject toReturn=new JSONObject();
    toReturn.put("error",ex.getMessage());
    return toReturn.toString();
  }
}

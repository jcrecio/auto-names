@POST @Path("/test") @Consumes(MediaType.APPLICATION_JSON) @UserConstraint(functionalities={SpagoBIConstants.DATASOURCE_MANAGEMENT}) public String testDataSource(IDataSource dataSource) throws Exception {
  logger.debug("IN");
  String url=dataSource.getUrlConnection();
  String user=dataSource.getUser();
  String pwd=dataSource.getPwd();
  String driver=dataSource.getDriver();
  String schemaAttr=dataSource.getSchemaAttribute();
  String jndi=dataSource.getJndi();
  IEngUserProfile profile=getUserProfile();
  String schema=(String)profile.getUserAttribute(schemaAttr);
  logger.debug("schema:" + schema);
  if (jndi != null && jndi.length() > 0) {
    testDataSource_extraction_1(jndi,schema);
  }
 else {
    if (driver.toLowerCase().contains("mongo")) {
      logger.debug("Checking the connection for MONGODB");
      MongoClient mongoClient=null;
      try {
        int databaseNameStart=url.lastIndexOf("/");
        testDataSource_extraction_2(databaseNameStart);
        String databaseUrl=url.substring(0,databaseNameStart);
        String databaseName=url.substring(databaseNameStart + 1);
        mongoClient=new MongoClient(databaseUrl);
        DB database=mongoClient.getDB(databaseName);
        database.getCollectionNames();
        logger.debug("Connection OK");
        return new JSONObject().toString();
      }
 catch (      Exception e) {
        logger.error("Error connecting to the mongoDB",e);
      }
 finally {
        if (mongoClient != null) {
          mongoClient.close();
        }
      }
    }
 else {
      try {
        Class.forName(driver);
      }
 catch (      ClassNotFoundException e) {
        logger.error("Driver not found",e);
        throw new SpagoBIRestServiceException("Driver not found: " + driver,buildLocaleFromSession(),e);
      }
      try (Connection connection=DriverManager.getConnection(url,user,pwd)){
        logger.debug("Connection performed successfully");
      }
     }
  }
  return new JSONObject().toString();
}

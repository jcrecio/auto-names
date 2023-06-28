@POST @Path("/modifyHierarchyBkps") @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8") @UserConstraint(functionalities={SpagoBIConstants.HIERARCHIES_MANAGEMENT}) public String modifyHierarchyBkps(@Context HttpServletRequest req) throws SQLException {
  Connection databaseConnection=null;
  try {
    JSONObject requestVal=RestUtilities.readBodyAsJSONObject(req);
    String dimension=requestVal.getString("dimension");
    String hierarchyNameNew=requestVal.getString("HIER_NM");
    String hierarchyNameOrig=requestVal.getString("HIER_NM_ORIG");
    Hierarchies hierarchies=HierarchiesSingleton.getInstance();
    String hierarchyTable=hierarchies.getHierarchyTableName(dimension);
    Hierarchy hierarchyFields=hierarchies.getHierarchy(dimension);
    List<Field> generalMetadataFields=new ArrayList<Field>(hierarchyFields.getMetadataGeneralFields());
    String dataSourceName=hierarchies.getDataSourceOfDimension(dimension);
    IDataSourceDAO dataSourceDAO=DAOFactory.getDataSourceDAO();
    IDataSource dataSource=dataSourceDAO.loadDataSourceByLabel(dataSourceName);
    if (dataSource == null) {
      throw new SpagoBIServiceException("An unexpected error occured while saving custom hierarchy","No datasource found for saving hierarchy");
    }
    LinkedHashMap<String,String> lstFields=new LinkedHashMap<String,String>();
    String columns=modifyHierarchyBkps_extraction_1(requestVal,generalMetadataFields,dataSource,lstFields);
    String hierNameColumn=AbstractJDBCDataset.encapsulateColumnName(HierarchyConstants.HIER_NM,dataSource);
    databaseConnection=dataSource.getConnection();
    Statement stmt=databaseConnection.createStatement();
    boolean doUpdateRelationsMT=false;
    if (!hierarchyNameNew.equalsIgnoreCase(hierarchyNameOrig)) {
      String selectQuery="SELECT count(*) as num FROM " + hierarchyTable + " WHERE  HIER_NM = ? ";
      PreparedStatement selectPs=databaseConnection.prepareStatement(selectQuery);
      selectPs.setString(1,hierarchyNameNew);
      ResultSet rs=selectPs.executeQuery();
      if (rs.next()) {
        String count=rs.getString("num");
        if (Integer.valueOf(count) > 0) {
          logger.error("A hierarchy with name " + hierarchyNameNew + "  already exists. Change name.");
          throw new SpagoBIServiceException("","A hierarchy with name " + hierarchyNameNew + "  already exists. Change name.");
        }
      }
      doUpdateRelationsMT=true;
    }
    String updateQuery="UPDATE " + hierarchyTable + " SET "+ columns+ " WHERE "+ hierNameColumn+ "= ?";
    logger.debug("The update query is [" + updateQuery + "]");
    PreparedStatement updatePs=databaseConnection.prepareStatement(updateQuery);
    databaseConnection.setAutoCommit(false);
    logger.debug("Auto-commit false. Begin transaction!");
    int pos=1;
    for (    String key : lstFields.keySet()) {
      String value=lstFields.get(key);
      updatePs.setObject(pos,value);
      pos++;
    }
    updatePs.setObject(pos,hierarchyNameOrig);
    updatePs.executeUpdate();
    logger.debug("Update query executed!");
    modifyHierarchyBkps_extraction_2(databaseConnection,hierarchyNameNew,hierarchyNameOrig,dataSource,doUpdateRelationsMT,updateQuery);
  }
 catch (  Throwable t) {
    if (!databaseConnection.getAutoCommit() && databaseConnection != null && !databaseConnection.isClosed()) {
      databaseConnection.rollback();
    }
    logger.error("An unexpected error occured while modifing custom hierarchy");
    throw new SpagoBIServiceException("An unexpected error occured while modifing custom hierarchy",t);
  }
 finally {
    try {
      if (databaseConnection != null && !databaseConnection.isClosed()) {
        databaseConnection.close();
      }
    }
 catch (    SQLException sqle) {
      throw new SpagoBIServiceException("An unexpected error occured while saving custom hierarchy structure",sqle);
    }
  }
  return "{\"response\":\"ok\"}";
}

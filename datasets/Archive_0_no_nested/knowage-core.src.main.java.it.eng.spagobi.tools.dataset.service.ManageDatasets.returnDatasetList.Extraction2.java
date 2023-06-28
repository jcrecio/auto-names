private void returnDatasetList(IDataSetDAO dsDao,Locale locale){
  logger.debug("IN");
  try {
    Integer totalItemsNum=dsDao.countDatasets();
    List<IDataSet> items=getListOfGenericDatasets(dsDao);
    logger.debug("Loaded items list");
    JSONArray itemsJSON=(JSONArray)SerializerFactory.getSerializer("application/json").serialize(items,locale);
    ISchedulerDAO schedulerDAO;
    try {
      schedulerDAO=DAOFactory.getSchedulerDAO();
    }
 catch (    Throwable t) {
      throw new SpagoBIRuntimeException("Impossible to load scheduler DAO",t);
    }
    try {
      SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
      for (int i=0; i < items.size(); i++) {
        if (items.get(i).isPersisted()) {
          List<Trigger> triggers=schedulerDAO.loadTriggers(JOB_GROUP,items.get(i).getLabel());
          returnDatasetList_extraction_1(itemsJSON,sdf,i,triggers);
        }
        ArrayList<BIObject> objectsUsing=DAOFactory.getBIObjDataSetDAO().getBIObjectsUsingDataset(items.get(i).getId());
        String documentsNames="";
        if (objectsUsing != null && objectsUsing.size() > 1) {
          for (int o=0; o < objectsUsing.size(); o++) {
            BIObject obj=objectsUsing.get(o);
            documentsNames=returnDatasetList_extraction_2(objectsUsing,documentsNames,o,obj);
          }
          itemsJSON.getJSONObject(i).put("hasDocumentsAssociated",documentsNames);
        }
        List<FederationDefinition> federationsAssociated=DAOFactory.getFedetatedDatasetDAO().loadFederationsUsingDataset(items.get(i).getId());
        if (federationsAssociated != null && federationsAssociated.size() > 0)         itemsJSON.getJSONObject(i).put("hasFederationsAssociated","true");
      }
      JSONObject responseJSON=createJSONResponse(itemsJSON,totalItemsNum);
      writeBackToClient(new JSONSuccess(responseJSON));
    }
 catch (    Throwable t) {
      throw new SpagoBIRuntimeException("An unexpected error occured while loading trigger list for datasets",t);
    }
 finally {
      logger.debug("OUT");
    }
  }
 catch (  Throwable e) {
    logger.error("Exception occurred while retrieving items",e);
    throw new SpagoBIServiceException(SERVICE_NAME,"sbi.general.retrieveItemsError",e);
  }
}

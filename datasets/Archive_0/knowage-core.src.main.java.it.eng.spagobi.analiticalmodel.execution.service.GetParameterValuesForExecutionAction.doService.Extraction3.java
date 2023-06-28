@Override public void doService(){
  String biparameterId;
  JSONObject selectedParameterValuesJSON;
  JSONObject filtersJSON=null;
  Map selectedParameterValues;
  String mode;
  JSONObject valuesJSON;
  String contest;
  BIObjectParameter biObjectParameter;
  ExecutionInstance executionInstance;
  String valueColumn;
  String descriptionColumn;
  List rows;
  List<ObjParuse> biParameterExecDependencies;
  ILovDetail lovProvDet;
  CacheInterface cache;
  List objParameterIds;
  int treeLovNodeLevel=0;
  String treeLovNodeValue=null;
  logger.debug("IN");
  try {
    biparameterId=getAttributeAsString(PARAMETER_ID);
    selectedParameterValuesJSON=getAttributeAsJSONObject(SELECTED_PARAMETER_VALUES);
    if (this.requestContainsAttribute(FILTERS)) {
      filtersJSON=getAttributeAsJSONObject(FILTERS);
    }
    mode=getAttributeAsString(MODE);
    try {
      treeLovNodeValue=getAttributeAsString(NODE);
      if (treeLovNodeValue.contains("lovroot")) {
        treeLovNodeValue="lovroot";
        treeLovNodeLevel=0;
      }
 else {
        String[] splittedNode=treeLovNodeValue.split(NODE_ID_SEPARATOR);
        treeLovNodeValue=splittedNode[0];
        treeLovNodeLevel=new Integer(splittedNode[1]);
      }
    }
 catch (    NullPointerException e) {
      logger.debug("there is no tree attribute for the Parameter [" + PARAMETER_ID + "]");
    }
    objParameterIds=getAttributeAsList(OBJ_PARAMETER_IDS);
    contest=getAttributeAsString(CONTEST);
    logger.debug("Parameter [" + PARAMETER_ID + "] is equals to ["+ biparameterId+ "]");
    logger.debug("Parameter [" + MODE + "] is equals to ["+ mode+ "]");
    logger.debug("Parameter [" + CONTEST + "] is equals to ["+ contest+ "]");
    if (mode == null) {
      mode=MODE_SIMPLE;
    }
    Assert.assertNotNull(getContext(),"Parameter [" + PARAMETER_ID + "] cannot be null");
    executionInstance=doService_extraction_1(selectedParameterValuesJSON,objParameterIds);
    BIObject obj=executionInstance.getBIObject();
    selectedParameterValues=null;
    if (selectedParameterValuesJSON != null) {
      selectedParameterValues=doService_extraction_2(selectedParameterValuesJSON,selectedParameterValues);
    }
    biObjectParameter=null;
    List parameters=obj.getDrivers();
    for (int i=0; i < parameters.size(); i++) {
      BIObjectParameter p=(BIObjectParameter)parameters.get(i);
      if (biparameterId.equalsIgnoreCase(p.getParameterUrlName())) {
        biObjectParameter=p;
        break;
      }
    }
    Assert.assertNotNull(biObjectParameter,"Impossible to find parameter [" + biparameterId + "]");
    try {
      Parameter parameter=biObjectParameter.getParameter();
      if (DateRangeDAOUtilities.isDateRange(parameter)) {
        manageDataRange(biObjectParameter,executionInstance);
        return;
      }
    }
 catch (    Exception e) {
      throw new SpagoBIServiceException(SERVICE_NAME,"Error on loading date range combobox values",e);
    }
    lovProvDet=executionInstance.getLovDetail(biObjectParameter);
    String lovResult=null;
    try {
      IEngUserProfile profile=getUserProfile();
      LovResultCacheManager executionCacheManager=new LovResultCacheManager();
      lovResult=executionCacheManager.getLovResult(profile,lovProvDet,executionInstance.getDependencies(biObjectParameter),executionInstance,true);
      LovResultHandler lovResultHandler=new LovResultHandler(lovResult);
      rows=lovResultHandler.getRows();
    }
 catch (    Exception e) {
      throw new SpagoBIServiceException(SERVICE_NAME,"Impossible to get parameter's values",e);
    }
    Assert.assertNotNull(lovResult,"Impossible to get parameter's values");
    try {
      if (filtersJSON != null) {
        String valuefilter=(String)filtersJSON.get(SpagoBIConstants.VALUE_FILTER);
        String columnfilter=(String)filtersJSON.get(SpagoBIConstants.COLUMN_FILTER);
        String typeFilter=(String)filtersJSON.get(SpagoBIConstants.TYPE_FILTER);
        String typeValueFilter=(String)filtersJSON.get(SpagoBIConstants.TYPE_VALUE_FILTER);
        rows=DelegatedBasicListService.filterList(rows,valuefilter,typeValueFilter,columnfilter,typeFilter);
      }
    }
 catch (    JSONException e) {
      throw new SpagoBIServiceException(SERVICE_NAME,"Impossible to read filter's configuration",e);
    }
    doService_extraction_3(selectedParameterValues,mode,contest,biObjectParameter,executionInstance,rows,lovProvDet,treeLovNodeLevel,treeLovNodeValue);
  }
  finally {
    logger.debug("OUT");
  }
}

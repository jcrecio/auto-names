@POST @Path("/getParameters") @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8") public String getParameters(@Context HttpServletRequest req){
  String result="";
  String metaModelParameterId;
  JSONObject selectedParameterValuesJSON;
  JSONObject filtersJSON=null;
  Map selectedParameterValues;
  String mode;
  JSONObject valuesJSON;
  BIMetaModelParameter biMetaModelParameter;
  List rows;
  List<MetaModelParuse> biMetaModelParameterExecDependencies;
  ILovDetail lovProvDet;
  List metaModelParameterIds;
  int treeLovNodeLevel=0;
  String treeLovNodeValue=null;
  BusinessModelRuntime dum=new BusinessModelRuntime(this.getUserProfile(),req.getLocale());
  String role;
  String name;
  Integer start=null;
  Integer limit=null;
  try {
    JSONObject requestVal=RestUtilities.readBodyAsJSONObject(req);
    role=(String)requestVal.opt(ROLE);
    if ((String)requestVal.opt(OBJECT_LABEL) != null) {
      name=(String)requestVal.opt(OBJECT_LABEL);
    }
 else {
      name=(String)requestVal.opt(OBJECT_NAME);
    }
    metaModelParameterId=(String)requestVal.opt(PARAMETER_ID);
    selectedParameterValuesJSON=(JSONObject)requestVal.opt(SELECTED_PARAMETER_VALUES);
    if (requestVal.opt(FILTERS) != null) {
      filtersJSON=(JSONObject)requestVal.opt(FILTERS);
    }
    mode=(requestVal.opt(MODE) == null) ? MODE_SIMPLE : (String)requestVal.opt(MODE);
    if (requestVal.opt(NODE) != null) {
      treeLovNodeValue=(String)requestVal.opt(NODE);
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
    if (requestVal.opt(START) != null) {
      start=(Integer)requestVal.opt(START);
    }
    if (requestVal.opt(LIMIT) != null) {
      limit=(Integer)requestVal.opt(LIMIT);
    }
    MetaModel obj;
    try {
      obj=DAOFactory.getMetaModelsDAO().loadMetaModelForExecutionByNameAndRole(name,role,false);
      if (selectedParameterValuesJSON != null) {
        dum.refreshParametersValues(selectedParameterValuesJSON,false,obj);
      }
      selectedParameterValues=null;
      if (selectedParameterValuesJSON != null) {
        try {
          selectedParameterValues=new HashMap();
          Iterator it=selectedParameterValuesJSON.keys();
          while (it.hasNext()) {
            String key=(String)it.next();
            Object v=selectedParameterValuesJSON.get(key);
            if (v == JSONObject.NULL) {
              selectedParameterValues.put(key,null);
            }
 else             if (v instanceof JSONArray) {
              JSONArray a=(JSONArray)v;
              String[] nv=new String[a.length()];
              for (int i=0; i < a.length(); i++) {
                if (a.get(i) != null) {
                  nv[i]=a.get(i).toString();
                }
 else {
                  nv[i]=null;
                }
              }
              selectedParameterValues.put(key,nv);
            }
 else             if (v instanceof String) {
              selectedParameterValues.put(key,v);
            }
 else             if (v instanceof Integer) {
              selectedParameterValues.put(key,"" + v);
            }
 else             if (v instanceof Double) {
              selectedParameterValues.put(key,"" + v);
            }
 else {
              Assert.assertUnreachable("Attribute [" + key + "] value ["+ v+ "] of PARAMETERS is not of type JSONArray nor String. It is of type ["+ v.getClass().getName()+ "]");
            }
          }
        }
 catch (        JSONException e) {
          throw new SpagoBIServiceException("parameter JSONObject is malformed",e);
        }
      }
      biMetaModelParameter=null;
      List parameters=obj.getDrivers();
      for (int i=0; i < parameters.size(); i++) {
        BIMetaModelParameter p=(BIMetaModelParameter)parameters.get(i);
        if (metaModelParameterId.equalsIgnoreCase(p.getParameterUrlName())) {
          biMetaModelParameter=p;
          break;
        }
      }
      Assert.assertNotNull(biMetaModelParameter,"Impossible to find parameter [" + metaModelParameterId + "]");
      lovProvDet=dum.getLovDetail(biMetaModelParameter);
      String lovResult=null;
      try {
        IEngUserProfile profile=getUserProfile();
        LovResultCacheManager executionCacheManager=new LovResultCacheManager();
        lovResult=executionCacheManager.getLovResultDum(profile,lovProvDet,dum.getDependencies(biMetaModelParameter,role),obj,true,req.getLocale());
        LovResultHandler lovResultHandler=new LovResultHandler(lovResult);
        rows=lovResultHandler.getRows();
      }
 catch (      MissingLOVDependencyException mldaE) {
        String localizedMessage=getLocalizedMessage("sbi.api.documentExecParameters.dependencyNotFill",req);
        String msg=localizedMessage + ": " + mldaE.getDependsFrom();
        throw new SpagoBIServiceException(SERVICE_NAME,msg);
      }
catch (      Exception e) {
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
 catch (      JSONException e) {
        throw new SpagoBIServiceException(SERVICE_NAME,"Impossible to read filter's configuration",e);
      }
      biMetaModelParameterExecDependencies=dum.getDependencies(biMetaModelParameter,role);
      if (lovProvDet instanceof DependenciesPostProcessingLov && selectedParameterValues != null && biMetaModelParameterExecDependencies != null && biMetaModelParameterExecDependencies.size() > 0) {
        rows=((DependenciesPostProcessingLov)lovProvDet).processDependencies(rows,selectedParameterValues,biMetaModelParameterExecDependencies);
      }
      if (lovProvDet.getLovType() != null && lovProvDet.getLovType().contains("tree")) {
        JSONArray valuesJSONArray=getChildrenForTreeLov(lovProvDet,rows,mode,treeLovNodeLevel,treeLovNodeValue);
        result=buildJsonResult("OK","",null,valuesJSONArray,metaModelParameterId).toString();
      }
 else {
        valuesJSON=buildJSONForLOV(lovProvDet,rows,mode,start,limit);
        result=buildJsonResult("OK","",valuesJSON,null,metaModelParameterId).toString();
      }
    }
 catch (    Exception e1) {
      throw new SpagoBIServiceException(SERVICE_NAME,"Impossible to get business model Execution Parameter EMFUserError",e1);
    }
  }
 catch (  IOException e2) {
    throw new SpagoBIServiceException(SERVICE_NAME,"Impossible to get business model Execution Parameter IOException",e2);
  }
catch (  JSONException e2) {
    throw new SpagoBIServiceException(SERVICE_NAME,"Impossible to get business model Execution Parameter JSONException",e2);
  }
  return result;
}

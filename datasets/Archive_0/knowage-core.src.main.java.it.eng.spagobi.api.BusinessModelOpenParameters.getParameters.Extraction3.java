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
    result=getParameters_extraction_1(req,result,metaModelParameterId,selectedParameterValuesJSON,filtersJSON,mode,rows,treeLovNodeLevel,treeLovNodeValue,dum,role,name,start,limit);
  }
 catch (  IOException e2) {
    throw new SpagoBIServiceException(SERVICE_NAME,"Impossible to get business model Execution Parameter IOException",e2);
  }
catch (  JSONException e2) {
    throw new SpagoBIServiceException(SERVICE_NAME,"Impossible to get business model Execution Parameter JSONException",e2);
  }
  return result;
}

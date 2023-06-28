@Override public ListIFace getList(SourceBean request,SourceBean response) throws Exception {
  PaginatorIFace paginator=new GenericPaginator();
  ListIFace list=new GenericList();
  RequestContainer requestContainer=getRequestContainer();
  SessionContainer session=requestContainer.getSessionContainer();
  ModalitiesValue modVal=(ModalitiesValue)session.getAttribute(SpagoBIConstants.MODALITY_VALUE_OBJECT);
  String looProvider=modVal.getLovProvider();
  String typeLov=LovDetailFactory.getLovTypeCode(looProvider);
  IEngUserProfile profile=null;
  profile=(IEngUserProfile)session.getAttribute(SpagoBIConstants.USER_PROFILE_FOR_TEST);
  if (profile == null) {
    SessionContainer permSess=session.getPermanentContainer();
    profile=(IEngUserProfile)permSess.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
  }
  SourceBean rowsSourceBean=null;
  List colNames=new ArrayList();
  if (typeLov.equalsIgnoreCase("QUERY")) {
    QueryDetail qd=QueryDetail.fromXML(looProvider);
    String datasource=qd.getDataSource();
    String statement=qd.getQueryDefinition();
    try {
      statement=StringUtilities.substituteProfileAttributesInString(statement,profile);
      rowsSourceBean=(SourceBean)executeSelect(getRequestContainer(),getResponseContainer(),datasource,statement,colNames);
    }
 catch (    Exception e) {
      String stacktrace=e.toString();
      response.setAttribute("stacktrace",stacktrace);
      int startIndex=stacktrace.indexOf("java.sql.");
      int endIndex=stacktrace.indexOf("\n\tat ",startIndex);
      if (endIndex == -1)       endIndex=stacktrace.indexOf(" at ",startIndex);
      if (startIndex != -1 && endIndex != -1)       response.setAttribute("errorMessage",stacktrace.substring(startIndex,endIndex));
      response.setAttribute("testExecuted","false");
    }
  }
 else   if (typeLov.equalsIgnoreCase("FIXED_LIST")) {
    FixedListDetail fixlistDet=FixedListDetail.fromXML(looProvider);
    try {
      String result=fixlistDet.getLovResult(profile,null,null,null);
      rowsSourceBean=SourceBean.fromXMLString(result);
      colNames=findFirstRowAttributes(rowsSourceBean);
      getList_extraction_2(rowsSourceBean);
    }
 catch (    Exception e) {
      SpagoBITracer.major(SpagoBIConstants.NAME_MODULE,this.getClass().getName(),"getList","Error while converting fix lov into spago list",e);
      String stacktrace=e.toString();
      response.setAttribute("stacktrace",stacktrace);
      response.setAttribute("errorMessage","Error while executing fix list lov");
      response.setAttribute("testExecuted","false");
      return list;
    }
  }
 else   if (typeLov.equalsIgnoreCase("SCRIPT")) {
    ScriptDetail scriptDetail=ScriptDetail.fromXML(looProvider);
    try {
      String result=scriptDetail.getLovResult(profile,null,null,null);
      rowsSourceBean=SourceBean.fromXMLString(result);
      colNames=findFirstRowAttributes(rowsSourceBean);
    }
 catch (    Exception e) {
      SpagoBITracer.major(SpagoBIConstants.NAME_MODULE,this.getClass().getName(),"getList","Error while executing the script lov",e);
      String stacktrace=e.toString();
      response.setAttribute("stacktrace",stacktrace);
      response.setAttribute("errorMessage","Error while executing script");
      response.setAttribute("testExecuted","false");
      return list;
    }
  }
 else   if (typeLov.equalsIgnoreCase("JAVA_CLASS")) {
    JavaClassDetail javaClassDetail=JavaClassDetail.fromXML(looProvider);
    try {
      String javaClassName=javaClassDetail.getJavaClassName();
      IJavaClassLov javaClassLov=(IJavaClassLov)Class.forName(javaClassName).newInstance();
      String result=javaClassLov.getValues(profile);
      rowsSourceBean=SourceBean.fromXMLString(result);
      colNames=findFirstRowAttributes(rowsSourceBean);
    }
 catch (    Exception e) {
      SpagoBITracer.major(SpagoBIConstants.NAME_MODULE,this.getClass().getName(),"getList","Error while executing the java class lov",e);
      String stacktrace=e.toString();
      response.setAttribute("stacktrace",stacktrace);
      response.setAttribute("errorMessage","Error while executing java class");
      response.setAttribute("testExecuted","false");
      return list;
    }
  }
 else   if (typeLov.equalsIgnoreCase("DATASET")) {
    DatasetDetail datasetClassDetail=DatasetDetail.fromXML(looProvider);
    try {
      String result=datasetClassDetail.getLovResult(profile,null,null,null);
      rowsSourceBean=SourceBean.fromXMLString(result);
      colNames=findFirstRowAttributes(rowsSourceBean);
    }
 catch (    Exception e) {
      SpagoBITracer.major(SpagoBIConstants.NAME_MODULE,this.getClass().getName(),"getList","Error while executing the dataset lov",e);
      String stacktrace=e.toString();
      response.setAttribute("stacktrace",stacktrace);
      response.setAttribute("errorMessage","Error while executing dataset");
      response.setAttribute("testExecuted","false");
      return list;
    }
  }
  String moduleConfigStr=getList_extraction_3(paginator,list,rowsSourceBean,colNames);
  moduleConfigStr+="</CONFIG>";
  SourceBean moduleConfig=SourceBean.fromXMLString(moduleConfigStr);
  response.setAttribute(moduleConfig);
  String valuefilter=(String)request.getAttribute(SpagoBIConstants.VALUE_FILTER);
  if (valuefilter != null) {
    String columnfilter=(String)request.getAttribute(SpagoBIConstants.COLUMN_FILTER);
    String typeFilter=(String)request.getAttribute(SpagoBIConstants.TYPE_FILTER);
    String typeValueFilter=(String)request.getAttribute(SpagoBIConstants.TYPE_VALUE_FILTER);
    list=DelegatedBasicListService.filterList(list,valuefilter,typeValueFilter,columnfilter,typeFilter,getResponseContainer().getErrorHandler());
  }
  response.setAttribute("testExecuted","true");
  return list;
}

private ListIFace loadSpagoList(SourceBean request,SourceBean response,Integer parId,String roleName) throws Exception {
  logger.debug("IN");
  RequestContainer requestContainer=getRequestContainer();
  SessionContainer session=requestContainer.getSessionContainer();
  String parameterFieldName=(String)request.getAttribute("parameterFieldName");
  logger.debug("parameterFieldName=" + parameterFieldName);
  PaginatorIFace paginator=new GenericPaginator();
  ListIFace list=new GenericList();
  String valColName="";
  IParameterDAO pardao=DAOFactory.getParameterDAO();
  Parameter par=pardao.loadForExecutionByParameterIDandRoleName(parId,roleName,false);
  ModalitiesValue modVal=par.getModalityValue();
  String looProvider=modVal.getLovProvider();
  String typeLov=LovDetailFactory.getLovTypeCode(looProvider);
  IEngUserProfile profile=null;
  SessionContainer permanentSession=session.getPermanentContainer();
  profile=(IEngUserProfile)permanentSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
  SourceBean rowsSourceBean=null;
  if (typeLov.equalsIgnoreCase("QUERY")) {
    QueryDetail qd=QueryDetail.fromXML(looProvider);
    valColName=qd.getValueColumnName();
    String datasource=qd.getDataSource();
    String statement=qd.getQueryDefinition();
    try {
      statement=StringUtilities.substituteProfileAttributesInString(statement,profile);
      rowsSourceBean=(SourceBean)executeSelect(getRequestContainer(),getResponseContainer(),datasource,statement);
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
    valColName=fixlistDet.getValueColumnName();
    try {
      String result=fixlistDet.getLovResult(profile,null,null,null);
      rowsSourceBean=SourceBean.fromXMLString(result);
      if (!rowsSourceBean.getName().equalsIgnoreCase("ROWS")) {
        throw new Exception("The fix list is empty");
      }
 else       if (rowsSourceBean.getAttributeAsList(DataRow.ROW_TAG).size() == 0) {
        throw new Exception("The fix list is empty");
      }
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
    valColName=scriptDetail.getValueColumnName();
    try {
      String result=scriptDetail.getLovResult(profile,null,null,null);
      rowsSourceBean=SourceBean.fromXMLString(result);
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
    valColName=javaClassDetail.getValueColumnName();
    try {
      String javaClassName=javaClassDetail.getJavaClassName();
      IJavaClassLov javaClassLov=(IJavaClassLov)Class.forName(javaClassName).newInstance();
      String result=javaClassLov.getValues(profile);
      rowsSourceBean=SourceBean.fromXMLString(result);
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
  int count=0;
  if (rowsSourceBean != null) {
    List rows=rowsSourceBean.getAttributeAsList(DataRow.ROW_TAG);
    for (int i=0; i < rows.size(); i++) {
      paginator.addRow(rows.get(i));
      count++;
    }
  }
  paginator.setPageSize(count);
  list.setPaginator(paginator);
  rowsSourceBean=list.getPaginator().getAll();
  List colNames=new ArrayList();
  List rows=null;
  if (rowsSourceBean != null) {
    rows=rowsSourceBean.getAttributeAsList(DataRow.ROW_TAG);
    if ((rows != null) && (rows.size() != 0)) {
      SourceBean row=(SourceBean)rows.get(0);
      List rowAttrs=row.getContainedAttributes();
      Iterator rowAttrsIter=rowAttrs.iterator();
      while (rowAttrsIter.hasNext()) {
        SourceBeanAttribute rowAttr=(SourceBeanAttribute)rowAttrsIter.next();
        colNames.add(rowAttr.getKey());
      }
    }
  }
  String moduleConfigStr="";
  moduleConfigStr+="<CONFIG>";
  moduleConfigStr+="	<QUERIES/>";
  moduleConfigStr+="	<COLUMNS>";
  if (colNames.size() == 0) {
    moduleConfigStr+="	<COLUMN name=\"No Result Found\" />";
  }
 else {
    Iterator iterColNames=colNames.iterator();
    while (iterColNames.hasNext()) {
      String colName=(String)iterColNames.next();
      moduleConfigStr+="	<COLUMN name=\"" + colName + "\" />";
    }
  }
  moduleConfigStr+="	</COLUMNS>";
  moduleConfigStr+="	<CAPTIONS/>";
  moduleConfigStr+="	<BUTTONS/>";
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
  response.setAttribute(SpagoBIConstants.PARAMETER_FIELD_NAME,parameterFieldName);
  response.setAttribute(SpagoBIConstants.VALUE_COLUMN_NAME,valColName);
  logger.debug("OUT");
  return list;
}

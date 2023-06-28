@Override public void doService(){
  logger.info("IN");
  SimpleDateFormat timeStampFormat=new SimpleDateFormat(TIMESTAMP_FORMAT,getLocale());
  SimpleDateFormat dateFormat=new SimpleDateFormat(DATE_FORMAT,getLocale());
  Integer id=this.getAttributeAsInteger(VERSION_ID);
  JSONObject driversJson=this.getAttributeAsJSONObject("DRIVERS");
  JSONObject parametersAttribute=getAttributeAsJSONObject(PARAMETERS.toUpperCase());
  JSONArray paramsJson=new JSONArray();
  Map<String,String> parameters=new HashMap<>();
  try {
    if (parametersAttribute != null)     paramsJson=parametersAttribute.getJSONArray(PARAMETERS);
    JSONObject pars=new JSONObject();
    pars.put(DataSetConstants.PARS,paramsJson);
    ManageDataSetsForREST mdsr=new ManageDataSetsForREST();
    parameters=mdsr.getDataSetParametersAsMap(pars);
  }
 catch (  Exception ex) {
    logger.debug("Cannot get Dataset parameters");
    throw new SpagoBIServiceException(this.getActionName(),"Impossible to transform parameters from json object to map",ex);
  }
  IDataSetDAO dao=DAOFactory.getDataSetDAO();
  dao.setUserProfile(this.getUserProfile());
  IDataSet dataSet=dao.loadDataSetById(id);
  UserProfile profile=(UserProfile)this.getUserProfile();
  try {
    new DatasetManagementAPI(profile).canLoadData(dataSet);
  }
 catch (  ActionNotPermittedException e) {
    logger.error("User " + profile.getUserId() + " cannot export the dataset with label "+ dataSet.getLabel());
    throw new SpagoBIServiceException(e.getI18NCode(),"User " + profile.getUserId() + " cannot export the dataset with label "+ dataSet.getLabel(),e);
  }
  Map<String,Object> drivers=null;
  try {
    drivers=JSONObjectDeserializator.getHashMapFromJSONObject(driversJson);
  }
 catch (  Exception e) {
    logger.debug("Drivers cannot be transformed from json object to map");
    throw new SpagoBIServiceException(this.getActionName(),"Impossible to transform drivers from json object to map",e);
  }
  dataSet.setDrivers(drivers);
  dataSet.setParamsMap(parameters);
  IDataStore dataStore=null;
  try {
    dataSet.loadData();
    dataStore=dataSet.getDataStore();
  }
 catch (  Exception e) {
    logger.error("Error loading dataset",e);
  }
  freezeHttpResponse();
  getHttpResponse().setHeader("Content-Disposition","attachment" + "; filename=\"" + dataSet.getName() + ".xlsx"+ "\";");
  getHttpResponse().setContentType("application/vnd.ms-excel");
  XSSFWorkbook wb=new XSSFWorkbook();
  XSSFSheet sheet=wb.createSheet("datastore");
  CreationHelper createHelper=wb.getCreationHelper();
  CellStyle borderStyleHeader=wb.createCellStyle();
  borderStyleHeader.setBorderBottom(BorderStyle.THIN);
  borderStyleHeader.setBorderLeft(BorderStyle.THIN);
  borderStyleHeader.setBorderRight(BorderStyle.THIN);
  borderStyleHeader.setBorderTop(BorderStyle.THIN);
  borderStyleHeader.setAlignment(HorizontalAlignment.CENTER);
  CellStyle borderStyleRow=wb.createCellStyle();
  borderStyleRow.setBorderBottom(BorderStyle.THIN);
  borderStyleRow.setBorderLeft(BorderStyle.THIN);
  borderStyleRow.setBorderRight(BorderStyle.THIN);
  borderStyleRow.setBorderTop(BorderStyle.THIN);
  borderStyleRow.setAlignment(HorizontalAlignment.RIGHT);
  CellStyle tsCellStyle=wb.createCellStyle();
  tsCellStyle.setDataFormat(createHelper.createDataFormat().getFormat(TIMESTAMP_FORMAT));
  tsCellStyle.setBorderBottom(BorderStyle.THIN);
  tsCellStyle.setBorderLeft(BorderStyle.THIN);
  tsCellStyle.setBorderRight(BorderStyle.THIN);
  tsCellStyle.setBorderTop(BorderStyle.THIN);
  tsCellStyle.setAlignment(HorizontalAlignment.RIGHT);
  CellStyle dateCellStyle=wb.createCellStyle();
  dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat(DATE_FORMAT));
  dateCellStyle.setBorderBottom(BorderStyle.THIN);
  dateCellStyle.setBorderLeft(BorderStyle.THIN);
  dateCellStyle.setBorderRight(BorderStyle.THIN);
  dateCellStyle.setBorderTop(BorderStyle.THIN);
  dateCellStyle.setAlignment(HorizontalAlignment.RIGHT);
  CellStyle intCellStyle=wb.createCellStyle();
  intCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("0"));
  intCellStyle.setBorderBottom(BorderStyle.THIN);
  intCellStyle.setBorderLeft(BorderStyle.THIN);
  intCellStyle.setBorderRight(BorderStyle.THIN);
  intCellStyle.setBorderTop(BorderStyle.THIN);
  intCellStyle.setAlignment(HorizontalAlignment.RIGHT);
  CellStyle decimalCellStyle=wb.createCellStyle();
  decimalCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#,##0.00"));
  decimalCellStyle.setBorderBottom(BorderStyle.THIN);
  decimalCellStyle.setBorderLeft(BorderStyle.THIN);
  decimalCellStyle.setBorderRight(BorderStyle.THIN);
  decimalCellStyle.setBorderTop(BorderStyle.THIN);
  decimalCellStyle.setAlignment(HorizontalAlignment.RIGHT);
  if (dataStore != null) {
    XSSFRow header=sheet.createRow((short)0);
    doService_extraction_1(timeStampFormat,dateFormat,dataStore,sheet,borderStyleHeader,borderStyleRow,tsCellStyle,dateCellStyle,intCellStyle,decimalCellStyle,header);
  }
 else {
    MessageBuilder msgBuild=new MessageBuilder();
    XSSFRow header=sheet.createRow((short)0);
    XSSFCell cell=header.createCell(1);
    cell.setCellValue(msgBuild.getMessage("exporter.dataset.excel",getLocale()));
    cell.setCellStyle(borderStyleHeader);
  }
  OutputStream out;
  try {
    out=getHttpResponse().getOutputStream();
    wb.write(out);
    getHttpResponse().getOutputStream().flush();
    getHttpResponse().getOutputStream().close();
  }
 catch (  IOException e) {
    logger.error("write output file stream error " + e.getMessage());
    throw new SpagoBIServiceException(this.getActionName(),"Impossible to write output file xls error",e);
  }
}

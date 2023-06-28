private FileDataSet manageFileDataSet(boolean savingDataset,JSONObject jsonDsConfig,JSONObject json) throws JSONException, IOException {
  FileDataSet dataSet=new FileDataSet();
  String dsId=json.optString(DataSetConstants.ID);
  String dsLabel=json.getString(DataSetConstants.LABEL);
  String fileType=json.getString(DataSetConstants.FILE_TYPE);
  String versionNum=json.getString(DataSetConstants.VERSION_NUM);
  String csvDelimiter=json.optString(DataSetConstants.CSV_FILE_DELIMITER_CHARACTER);
  String csvQuote=json.optString(DataSetConstants.CSV_FILE_QUOTE_CHARACTER);
  String dateFormat=json.optString(DataSetConstants.FILE_DATE_FORMAT);
  String timestampFormat=json.optString(DataSetConstants.FILE_TIMESTAMP_FORMAT);
  String csvEncoding=json.optString(DataSetConstants.CSV_FILE_ENCODING);
  String skipRows=json.optString(DataSetConstants.XSL_FILE_SKIP_ROWS);
  String limitRows=json.optString(DataSetConstants.XSL_FILE_LIMIT_ROWS);
  String xslSheetNumber=json.optString(DataSetConstants.XSL_FILE_SHEET_NUMBER);
  JSONArray dsMeta=json.optJSONArray(DataSetConstants.FILE_DS_METADATA);
  if (dsMeta != null && dsMeta.length() > 0) {
    DatasetMetadataParser dsp=new DatasetMetadataParser();
    String metadataXML=dsp.metadataToXML(getUserMetaData(dsMeta));
    dataSet.setDsMetadata(metadataXML);
  }
  String dsLab=dsLabel;
  Boolean newFileUploaded=false;
  if (json.optString("fileUploaded") != null) {
    newFileUploaded=Boolean.valueOf(json.optString("fileUploaded"));
  }
  if (versionNum != "") {
    dsLab=dsLabel + "_" + versionNum;
  }
  jsonDsConfig.put(DataSetConstants.FILE_TYPE,fileType);
  jsonDsConfig.put(DataSetConstants.CSV_FILE_DELIMITER_CHARACTER,csvDelimiter);
  jsonDsConfig.put(DataSetConstants.CSV_FILE_QUOTE_CHARACTER,csvQuote);
  jsonDsConfig.put(DataSetConstants.CSV_FILE_ENCODING,csvEncoding);
  jsonDsConfig.put(DataSetConstants.XSL_FILE_SKIP_ROWS,skipRows);
  jsonDsConfig.put(DataSetConstants.XSL_FILE_LIMIT_ROWS,limitRows);
  jsonDsConfig.put(DataSetConstants.XSL_FILE_SHEET_NUMBER,xslSheetNumber);
  jsonDsConfig.put(DataSetConstants.FILE_DATE_FORMAT,dateFormat);
  jsonDsConfig.put(DataSetConstants.FILE_TIMESTAMP_FORMAT,timestampFormat);
  dataSet.setResourcePath(DAOConfig.getResourcePath());
  String fileName=json.getString(DataSetConstants.FILE_NAME);
  File pathFile=new File(fileName);
  fileName=pathFile.getName();
  if (savingDataset) {
    logger.debug("When saving the dataset the file associated will get the dataset label name");
    if (dsLabel != null) {
      jsonDsConfig.put(DataSetConstants.FILE_NAME,dsLab + "." + fileType.toLowerCase());
    }
  }
 else {
    jsonDsConfig.put(DataSetConstants.FILE_NAME,fileName);
  }
  dataSet.setConfiguration(jsonDsConfig.toString());
  if ((dsId == null) || (dsId.isEmpty())) {
    logger.debug("By creating a new dataset, the file uploaded has to be renamed and moved");
    dataSet.setUseTempFile(true);
    if (savingDataset) {
      logger.debug("Rename and move the file");
      String resourcePath=dataSet.getResourcePath();
      if (dsLabel != null) {
        renameAndMoveDatasetFile(fileName,dsLab,resourcePath,fileType);
        dataSet.setUseTempFile(false);
      }
    }
  }
 else {
    logger.debug("Reading or modifying a existing dataset. If change the label then the name of the file should be changed");
    JSONObject configuration;
    Integer id_ds=json.getInt(DataSetConstants.ID);
    configuration=new JSONObject(DAOFactory.getDataSetDAO().loadDataSetById(id_ds).getConfiguration());
    String realName=configuration.getString("fileName");
    if (dsLabel != null && !realName.equals(dsLabel)) {
      File dest=new File(SpagoBIUtilities.getResourcePath() + File.separatorChar + "dataset"+ File.separatorChar+ "files"+ File.separatorChar+ dsLab+ "."+ configuration.getString("fileType").toLowerCase());
      File source=new File(SpagoBIUtilities.getResourcePath() + File.separatorChar + "dataset"+ File.separatorChar+ "files"+ File.separatorChar+ realName);
      if (!source.getCanonicalPath().equals(dest.getCanonicalPath()) && savingDataset && !newFileUploaded) {
        logger.debug("Source and destination are not the same. Copying from source to dest");
        FileUtils.copyFile(source,dest);
      }
    }
    if (newFileUploaded) {
      logger.debug("Modifying an existing dataset with a new file uploaded");
      dataSet.setUseTempFile(true);
      logger.debug("Saving the existing dataset with a new file associated");
      if (savingDataset) {
        logger.debug("Rename and move the file");
        String resourcePath=dataSet.getResourcePath();
        if (dsLabel != null) {
          renameAndMoveDatasetFile(fileName,dsLabel + "_" + versionNum,resourcePath,fileType);
          dataSet.setUseTempFile(false);
        }
      }
    }
 else {
      logger.debug("Using existing dataset file, file in correct place");
      dataSet.setUseTempFile(false);
    }
  }
  dataSet.setFileType(fileType);
  if (savingDataset) {
    logger.debug("The file used will have the name equals to dataset's label");
    dataSet.setFileName(dsLab + "." + fileType.toLowerCase());
  }
 else {
    dataSet.setFileName(fileName);
  }
  return dataSet;
}

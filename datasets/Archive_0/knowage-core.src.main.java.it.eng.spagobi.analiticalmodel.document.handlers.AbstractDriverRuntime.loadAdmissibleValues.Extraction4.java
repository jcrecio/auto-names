/** 
 * Load admissible values from LOV done if not lookup and not manual input done if is from cross and not manual input
 */
protected void loadAdmissibleValues(AbstractDriver driver,AbstractBIResourceRuntime dum){
  logger.debug("IN");
  try {
    Integer paruseId=analyticalDriverExecModality.getUseID();
    ParameterUse parameterUse=DAOFactory.getParameterUseDAO().loadByUseID(paruseId);
    if ("lov".equalsIgnoreCase(parameterUse.getValueSelection())) {
      ILovDetail lovProvDet=dum.getLovDetail(driver);
      lovColumnsNames=lovProvDet.getVisibleColumnNames();
      lovDescriptionColumnName=lovProvDet.getDescriptionColumnName();
      lovValueColumnName=lovProvDet.getValueColumnName();
    }
    boolean retrieveAdmissibleValue=false;
    retrieveAdmissibleValue=areAdmissibleValuesToBePreloaded(driver);
    if (retrieveAdmissibleValue) {
      List rows;
      try {
        rows=executeLOV(dum);
      }
 catch (      MissingLOVDependencyException e) {
        logger.debug("Could not get LOV values because of a missing LOV dependency",e);
        setValuesCount(-1);
        return;
      }
      rows=applyPostProcessingDependencies(rows,dum);
      setValuesCount(rows == null ? 0 : rows.size());
      logger.debug("Loaded " + valuesCount + "values");
      admissibleValues=new ArrayList<HashMap<String,Object>>();
      if (getValuesCount() == 1 && this.isMandatory()) {
        SourceBean lovSB=(SourceBean)rows.get(0);
        value=getValueFromLov(lovSB);
        description=getDescriptionFromLov(lovSB);
        driver.setParameterValues(new ArrayList<>(Arrays.asList(value)));
        driver.setParameterValuesDescription(new ArrayList<>(Arrays.asList(description)));
      }
      JSONObject valuesJSON=DocumentExecutionUtils.buildJSONForLOV(dum.getLovDetail(driver),rows,DocumentExecutionUtils.MODE_SIMPLE);
      JSONArray valuesJSONArray=valuesJSON.getJSONArray("root");
      for (int i=0; i < valuesJSONArray.length(); i++) {
        loadAdmissibleValues_extraction_1(driver,valuesJSONArray,i);
      }
      if (isFromCross) {
        loadAdmissibleValues_extraction_3(driver);
      }
    }
 else {
      setValuesCount(-1);
    }
  }
 catch (  Exception e) {
    logger.error("Errpr in retrieving admissible values");
    throw new SpagoBIRuntimeException(e);
  }
  logger.debug("OUT");
}

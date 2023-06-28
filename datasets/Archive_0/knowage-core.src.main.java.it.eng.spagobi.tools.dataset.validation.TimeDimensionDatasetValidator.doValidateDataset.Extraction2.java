@Override public ValidationErrors doValidateDataset(IDataStore dataStore,Map<String,HierarchyLevel> hierarchiesColumnsToCheck){
  ValidationErrors validationErrors=new ValidationErrors();
  MeasureCatalogue measureCatalogue=MeasureCatalogueSingleton.getMeasureCatologue();
  if (measureCatalogue.isValid()) {
    MetaModelWrapper metamodelWrapper=measureCatalogue.getMetamodelWrapper();
    for (    Map.Entry<String,HierarchyLevel> entry : hierarchiesColumnsToCheck.entrySet()) {
      MessageBuilder msgBuild=new MessageBuilder();
      logger.debug("Column Name= " + entry.getKey() + " / HierarchyLevel"+ entry.getValue());
      String columnName=entry.getKey();
      HierarchyLevel hierarchyLevel=entry.getValue();
      if (hierarchyLevel.isValidEntry()) {
        String hierarchyName=hierarchyLevel.getHierarchy_name();
        String hierarchyLevelName=hierarchyLevel.getLevel_name();
        if (hierarchyName.equalsIgnoreCase(TIME_HIERARCHY_NAME)) {
          doValidateDataset_extraction_1(dataStore,validationErrors,metamodelWrapper,msgBuild,columnName,hierarchyName,hierarchyLevelName);
        }
      }
    }
  }
  return validationErrors;
}

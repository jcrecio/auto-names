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
          HierarchyWrapper hierarchy=metamodelWrapper.getHierarchy(TIME_HIERARCHY_NAME);
          if (hierarchy != null) {
            if (hierarchy.getName().equalsIgnoreCase(hierarchyName)) {
              List<Level> levels=hierarchy.getLevels();
              Level level=hierarchy.getLevel(hierarchyLevelName);
              if (level != null) {
                String levelName=level.getName();
                IDataStore dataStoreLevel=hierarchy.getMembers(levelName);
                Set<String> admissibleValues=dataStoreLevel.getFieldDistinctValuesAsString(0);
                String hint=generateHintValues(admissibleValues);
                Iterator it=dataStore.iterator();
                int columnIndex=dataStore.getMetaData().getFieldIndex(columnName);
                int rowNumber=0;
                while (it.hasNext()) {
                  IRecord record=(IRecord)it.next();
                  IField field=record.getFieldAt(columnIndex);
                  Object fieldValue=field.getValue();
                  doValidateDataset_extraction_2(validationErrors,msgBuild,levelName,admissibleValues,hint,columnIndex,rowNumber,field,fieldValue);
                  rowNumber++;
                }
              }
 else {
                logger.warn("Attention: the hierarchy " + TIME_HIERARCHY_NAME + " doesn't contain a level "+ hierarchyLevelName);
              }
            }
          }
 else {
            logger.warn("Attention: the validation model doesn't contain a hierarchy with name " + TIME_HIERARCHY_NAME + ". Validation will not be performed.");
          }
        }
      }
    }
  }
  return validationErrors;
}

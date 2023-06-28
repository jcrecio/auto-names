private void synchronizeUserFunctionalities(Session aSession) throws Exception {
  logger.debug("IN");
  SourceBean userFunctionalitiesSB=getConfiguration();
  configurationFileName="it/eng/spagobi/commons/initializers/metadata/config/roleTypeUserFunctionalities.xml";
  SourceBean roleTypeUserFunctionalitiesSB=getConfiguration();
  if (userFunctionalitiesSB == null) {
    throw new Exception("User functionalities configuration file not found!!!");
  }
  if (roleTypeUserFunctionalitiesSB == null) {
    throw new Exception("Role type user functionalities configuration file not found!!!");
  }
  List userFunctionalitiesList=userFunctionalitiesSB.getAttributeAsList("USER_FUNCTIONALITY");
  if (userFunctionalitiesList == null || userFunctionalitiesList.isEmpty()) {
    throw new Exception("No predefined user functionalities found!!!");
  }
  List<SbiUserFunctionality> sbiUserFunctionalityList=getsbiUserFunctionalities(aSession);
  for (  SbiUserFunctionality sbiUserFunc : sbiUserFunctionalityList) {
    boolean isInConfigFile=false;
    String nameInDB=sbiUserFunc.getName();
    String productTypeInDB=sbiUserFunc.getProductType().getLabel();
    Iterator it=userFunctionalitiesList.iterator();
    while (it.hasNext()) {
      SourceBean aUSerFunctionalitySB=(SourceBean)it.next();
      String nameInFile=(String)aUSerFunctionalitySB.getAttribute("name");
      String productTypeInFile=(String)aUSerFunctionalitySB.getAttribute("productType");
      if (nameInFile.equals(nameInDB) && productTypeInFile.equals(productTypeInDB)) {
        isInConfigFile=true;
        break;
      }
    }
    if (!isInConfigFile) {
      deleteUserFunctionality(aSession,sbiUserFunc);
    }
  }
  Iterator it=userFunctionalitiesList.iterator();
  while (it.hasNext()) {
    SourceBean aUSerFunctionalitySB=(SourceBean)it.next();
    String userFunctionalityName=(String)aUSerFunctionalitySB.getAttribute("name");
    String userFunctionalityProductType=(String)aUSerFunctionalitySB.getAttribute("productType");
    SbiProductType productType=findProductType(aSession,userFunctionalityProductType);
    if (productType != null) {
      SbiUserFunctionality aUserFunctionality=synchronizeUserFunctionalities_extraction_2(aSession,roleTypeUserFunctionalitiesSB,aUSerFunctionalitySB,userFunctionalityName,productType);
    }
  }
  logger.debug("OUT");
}

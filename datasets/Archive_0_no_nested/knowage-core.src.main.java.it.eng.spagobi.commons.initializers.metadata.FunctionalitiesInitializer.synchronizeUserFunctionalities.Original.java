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
      String hql="from SbiUserFunctionality f where f.name=? and f.productType = ?";
      Query hqlQuery=aSession.createQuery(hql);
      hqlQuery.setParameter(0,userFunctionalityName);
      hqlQuery.setParameter(1,productType.getProductTypeId(),Hibernate.INTEGER);
      SbiUserFunctionality aUserFunctionality;
      try {
        aUserFunctionality=(SbiUserFunctionality)hqlQuery.uniqueResult();
      }
 catch (      HibernateException he) {
        throw new SpagoBIRuntimeException("The user functionality [" + userFunctionalityName + "] for the product type ["+ productType.getLabel()+ ","+ productType.getProductTypeId()+ "] returns more than one result.");
      }
      if (aUserFunctionality == null) {
        aUserFunctionality=new SbiUserFunctionality();
        aUserFunctionality.setName(userFunctionalityName);
        aUserFunctionality.setProductType(productType);
        aUserFunctionality.setDescription((String)aUSerFunctionalitySB.getAttribute("description"));
      }
      Object roleTypesObject=roleTypeUserFunctionalitiesSB.getFilteredSourceBeanAttribute("ROLE_TYPE_USER_FUNCTIONALITY","userFunctionality",userFunctionalityName);
      if (roleTypesObject == null) {
        throw new Exception("No role type found for user functionality [" + userFunctionalityName + "] in product type ["+ productType.getLabel()+ "]!!!");
      }
      StringBuffer roleTypesStrBuffer=new StringBuffer();
      Set roleTypes=new HashSet();
      if (aUserFunctionality.getRoleType() != null) {
        roleTypes.addAll(aUserFunctionality.getRoleType());
      }
      if (roleTypesObject instanceof SourceBean) {
        SourceBean roleTypeSB=(SourceBean)roleTypesObject;
        String roleTypeCd=(String)roleTypeSB.getAttribute("roleType");
        roleTypesStrBuffer.append(roleTypeCd);
        SbiDomains domainRoleType=findDomain(aSession,roleTypeCd,"ROLE_TYPE");
        roleTypes.add(domainRoleType);
      }
 else       if (roleTypesObject instanceof List) {
        List roleTypesSB=(List)roleTypesObject;
        Iterator roleTypesIt=roleTypesSB.iterator();
        while (roleTypesIt.hasNext()) {
          SourceBean roleTypeSB=(SourceBean)roleTypesIt.next();
          String roleTypeCd=(String)roleTypeSB.getAttribute("roleType");
          roleTypesStrBuffer.append(roleTypeCd);
          if (roleTypesIt.hasNext()) {
            roleTypesStrBuffer.append(";");
          }
          SbiDomains domainRoleType=findDomain(aSession,roleTypeCd,"ROLE_TYPE");
          roleTypes.add(domainRoleType);
        }
      }
      aUserFunctionality.setRoleType(roleTypes);
      logger.debug("Inserting UserFunctionality with name = [" + aUSerFunctionalitySB.getAttribute("name") + "] associated to role types ["+ roleTypesStrBuffer.toString()+ "]...");
      aSession.save(aUserFunctionality);
    }
  }
  logger.debug("OUT");
}

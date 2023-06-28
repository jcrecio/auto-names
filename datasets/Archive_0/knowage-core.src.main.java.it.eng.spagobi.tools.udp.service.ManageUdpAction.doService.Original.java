@Override public void doService(){
  logger.debug("IN");
  IUdpDAO udpDao=null;
  IDomainDAO daoDomain=DAOFactory.getDomainDAO();
  udpDao=DAOFactory.getUdpDAO();
  udpDao.setUserProfile(getUserProfile());
  udpDao.setUserProfile(getUserProfile());
  HttpServletRequest httpRequest=getHttpRequest();
  MessageBuilder m=new MessageBuilder();
  Locale locale=m.getLocale(httpRequest);
  String serviceType=this.getAttributeAsString(MESSAGE_DET);
  logger.debug("Service type " + serviceType);
  if (serviceType != null && serviceType.equals(UDP_LIST)) {
    try {
      Integer start=getAttributeAsInteger(START);
      Integer limit=getAttributeAsInteger(LIMIT);
      if (start == null) {
        start=START_DEFAULT;
      }
      if (limit == null) {
        limit=LIMIT_DEFAULT;
      }
      Integer totalNum=udpDao.countUdp();
      List<SbiUdp> udpList=udpDao.loadPagedUdpList(start,limit);
      logger.debug("Loaded udp list");
      JSONArray udpJSON=(JSONArray)SerializerFactory.getSerializer("application/json").serialize(udpList,locale);
      JSONObject udpResponseJSON=createJSONResponseUdp(udpJSON,totalNum);
      writeBackToClient(new JSONSuccess(udpResponseJSON));
    }
 catch (    Throwable e) {
      logger.error(e.getMessage(),e);
      try {
        writeBackToClient("Exception occurred while retrieving user defined properties");
      }
 catch (      IOException e1) {
        logger.error(e1.getMessage(),e1);
      }
      throw new SpagoBIServiceException(SERVICE_NAME,"Exception occurred while retrieving user defined properties",e);
    }
  }
 else   if (serviceType != null && serviceType.equals(UDP_DETAIL)) {
    try {
      String name=getAttributeAsString(NAME);
      String label=getAttributeAsString(LABEL);
      String description=getAttributeAsString(DESCRIPTION);
      String typeStr=getAttributeAsString(TYPE);
      Domain tmpDomain=daoDomain.loadDomainByCodeAndValue(UDP_TYPES,typeStr);
      Integer type=tmpDomain.getValueId();
      String familyStr=getAttributeAsString(FAMILY);
      tmpDomain=daoDomain.loadDomainByCodeAndValue(UDP_FAMILIES,familyStr);
      Integer family=tmpDomain.getValueId();
      Boolean isMultivalue=Boolean.valueOf(getAttributeAsBoolean(IS_MULTIVALUE));
      String id=getAttributeAsString(ID);
      SbiUdp udp=new SbiUdp();
      udp.setName(name);
      udp.setLabel(label);
      udp.setDescription(description);
      udp.setTypeId(type);
      udp.setFamilyId(family);
      udp.setIsMultivalue(isMultivalue);
      if (id != null && !id.equals("") && !id.equals("0")) {
        udp.setUdpId(Integer.valueOf(id));
        udpDao.update(udp);
        logger.debug("User attribute " + id + " updated");
        JSONObject attributesResponseSuccessJSON=new JSONObject();
        attributesResponseSuccessJSON.put("success",true);
        attributesResponseSuccessJSON.put("responseText","Operation succeded");
        writeBackToClient(new JSONSuccess(attributesResponseSuccessJSON));
      }
 else {
        Integer udpID=udpDao.insert(udp);
        logger.debug("New User Attribute inserted");
        JSONObject attributesResponseSuccessJSON=new JSONObject();
        attributesResponseSuccessJSON.put("success",true);
        attributesResponseSuccessJSON.put("responseText","Operation succeded");
        attributesResponseSuccessJSON.put("id",udpID);
        writeBackToClient(new JSONSuccess(attributesResponseSuccessJSON));
      }
    }
 catch (    JSONException e) {
      logger.error(e.getMessage(),e);
      throw new SpagoBIServiceException(SERVICE_NAME,"Exception occurred while saving new user attribute",e);
    }
catch (    IOException e) {
      logger.error(e.getMessage(),e);
      throw new SpagoBIServiceException(SERVICE_NAME,"Exception occurred while saving new user attribute",e);
    }
catch (    Exception e) {
      logger.error(e.getMessage(),e);
      throw new SpagoBIServiceException(SERVICE_NAME,"Exception occurred while saving new user attribute",e);
    }
  }
 else   if (serviceType != null && serviceType.equalsIgnoreCase(UDP_DELETE)) {
    Integer id=getAttributeAsInteger(ID);
    try {
      udpDao.delete(id);
      logger.debug("User Attribute deleted");
      writeBackToClient(new JSONAcknowledge("Operation succeded"));
    }
 catch (    Throwable e) {
      logger.error("Exception occurred while deleting role",e);
      throw new SpagoBIServiceException(SERVICE_NAME,"Exception occurred while deleting user attribute",e);
    }
  }
 else   if (serviceType == null) {
    try {
      List types=daoDomain.loadListDomainsByType(UDP_TYPES);
      getSessionContainer().setAttribute("TYPE_LIST",types);
      List families=daoDomain.loadListDomainsByType(UDP_FAMILIES);
      getSessionContainer().setAttribute("FAMILY_LIST",families);
    }
 catch (    EMFUserError e) {
      logger.error(e.getMessage(),e);
      throw new SpagoBIServiceException(SERVICE_NAME,"Exception retrieving types and/or families",e);
    }
  }
  logger.debug("OUT");
}

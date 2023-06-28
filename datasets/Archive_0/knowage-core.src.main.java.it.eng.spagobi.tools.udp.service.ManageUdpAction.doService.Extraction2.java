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
 else   doService_extraction_1(udpDao,daoDomain,serviceType);
  logger.debug("OUT");
}

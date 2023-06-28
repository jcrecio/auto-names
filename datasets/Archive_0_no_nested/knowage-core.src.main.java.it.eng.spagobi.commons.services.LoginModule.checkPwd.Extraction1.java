private boolean checkPwd(SbiUser user) throws Exception {
  logger.debug("IN");
  boolean toReturn=false;
  if (user == null)   return toReturn;
  if (encriptedBefore72(user)) {
    logger.info("Old encrypting method. Change password required.");
    return true;
  }
  Date currentDate=new Date();
  IConfigDAO configDao=DAOFactory.getSbiConfigDAO();
  List lstConfigChecks=configDao.loadConfigParametersByProperties(PROP_NODE);
  logger.debug("checks found on db: " + lstConfigChecks.size());
  for (int i=0; i < lstConfigChecks.size(); i++) {
    Config check=(Config)lstConfigChecks.get(i);
    if ((SpagoBIConstants.CHANGEPWD_CHANGE_FIRST).equals(check.getLabel()) && new Boolean(check.getValueCheck()) == true && user.getDtLastAccess() == null) {
      logger.info("The pwd needs to activate!");
      toReturn=true;
      break;
    }
    if ((SpagoBIConstants.CHANGEPWD_EXPIRED_TIME).equals(check.getLabel()) && user.getDtPwdEnd() != null && currentDate.compareTo(user.getDtPwdEnd()) >= 0) {
      logger.info("The pwd is expiring... it should be changed");
      toReturn=true;
      break;
    }
    if ((SpagoBIConstants.CHANGEPWD_DISACTIVE_TIME).equals(check.getLabel())) {
      Date tmpEndForUnused=null;
      if (user.getDtLastAccess() != null) {
        SimpleDateFormat sdf=new SimpleDateFormat(DATE_FORMAT);
        Calendar cal=Calendar.getInstance();
        cal.set(user.getDtLastAccess().getYear() + 1900,user.getDtLastAccess().getMonth(),user.getDtLastAccess().getDate());
        cal.add(Calendar.MONTH,6);
        try {
          tmpEndForUnused=StringUtilities.stringToDate(sdf.format(cal.getTime()),DATE_FORMAT);
          logger.debug("End Date For Unused: " + tmpEndForUnused);
        }
 catch (        Exception e) {
          logger.error("The control pwd goes on error: " + e);
        }
      }
      if (tmpEndForUnused != null && currentDate.compareTo(tmpEndForUnused) >= 0) {
        logger.info("The pwd is unused more than 6 months! It's locked!!");
        toReturn=true;
        break;
      }
    }
  }
  toReturn=checkPwd_extraction_2(user,toReturn);
  logger.debug("OUT");
  return toReturn;
}

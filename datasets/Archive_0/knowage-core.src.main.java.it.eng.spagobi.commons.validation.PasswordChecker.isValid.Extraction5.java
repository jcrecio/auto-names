/** 
 * This method checks the syntax of new pwd.
 * @return true if the new password is correct
 */
public boolean isValid(String newPwd,String newPwd2) throws Exception {
  IConfigDAO configDao=DAOFactory.getSbiConfigDAO();
  List configChecks=configDao.loadConfigParametersByProperties(PROP_NODE);
  logger.debug("checks found on db: " + configChecks.size());
  if (StringUtilities.isEmpty(newPwd) || StringUtilities.isEmpty(newPwd2)) {
    logger.debug("The new password is empty.");
    throw new EMFUserError(EMFErrorSeverity.ERROR,14011);
  }
  if (!newPwd.equals(newPwd2)) {
    logger.debug("The two passwords are not the same.");
    throw new EMFUserError(EMFErrorSeverity.ERROR,14000);
  }
  for (  Object lstConfigCheck : configChecks) {
    isValid_extraction_1(newPwd,lstConfigCheck);
  }
  return true;
}

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
    Config check=(Config)lstConfigCheck;
    if (check.getValueTypeId() != null && check.getValueCheck() == null) {
      logger.debug("The value configuration on db isn't valorized.");
      Vector v=new Vector();
      v.add(check.getLabel());
      throw new EMFUserError(EMFErrorSeverity.ERROR,14009,v,new HashMap());
    }
    if (check.getLabel().equals(SpagoBIConstants.CHANGEPWDMOD_LEN_MIN)) {
      int pwdLen=newPwd.length();
      if (pwdLen < Integer.parseInt(check.getValueCheck())) {
        logger.debug("The password's length isn't correct.");
        Vector v=new Vector();
        v.add(check.getValueCheck());
        throw new EMFUserError(EMFErrorSeverity.ERROR,14001,v,new HashMap());
      }
    }
    if (check.getLabel().equals(SpagoBIConstants.CHANGEPWDMOD_ALPHA)) {
      char pwdChars[]=new char[newPwd.length()];
      newPwd.getChars(0,newPwd.length(),pwdChars,0);
      boolean containsChar=false;
      for (      char pwdChar : pwdChars) {
        if (check.getValueCheck().contains(String.valueOf(pwdChar))) {
          containsChar=true;
          break;
        }
      }
      if (!containsChar) {
        logger.debug("The password's doesn't contain alphabetical char.");
        throw new EMFUserError(EMFErrorSeverity.ERROR,14002,new Vector(),new HashMap());
      }
    }
    if (check.getLabel().equals(SpagoBIConstants.CHANGEPWDMOD_LOWER_CHAR)) {
      char pwdChars[]=new char[newPwd.length()];
      newPwd.getChars(0,newPwd.length(),pwdChars,0);
      boolean containsChar=false;
      for (      char pwdChar : pwdChars) {
        if (check.getValueCheck().contains(String.valueOf(pwdChar))) {
          containsChar=true;
          break;
        }
      }
      if (!containsChar) {
        logger.debug("The password's doesn't contain lower char.");
        throw new EMFUserError(EMFErrorSeverity.ERROR,14004,new Vector(),new HashMap());
      }
    }
    if (check.getLabel().equals(SpagoBIConstants.CHANGEPWDMOD_UPPER_CHAR)) {
      char pwdChars[]=new char[newPwd.length()];
      newPwd.getChars(0,newPwd.length(),pwdChars,0);
      boolean containsChar=false;
      for (      char pwdChar : pwdChars) {
        if (check.getValueCheck().contains(String.valueOf(pwdChar))) {
          containsChar=true;
          break;
        }
      }
      if (!containsChar) {
        logger.debug("The password's doesn't contain upper char.");
        throw new EMFUserError(EMFErrorSeverity.ERROR,14005,new Vector(),new HashMap());
      }
    }
    if (check.getLabel().equals(SpagoBIConstants.CHANGEPWDMOD_SPECIAL_CHAR)) {
      char pwdChars[]=new char[newPwd.length()];
      newPwd.getChars(0,newPwd.length(),pwdChars,0);
      boolean containsChar=false;
      for (      char pwdChar : pwdChars) {
        if (check.getValueCheck().contains(String.valueOf(pwdChar))) {
          containsChar=true;
          break;
        }
      }
      if (!containsChar) {
        logger.debug("The password's doesn't contain special char.");
        Vector v=new Vector();
        v.add(check.getValueCheck());
        throw new EMFUserError(EMFErrorSeverity.ERROR,14003,v,new HashMap());
      }
    }
    if (check.getLabel().equals(SpagoBIConstants.CHANGEPWDMOD_NUMBER)) {
      char pwdChars[]=new char[newPwd.length()];
      newPwd.getChars(0,newPwd.length(),pwdChars,0);
      boolean containsChar=false;
      for (      char pwdChar : pwdChars) {
        if (check.getValueCheck().contains(String.valueOf(pwdChar))) {
          containsChar=true;
          break;
        }
      }
      if (!containsChar) {
        logger.debug("The password's doesn't contain numeric char.");
        throw new EMFUserError(EMFErrorSeverity.ERROR,14006,new Vector(),new HashMap());
      }
    }
  }
  return true;
}
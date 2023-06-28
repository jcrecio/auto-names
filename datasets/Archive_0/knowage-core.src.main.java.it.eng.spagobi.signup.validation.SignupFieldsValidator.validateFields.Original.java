@Override public JSONArray validateFields(MultivaluedMap<String,String> parameters){
  IMessageBuilder msgBuilder=MessageBuilderFactory.getMessageBuilder();
  JSONArray validationErrors=new JSONArray();
  String strLocale=GeneralUtilities.trim(parameters.getFirst("locale"));
  Locale locale=new Locale(strLocale.substring(0,strLocale.indexOf("_")),strLocale.substring(strLocale.indexOf("_") + 1));
  String name=GeneralUtilities.trim(parameters.getFirst("name"));
  String surname=GeneralUtilities.trim(parameters.getFirst("surname"));
  String username=GeneralUtilities.trim(parameters.getFirst("username"));
  String password=GeneralUtilities.trim(parameters.getFirst("password"));
  String confirmPassword=GeneralUtilities.trim(parameters.getFirst("confirmPassword"));
  String email=GeneralUtilities.trim(parameters.getFirst("email"));
  String birthDate=GeneralUtilities.trim(parameters.getFirst("birthDate"));
  String strUseCaptcha=(parameters.getFirst("useCaptcha") == null) ? "true" : parameters.getFirst("useCaptcha");
  boolean useCaptcha=Boolean.valueOf(strUseCaptcha);
  String captcha=GeneralUtilities.trim(parameters.getFirst("captcha"));
  String terms=parameters.getFirst("terms");
  String modify=GeneralUtilities.trim(parameters.getFirst("modify"));
  try {
    if (name != null)     name=URLDecoder.decode(name,"UTF-8");
    if (surname != null)     surname=URLDecoder.decode(surname,"UTF-8");
    if (username != null)     username=URLDecoder.decode(username,"UTF-8");
    if (password != null)     password=URLDecoder.decode(password,"UTF-8");
    if (confirmPassword != null)     confirmPassword=URLDecoder.decode(confirmPassword,"UTF-8");
    if (email != null)     email=URLDecoder.decode(email,"UTF-8");
    if (birthDate != null)     birthDate=URLDecoder.decode(birthDate,"UTF-8");
  }
 catch (  Exception ex) {
    logger.error(ex.getMessage());
    throw new RuntimeException(ex);
  }
  try {
    if (email == null)     validationErrors.put(new JSONObject("{message: \"" + msgBuilder.getMessage("signup.check.emailMandatory",locale) + "\"}"));
 else {
      if (!validateEmail(email))       validationErrors.put(new JSONObject("{message: \"" + msgBuilder.getMessage("signup.check.emailInvalid",locale) + "\"}"));
    }
    if (birthDate != null)     if (!validateDate(birthDate))     validationErrors.put(new JSONObject("{message: \"" + msgBuilder.getMessage("signup.check.birthdayInvalid",locale) + "\"}"));
    if (name == null)     validationErrors.put(new JSONObject("{message: \"" + msgBuilder.getMessage("signup.check.nameMandatory",locale) + "\"}"));
    if (surname == null)     validationErrors.put(new JSONObject("{message: \"" + msgBuilder.getMessage("signup.check.surnameMandatory",locale) + "\"}"));
    if (modify == null) {
      if (password == null)       validationErrors.put(new JSONObject("{message: \"" + msgBuilder.getMessage("signup.check.pwdMandatory",locale) + "\"}"));
 else {
        if (!validatePassword(password,username)) {
          String errorMsg=msgBuilder.getMessage("signup.check.pwdInvalid",locale);
          validationErrors.put(new JSONObject("{message: '" + JSONUtils.escapeJsonString(errorMsg) + "'}"));
        }
      }
      if (username == null)       validationErrors.put(new JSONObject("{message: \"" + msgBuilder.getMessage("signup.check.usernameMandatory",locale) + "\"}"));
      if (confirmPassword == null)       validationErrors.put(new JSONObject("{message: \"" + msgBuilder.getMessage("signup.check.confirmPwdMandatory",locale) + "\"}"));
      if (useCaptcha && !Boolean.valueOf(terms))       validationErrors.put(new JSONObject("{message: \"" + msgBuilder.getMessage("signup.check.agreeMandatory",locale) + "\"}"));
      if (password != null && !password.equals(defaultPassword) && confirmPassword != null && !confirmPassword.equals(defaultPasswordConfirm))       if (!password.equals(confirmPassword))       validationErrors.put(new JSONObject("{message: \"" + msgBuilder.getMessage("signup.check.pwdNotEqual",locale) + "\"}"));
      if (useCaptcha && captcha == null)       validationErrors.put(new JSONObject("{message: \"" + msgBuilder.getMessage("signup.check.captchaMandatory",locale) + "\"}"));
    }
  }
 catch (  JSONException e1) {
    logger.error(e1.getMessage());
    throw new RuntimeException(e1);
  }
  return validationErrors;
}

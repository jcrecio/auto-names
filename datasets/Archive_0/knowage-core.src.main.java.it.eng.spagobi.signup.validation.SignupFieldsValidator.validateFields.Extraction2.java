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
    validateFields_extraction_1(msgBuilder,validationErrors,locale,name,surname,username,password,confirmPassword,birthDate,useCaptcha,captcha,terms,modify);
  }
 catch (  JSONException e1) {
    logger.error(e1.getMessage());
    throw new RuntimeException(e1);
  }
  return validationErrors;
}

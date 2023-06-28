/** 
 * For each input field type (Numeric, URL, extc:), this method applies validation. Every time a validation fails, an error is added to the <code>errorHandler</code> errors stack.
 * @param serviceRequest The request Source Bean
 * @param errorHandler The errors Stack 
 * @throws Exception If any exception occurs.
 */
private void automaticValidation(SourceBean serviceRequest,EMFErrorHandler errorHandler){
  List fields=_validationStructure.getAttributeAsList("FIELDS.FIELD");
  for (Iterator iter=fields.iterator(); iter.hasNext(); ) {
    Object valueObj=null;
    String value=null;
    List validators=null;
    SourceBean currentValidator=null;
    String validatorName=null;
    Iterator itValidators=null;
    try {
      SourceBean field=(SourceBean)iter.next();
      String fieldName=(String)field.getAttribute("name");
      String fieldLabel=(String)field.getAttribute("label");
      String multivaluesStr=(String)field.getAttribute("multivalues");
      boolean multivalues=(multivaluesStr != null && multivaluesStr.equalsIgnoreCase("true")) ? true : false;
      String separator=null;
      if (multivalues)       separator=(String)field.getAttribute("separator");
      if (fieldLabel != null && fieldLabel.startsWith("#")) {
        String key=fieldLabel.substring(1);
        String bundle=(String)field.getAttribute("bundle");
        String fieldDescription="";
        if ((bundle != null) && !bundle.trim().equals("")) {
          fieldDescription=MessageBundle.getMessage(key,bundle);
        }
 else {
          fieldDescription=MessageBundle.getMessage(key);
        }
        if (fieldDescription != null && !fieldDescription.trim().equals(""))         fieldLabel=fieldDescription;
      }
      if ((fieldLabel == null) || (fieldLabel.trim().equals("")))       fieldLabel=fieldName;
      valueObj=serviceRequest.getAttribute(fieldName);
      if (valueObj != null)       value=valueObj.toString();
      String[] values=null;
      if (multivalues)       values=value.split(separator);
 else       values=new String[]{value};
      validators=field.getAttributeAsList("VALIDATOR");
      for (int i=0; i < values.length; i++) {
        value=values[i];
        itValidators=validators.iterator();
        while (itValidators.hasNext()) {
          currentValidator=(SourceBean)itValidators.next();
          validatorName=(String)currentValidator.getAttribute("validatorName");
          String arg0=(String)currentValidator.getAttribute("arg0");
          String arg1=(String)currentValidator.getAttribute("arg1");
          String arg2=(String)currentValidator.getAttribute("arg2");
          EMFValidationError error=validateField(fieldName,fieldLabel,value,validatorName,arg0,arg1,arg2);
          errorHandler.addError(error);
        }
      }
    }
 catch (    Exception ex) {
      TracerSingleton.log(Constants.NOME_MODULO,TracerSingleton.INFORMATION,"ValidationModule::automaticValidation",ex);
    }
  }
}

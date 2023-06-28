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
      automaticValidation_extraction_1(serviceRequest,errorHandler,iter,value);
    }
 catch (    Exception ex) {
      TracerSingleton.log(Constants.NOME_MODULO,TracerSingleton.INFORMATION,"ValidationModule::automaticValidation",ex);
    }
  }
}

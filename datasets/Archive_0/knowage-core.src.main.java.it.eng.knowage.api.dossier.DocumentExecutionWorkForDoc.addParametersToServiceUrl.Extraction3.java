public String addParametersToServiceUrl(Integer progressthreadId,BIObject biObject,Report reportToUse,StringBuilder serviceUrlBuilder,JSONArray jsonArray,HashMap<String,String> paramMap) throws UnsupportedEncodingException, JSONException {
  List<BIObjectParameter> drivers=biObject.getDrivers();
  if (drivers != null) {
    List<Parameter> parameter=reportToUse.getParameters();
    if (drivers.size() != parameter.size()) {
      throw new SpagoBIRuntimeException("There are a different number of parameters/drivers between document and template");
    }
    Collections.sort(drivers);
    ParametersDecoder decoder=new ParametersDecoder();
    for (    BIObjectParameter biObjectParameter : drivers) {
      boolean found=false;
      String value="";
      String paramName="";
      found=addParametersToServiceUrl_extraction_1(serviceUrlBuilder,paramMap,parameter,decoder,biObjectParameter,found,value,paramName);
      if (!found) {
        throw new SpagoBIRuntimeException("There is no match between document parameters and template parameters.");
      }
    }
  }
  return serviceUrlBuilder.toString();
}

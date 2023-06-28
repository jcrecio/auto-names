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
      for (      Parameter templateParameter : parameter) {
        if (templateParameter.getType().equals("dynamic")) {
          if (templateParameter.getValue() != null && !templateParameter.getValue().isEmpty()) {
            value=templateParameter.getValue();
            addParametersToServiceUrl_extraction_2(decoder,value);
            if (biObjectParameter.getParameterUrlName().equals(templateParameter.getUrlName())) {
              paramName=templateParameter.getUrlName();
              serviceUrlBuilder.append(String.format("&%s=%s",biObjectParameter.getParameterUrlName(),URLEncoder.encode(value,StandardCharsets.UTF_8.toString())));
              serviceUrlBuilder.append(String.format("&%s_description=%s",biObjectParameter.getParameterUrlName(),URLEncoder.encode(templateParameter.getUrlNameDescription(),StandardCharsets.UTF_8.toString())));
              found=true;
              break;
            }
          }
        }
 else {
          if (biObjectParameter.getParameterUrlName().equals(templateParameter.getUrlName())) {
            serviceUrlBuilder.append(String.format("&%s=%s",biObjectParameter.getParameterUrlName(),URLEncoder.encode(templateParameter.getValue(),StandardCharsets.UTF_8.toString())));
            value=templateParameter.getValue();
            paramName=templateParameter.getUrlName();
            addParametersToServiceUrl_extraction_3(templateParameter);
            serviceUrlBuilder.append(String.format("&%s_description=%s",biObjectParameter.getParameterUrlName(),URLEncoder.encode(templateParameter.getUrlNameDescription(),StandardCharsets.UTF_8.toString())));
            found=true;
            break;
          }
        }
      }
      paramMap.put(paramName,value);
      if (!found) {
        throw new SpagoBIRuntimeException("There is no match between document parameters and template parameters.");
      }
    }
  }
  return serviceUrlBuilder.toString();
}

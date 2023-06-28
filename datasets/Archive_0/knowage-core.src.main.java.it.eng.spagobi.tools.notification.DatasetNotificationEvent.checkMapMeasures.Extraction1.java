public Set<String> checkMapMeasures(List<MeasureCatalogueMeasure> measuresOfDataset,BIObject sbiDocument) throws EMFUserError, EMFInternalError, JsonMappingException, JsonParseException, JSONException, IOException {
  Set<String> emailsAddressOfAuthors=new HashSet<String>();
  ObjTemplate templateMap=sbiDocument.getActiveTemplate();
  byte[] templateContentBytes=templateMap.getContent();
  if (templateContentBytes != null) {
    String templateContent=new String(templateContentBytes);
    if (!templateContent.trim().startsWith("<")) {
      if (isValidJSON(templateContent)) {
        JSONObject mapTemplateJSONObject=JSONUtils.toJSONObject(templateContent);
        if (mapTemplateJSONObject.has("storeType")) {
          String storeType=mapTemplateJSONObject.getString("storeType");
          if (storeType.equalsIgnoreCase("virtualStore")) {
            if (mapTemplateJSONObject.has("storeConfig")) {
              JSONObject storeConfigJSON=mapTemplateJSONObject.getJSONObject("storeConfig");
              if (storeConfigJSON.has("params")) {
                JSONObject paramsJSON=storeConfigJSON.getJSONObject("params");
                JSONArray measureLabelsArray=paramsJSON.getJSONArray("labels");
                for (int i=0; i < measureLabelsArray.length(); i++) {
                  String measureLabel=measureLabelsArray.getString(i);
                  for (                  MeasureCatalogueMeasure measureDataset : measuresOfDataset) {
                    if (measureDataset.getLabel().equals(measureLabel)) {
                      String documentCreationUser=sbiDocument.getCreationUser();
                      ISecurityServiceSupplier supplier=SecurityServiceSupplierFactory.createISecurityServiceSupplier();
                      SpagoBIUserProfile userProfile=supplier.createUserProfile(documentCreationUser);
                      HashMap userAttributes=userProfile.getAttributes();
                      checkMapMeasures_extraction_2(emailsAddressOfAuthors,userAttributes);
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
  return emailsAddressOfAuthors;
}

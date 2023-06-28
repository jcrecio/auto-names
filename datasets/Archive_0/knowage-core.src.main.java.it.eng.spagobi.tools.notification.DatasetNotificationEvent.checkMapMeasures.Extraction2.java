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
            checkMapMeasures_extraction_1(measuresOfDataset,sbiDocument,emailsAddressOfAuthors,mapTemplateJSONObject);
          }
        }
      }
    }
  }
  return emailsAddressOfAuthors;
}

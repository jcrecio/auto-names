/** 
 * @return a JSON object representing the input request to the service with the following structure: <code>{ action: STRING , sourceDataset: { label: STRING } , sourceDocument: { id: NUMBER } , document: { id: NUMBER label: STRING name: STRING description: STRING type: STRING engineId: NUMBER metadata: [JSON, ..., JSON] } , customData: { query: [STRING] workseheet: [JSON] smartfilter:  [JSON] } , folders: [STRING, ... , STRING] } </code>
 * @throws JSONException
 * @throws EMFUserError
 */
public JSONObject parseRequest() throws JSONException, EMFUserError {
  JSONObject request=new JSONObject();
  String action=this.getAttributeAsString(MESSAGE_DET);
  request.put("action",action);
  String sourceDatasetLabel=getAttributeAsString(OBJ_DATASET_LABEL);
  if (StringUtilities.isNotEmpty(sourceDatasetLabel)) {
    JSONObject sourceDataset=new JSONObject();
    sourceDataset.put("label",sourceDatasetLabel);
    request.put("sourceDataset",sourceDataset);
  }
  String sourceDocumentId=getAttributeAsString(OBJ_ID);
  if (StringUtilities.isNotEmpty(sourceDocumentId)) {
    JSONObject sourceDocument=new JSONObject();
    sourceDocument.put("id",sourceDocumentId);
    request.put("sourceDocument",sourceDocument);
  }
  JSONObject document=new JSONObject();
  String documentId=getAttributeAsString(ID);
  if (documentId != null)   document.put("id",documentId);
  String label=getAttributeAsString(LABEL);
  if (label != null)   document.put("label",label);
  String name=getAttributeAsString(NAME);
  if (name != null)   document.put("name",name);
  String description=getAttributeAsString(DESCRIPTION);
  if (description != null)   document.put("description",description);
  String visibility=getAttributeAsString(VISIBILITY);
  if (visibility != null)   document.put("visibility",visibility);
  String type=getAttributeAsString(TYPE);
  if (type != null)   document.put("type",type);
  String engineId=getAttributeAsString(ENGINE);
  if (engineId != null)   document.put("engineId",engineId);
  String previewFile=getAttributeAsString(PREVIEW_FILE);
  if (previewFile != null)   document.put("previewFile",previewFile);
  String businessMetadata=getAttributeAsString(BUSINESS_METADATA);
  if (StringUtilities.isNotEmpty(businessMetadata)) {
    JSONObject businessMetadataJSON=new JSONObject(businessMetadata);
    JSONArray metaProperties=new JSONArray();
    JSONArray names=businessMetadataJSON.names();
    for (int i=0; i < names.length(); i++) {
      String key=names.getString(i);
      String value=businessMetadataJSON.getString(key);
      JSONObject metaProperty=new JSONObject();
      metaProperty.put("meta_name",key);
      metaProperty.put("meta_content",value);
      metaProperties.put(metaProperty);
    }
    document.put("metadata",metaProperties);
  }
  request.put("document",document);
  JSONObject customData=new JSONObject();
  if (requestContainsAttribute(FORMVALUES) && StringUtilities.isNotEmpty(getAttributeAsString(FORMVALUES))) {
    JSONObject smartFilterData=getAttributeAsJSONObject(FORMVALUES);
    if (smartFilterData != null)     customData.put("smartFilter",smartFilterData);
  }
  String query=getAttributeAsString(OBJECT_QUERY);
  if (query != null)   customData.put("query",query);
  JSONObject templateContent=getAttributeAsJSONObject(TEMPLATE);
  if (templateContent != null)   customData.put("templateContent",templateContent);
  request.put("customData",customData);
  JSONArray foldersJSON=new JSONArray();
  if (requestContainsAttribute(FUNCTS) && StringUtilities.isNotEmpty(getAttributeAsString(FUNCTS))) {
    foldersJSON=getAttributeAsJSONArray(FUNCTS);
    if (foldersJSON != null)     request.put("folders",foldersJSON);
  }
  String communityFCode=getAttributeAsString(COMMUNITY);
  if (communityFCode != null && !"".equalsIgnoreCase(communityFCode)) {
    if (communityFCode.startsWith("-1")) {
      String realCode=communityFCode.substring(communityFCode.indexOf("__") + 2);
      if (!realCode.equals("")) {
        for (int i=0; i < foldersJSON.length(); i++) {
          if (foldersJSON.get(i).equals(Integer.valueOf(realCode))) {
            foldersJSON.remove(i);
            break;
          }
        }
      }
    }
 else {
      LowFunctionality commF=DAOFactory.getLowFunctionalityDAO().loadLowFunctionalityByCode(communityFCode,false);
      Integer commFId=commF.getId();
      foldersJSON.put(commFId);
    }
  }
  logger.debug("Request succesfully parsed: " + request.toString(3));
  return request;
}

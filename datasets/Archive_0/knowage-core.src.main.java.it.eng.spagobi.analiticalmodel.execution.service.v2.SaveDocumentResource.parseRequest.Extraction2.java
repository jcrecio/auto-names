/** 
 * @return a JSON object representing the input request to the service with the following structure: <code>{ action: STRING , sourceDataset: { label: STRING } , sourceDocument: { id: NUMBER } , document: { id: NUMBER label: STRING name: STRING description: STRING type: STRING engineId: NUMBER metadata: [JSON, ..., JSON] } , customData: { query: [STRING] workseheet: [JSON] smartfilter:  [JSON] } , folders: [STRING, ... , STRING] } </code>
 * @throws JSONException
 * @throws EMFUserError
 */
public JSONObject parseRequest() throws JSONException, EMFUserError {
  JSONObject request=parseRequest_extraction_1();
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
    parseRequest_extraction_2(foldersJSON,communityFCode);
  }
  logger.debug("Request succesfully parsed: " + request.toString(3));
  return request;
}

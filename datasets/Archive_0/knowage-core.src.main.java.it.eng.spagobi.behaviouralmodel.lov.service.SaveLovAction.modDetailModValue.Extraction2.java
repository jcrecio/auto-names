/** 
 * Inserts/Modifies the detail of a value according to the user request. When a value in the LOV list is modified, the <code>modifyModalitiesValue</code> method is called; when a new parameter use mode is added, the <code>inserModalitiesValue</code> method is called. These two cases are differentiated by the <code>mod</code> String input value .
 * @param request The request information contained in a SourceBean Object
 * @param mod A request string used to differentiate insert/modify operations
 * @param response The response SourceBean
 * @throws EMFUserError If an exception occurs
 * @throws SourceBeanException If a SourceBean exception occurs
 */
private void modDetailModValue(String mod) throws EMFUserError, SourceBeanException {
  ModalitiesValue modVal=null;
  HashMap<String,String> logParam=new HashMap();
  boolean responseTestLov;
  boolean responseLoopback;
  try {
    modVal=(ModalitiesValue)session.getAttribute(SpagoBIConstants.MODALITY_VALUE_OBJECT);
    logParam.put("NAME",modVal.getName());
    logParam.put("TYPE",modVal.getITypeCd());
    logParam.put("LABEL",modVal.getLabel());
    String lovProviderModified=getAttributeAsString("lovProviderModified");
    if (lovProviderModified != null && !lovProviderModified.trim().equals(""))     session.setAttribute(SpagoBIConstants.LOV_MODIFIED,lovProviderModified);
    String returnFromTestMsg=getAttributeAsString("RETURN_FROM_TEST_MSG");
    if ("SAVE".equalsIgnoreCase(returnFromTestMsg)) {
      Collection errors=errorHandler.getErrors();
      if (errors != null && errors.size() > 0) {
        Iterator iterator=errors.iterator();
        while (iterator.hasNext()) {
          Object error=iterator.next();
          if (error instanceof EMFValidationError) {
            responseTestLov=true;
            AuditLogUtilities.updateAudit(getHttpRequest(),profile,"LOV.ADD/MODIFY",logParam,"KO");
            return;
          }
        }
      }
      List<String> invisCols=null;
      JSONObject lovConfiguration=getAttributeAsJSONObject(AdmintoolsConstants.LOV_CONFIGURATION);
      String valueColumn=lovConfiguration.optString("valueColumnName");
      String descriptionColumn=lovConfiguration.optString("descriptionColumnName");
      String lovType=lovConfiguration.optString("lovType");
      JSONArray treeLevelColumns=lovConfiguration.optJSONArray("treeLevelsColumns");
      JSONArray visibleColumns=lovConfiguration.optJSONArray("visibleColumnNames");
      JSONArray columns=lovConfiguration.optJSONArray("column");
      ILovDetail lovDetail=modDetailModValue_extraction_2(modVal,invisCols,valueColumn,descriptionColumn,lovType,treeLevelColumns,visibleColumns,columns);
      String newLovProvider=lovDetail.toXML();
      modVal.setLovProvider(newLovProvider);
      session.delAttribute(SpagoBIConstants.MODALITY_VALUE_OBJECT);
      session.delAttribute(SpagoBIConstants.MODALITY);
    }
 else     if ("DO_NOT_SAVE".equalsIgnoreCase(returnFromTestMsg)) {
      modVal=(ModalitiesValue)session.getAttribute(SpagoBIConstants.MODALITY_VALUE_OBJECT);
      return;
    }
    if (mod.equalsIgnoreCase(AdmintoolsConstants.DETAIL_INS)) {
      IModalitiesValueDAO dao=DAOFactory.getModalitiesValueDAO();
      dao.setUserProfile(profile);
      dao.insertModalitiesValue(modVal);
      AuditLogUtilities.updateAudit(getHttpRequest(),profile,"LOV.ADD",logParam,"OK");
    }
 else {
      IModalitiesValueDAO dao=DAOFactory.getModalitiesValueDAO();
      dao.setUserProfile(profile);
      dao.modifyModalitiesValue(modVal);
      AuditLogUtilities.updateAudit(getHttpRequest(),profile,"LOV.MOD",logParam,"OK");
    }
  }
 catch (  Exception ex) {
    logger.error("Cannot fill response container",ex);
    HashMap params=new HashMap();
    modDetailModValue_extraction_3(logParam,params);
    throw new EMFUserError(EMFErrorSeverity.ERROR,1018,new Vector(),params);
  }
  responseLoopback=true;
  session.delAttribute(SpagoBIConstants.LOV_MODIFIED);
}

/** 
 * @param documentDescriptor The descriptor of the target document
 * @param subObjectId        The id of the target subobject (optional). If it is nos specified the metadata properties will be applied to the main object
 * @param metadata           The metadata properties to add. They are encoded as an array of object like the following one <code>{ meta_id: NUMBER , meta_name: STRING , meta_content: STRING } </code>                at least one between attributes meta_id and meta_name must be set. TODO use this method to refactor class SaveMetadataAction
 */
public void saveDocumentMetadataProperties(Object documentDescriptor,Integer subObjectId,List<MetadataDTO> metadata){
  logger.debug("IN");
  try {
    Assert.assertNotNull(documentDescriptor,"Input parameter [documentDescriptor] cannot be null");
    Assert.assertNotNull(metadata,"Input parameter [metadata] cannot be null");
    BIObject document=getDocument(documentDescriptor);
    if (document == null) {
      throw new SpagoBIRuntimeException("Impossible to resolve document [" + documentDescriptor + "]");
    }
    for (    MetadataDTO metadataDTO : metadata) {
      Integer metadataPropertyId=null;
      if (metadataDTO.getMetaId() != null) {
        metadataPropertyId=metadataDTO.getMetaId();
      }
      String metadataPropertyName=metadataDTO.getName();
      if (metadataPropertyId == null && metadataPropertyName == null) {
        throw new SpagoBIRuntimeException("Attributes [" + MetadataJSONSerializer.METADATA_ID + "] and ["+ MetadataJSONSerializer.NAME+ "] cannot be both null");
      }
      if (metadataPropertyId == null) {
        ObjMetadata metadataProperty=getMetadataProperty(metadataPropertyName);
        if (metadataProperty != null) {
          metadataPropertyId=metadataProperty.getObjMetaId();
        }
        if (metadataPropertyId == null) {
          logger.warn("Impossible to resolve metadata property [" + metadataPropertyName + "]");
          continue;
        }
      }
      String documentMetadataPropertyValue=metadataDTO.getText();
      if (documentMetadataPropertyValue == null) {
        throw new SpagoBIRuntimeException("Attributes [" + MetadataJSONSerializer.TEXT + "] of metadata property cannot ["+ metadataPropertyId+ "] be null");
      }
      ObjMetacontent documentMatadataProperty=documentMetadataPropertyDAO.loadObjMetacontent(metadataPropertyId,document.getId(),subObjectId);
      if (documentMatadataProperty == null) {
        logger.debug("ObjMetacontent for metadata id = " + metadataPropertyId + ", biobject id = "+ document.getId()+ ", subobject id = "+ subObjectId+ " was not found, creating a new one...");
        documentMatadataProperty=new ObjMetacontent();
        documentMatadataProperty.setObjmetaId(metadataPropertyId);
        documentMatadataProperty.setBiobjId(document.getId());
        documentMatadataProperty.setSubobjId(subObjectId);
        documentMatadataProperty.setContent(documentMetadataPropertyValue.getBytes("UTF-8"));
        documentMatadataProperty.setCreationDate(new Date());
        documentMatadataProperty.setLastChangeDate(new Date());
        documentMetadataPropertyDAO.insertObjMetacontent(documentMatadataProperty);
      }
 else {
        logger.debug("ObjMetacontent for metadata id = " + metadataPropertyId + ", biobject id = "+ document.getId()+ ", subobject id = "+ subObjectId+ " was found, it will be modified...");
        documentMatadataProperty.setContent(documentMetadataPropertyValue.getBytes("UTF-8"));
        documentMatadataProperty.setLastChangeDate(new Date());
        documentMetadataPropertyDAO.modifyObjMetacontent(documentMatadataProperty);
      }
    }
  }
 catch (  Throwable e) {
    throw new SpagoBIRuntimeException("Exception occurred while saving metadata",e);
  }
 finally {
    logger.debug("OUT");
  }
}

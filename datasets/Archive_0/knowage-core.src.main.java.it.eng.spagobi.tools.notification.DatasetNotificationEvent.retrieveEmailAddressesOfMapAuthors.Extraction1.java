public Set<String> retrieveEmailAddressesOfMapAuthors() throws Exception {
  if (emailAdressesOfMapAuthors == null) {
    Set<String> emailsAddressOfAuthors=new HashSet<String>();
    if (argument != null) {
      if (argument instanceof IDataSet) {
        IDataSet dataset=(IDataSet)argument;
        int datasetId=dataset.getId();
        IBIObjectDAO biObjectDAO=DAOFactory.getBIObjectDAO();
        List mapsDocuments=biObjectDAO.loadBIObjects("MAP",null,null);
        MeasureCatalogue catalogue=null;
        try {
          catalogue=MeasureCatalogueSingleton.getMeasureCatologue();
        }
 catch (        Exception e) {
          logger.debug("Measure Catalog Cannot be instantiated");
        }
        List<MeasureCatalogueMeasure> measuresOfDataset=null;
        if (catalogue != null) {
          measuresOfDataset=catalogue.getMeasureCatalogueMeasure(dataset);
        }
        Iterator iterator=mapsDocuments.iterator();
        while (iterator.hasNext()) {
          Object document=iterator.next();
          if (document instanceof BIObject) {
            BIObject sbiDocument=(BIObject)document;
            if ((sbiDocument.getDataSetId() != null) && (sbiDocument.getDataSetId() == datasetId)) {
              String documentCreationUser=sbiDocument.getCreationUser();
              ISecurityServiceSupplier supplier=SecurityServiceSupplierFactory.createISecurityServiceSupplier();
              SpagoBIUserProfile userProfile=supplier.createUserProfile(documentCreationUser);
              HashMap userAttributes=userProfile.getAttributes();
              retrieveEmailAddressesOfMapAuthors_extraction_2(emailsAddressOfAuthors,userAttributes);
            }
 else {
              if ((measuresOfDataset != null) && (!measuresOfDataset.isEmpty())) {
                Set<String> emailMapMeasures=checkMapMeasures(measuresOfDataset,sbiDocument);
                emailsAddressOfAuthors.addAll(emailMapMeasures);
              }
            }
          }
        }
      }
    }
    this.setEmailAdressesOfMapAuthors(emailsAddressOfAuthors);
    return emailsAddressOfAuthors;
  }
 else {
    return this.getEmailAdressesOfMapAuthors();
  }
}

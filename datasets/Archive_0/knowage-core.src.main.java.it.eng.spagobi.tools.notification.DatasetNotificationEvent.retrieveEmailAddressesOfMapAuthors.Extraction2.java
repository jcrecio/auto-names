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
        retrieveEmailAddressesOfMapAuthors_extraction_1(emailsAddressOfAuthors,datasetId,mapsDocuments,measuresOfDataset);
      }
    }
    this.setEmailAdressesOfMapAuthors(emailsAddressOfAuthors);
    return emailsAddressOfAuthors;
  }
 else {
    return this.getEmailAdressesOfMapAuthors();
  }
}

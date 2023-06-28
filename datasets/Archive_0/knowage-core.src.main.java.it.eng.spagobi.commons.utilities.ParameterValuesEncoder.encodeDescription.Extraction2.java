/** 
 * Get the description of a BIObjectParameter and encode it's description.. In this way we create a new parameter with the description of the parameter to pass at the engine
 * @param biobjPar the parameter
 * @return a string with the encoded description
 */
public String encodeDescription(BIObjectParameter biobjPar){
  logger.debug("IN");
  if (biobjPar.getParameterValues() == null) {
    logger.debug("biobjPar.getParameterValues() == null");
    return null;
  }
  Parameter parameter=biobjPar.getParameter();
  if (parameter != null) {
    if (biobjPar.getParameterValuesDescription() == null) {
      return "";
    }
    ModalitiesValue modValue=parameter.getModalityValue();
    return encodeDescription_extraction_1(biobjPar,modValue);
  }
 else {
    Integer parId=biobjPar.getParID();
    String type=null;
    if (parId == null) {
      logger.warn("Parameter object nor parameter id are set into BiObjectPrameter with label = " + biobjPar.getLabel() + " of document with id = "+ biobjPar.getBiObjectID());
    }
 else {
      try {
        Parameter aParameter=DAOFactory.getParameterDAO().loadForDetailByParameterID(parId);
        type=aParameter.getType();
      }
 catch (      EMFUserError e) {
        logger.warn("Error loading parameter with id = " + biobjPar.getParID());
      }
    }
    List values=biobjPar.getParameterValuesDescription();
    if (values != null && values.size() > 0) {
      return encodeDescription_extraction_2(biobjPar,values);
    }
 else     return "";
  }
}

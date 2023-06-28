public void initGeoDimensionLevels(){
  logger.debug("IN");
  try {
    List<GeoLayer> geoLayers=new ArrayList<GeoLayer>();
    try {
      ISbiGeoLayersDAO geoLayersDAO=DAOFactory.getSbiGeoLayerDao();
      IEngUserProfile profile=UserUtilities.getUserProfile();
      geoLayers=geoLayersDAO.loadAllLayers(null,profile);
      if (geoLayers != null) {
        levels=new ArrayList<Properties>();
        for (int i=0; i < geoLayers.size(); i++) {
          GeoLayer level=geoLayers.get(i);
          String name=level.getName();
          String layerLabel="";
          String layerId="";
          String layerName="";
          String layer_file="";
          String layer_url="";
          String layer_zoom="";
          String layer_cetral_point="";
          String layer_params="";
          String layer_options="";
          Properties props=initGeoDimensionLevels_extraction_1(level,name,layerLabel,layerId,layerName,layer_file,layer_url,layer_zoom,layer_cetral_point,layer_params,layer_options);
          levels.add(props);
        }
      }
    }
 catch (    EMFUserError e) {
      logger.error("Error getting layer properties",e);
      throw new SpagoBIRuntimeException("Error getting layer properties",e);
    }
  }
 catch (  Throwable t) {
    throw new RuntimeException("An error occured while loading geo dimension levels' properties from layers catalogue",t);
  }
 finally {
    logger.debug("OUT");
  }
}

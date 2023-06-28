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
          if (level.getLayerDef() != null) {
            JSONObject js=null;
            try {
              js=new JSONObject(new String(level.getLayerDef()));
            }
 catch (            JSONException e) {
              logger.error("Error serializing the definition of the layer" + level.getLabel(),e);
              throw new SpagoBIRuntimeException("Error serializing the definition of the layer" + level.getLabel(),e);
            }
            if (js != null) {
              String[] properties=JSONObject.getNames(js);
              if (properties != null) {
                layerId=(String)js.get("propsId");
                layerLabel=(String)js.get("propsLabel");
                layerName=(String)js.get("propsName");
                layer_file=(String)js.get("propsFile");
                layer_url=(String)js.get("propsUrl");
                layer_zoom=(String)js.get("propsZoom");
                layer_cetral_point=(String)js.get("propsCentralPoint");
                layer_params=(String)js.get("propsParams");
                layer_options=(String)js.get("propsOptions");
              }
            }
          }
          Properties props=new Properties();
          if (name != null && !"".equals(name))           props.setProperty("name",name);
          if (layerName != null && !"".equals(layerName))           props.setProperty("layerName",layerName);
          if (layerLabel != null && !"".equals(layerLabel))           props.setProperty("layerLabel",layerLabel);
          if (layerId != null && !"".equals(layerId))           props.setProperty("layerId",layerId);
          if (layer_file != null && !"".equals(layer_file))           props.setProperty("layer_file",layer_file);
          if (layer_url != null && !"".equals(layer_url))           props.setProperty("layer_url",layer_url);
          if (layer_zoom != null && !"".equals(layer_zoom))           props.setProperty("layer_zoom",layer_zoom);
          if (layer_cetral_point != null && !"".equals(layer_cetral_point))           props.setProperty("layer_cetral_point",layer_cetral_point);
          if (layer_params != null && !"".equals(layer_params))           props.setProperty("layer_params",layer_params);
          if (layer_options != null && !"".equals(layer_options))           props.setProperty("layer_options",layer_options);
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

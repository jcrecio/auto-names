private void addItemForJSTree(StringBuffer htmlStream,LowFunctionality folder,boolean isRoot,boolean isInitialPath){
  String nameLabel=folder.getName();
  String name=msgBuilder.getMessage(nameLabel,"messages",httpRequest);
  String codeType=folder.getCodType();
  Integer idFolder=folder.getId();
  Integer parentId=null;
  if (isInitialPath)   parentId=new Integer(dTreeRootId);
 else   parentId=folder.getParentId();
  if (isRoot) {
    htmlStream.append("	treeCMS.add(" + idFolder + ", "+ dTreeRootId+ ",'"+ name+ "', '', '', '', '', '', 'true');\n");
  }
 else {
    if (codeType.equalsIgnoreCase(SpagoBIConstants.LOW_FUNCTIONALITY_TYPE_CODE)) {
      String imgFolder=urlBuilder.getResourceLinkByTheme(httpRequest,"/img/treefolder.gif",currTheme);
      String imgFolderOp=urlBuilder.getResourceLinkByTheme(httpRequest,"/img/treefolderopen.gif",currTheme);
      htmlStream.append("	treeCMS.add(" + idFolder + ", "+ parentId+ ",'"+ name+ "', '', '', '', '"+ imgFolder+ "', '"+ imgFolderOp+ "', '', '');\n");
      List objects=folder.getBiObjects();
      for (Iterator it=objects.iterator(); it.hasNext(); ) {
        BIObject obj=(BIObject)it.next();
        Engine engine=obj.getEngine();
        if (engine != null) {
          if (!EngineUtilities.isExternal(obj.getEngine()) && !engine.getClassName().equals("it.eng.spagobi.engines.kpi.SpagoBIKpiInternalEngine")) {
            continue;
          }
          if (!(engine.getDriverName().equals("it.eng.spagobi.engines.drivers.birt.BirtReportDriver") || engine.getDriverName().equals("it.eng.spagobi.engines.drivers.commonj.CommonjDriver") || engine.getDriverName().equals("it.eng.spagobi.engines.drivers.talend.TalendDriver"))) {
            continue;
          }
        }
        String biObjType=obj.getBiObjectTypeCode();
        String imgUrl="/img/objecticon_" + biObjType + ".png";
        String userIcon=urlBuilder.getResourceLinkByTheme(httpRequest,imgUrl,currTheme);
        String biObjState=obj.getStateCode();
        String stateImgUrl="/img/stateicon_" + biObjState + ".png";
        String stateIcon=urlBuilder.getResourceLinkByTheme(httpRequest,stateImgUrl,currTheme);
        Integer idObj=obj.getId();
        String stateObj=obj.getStateCode();
        if (stateObj.equalsIgnoreCase("REL")) {
          String checked="";
          checked=addItemForJSTree_extraction_2(obj,checked);
          htmlStream.append("	treeCMS.add(" + dTreeObjects-- + ", "+ idFolder+ ",'<img src=\\'"+ stateIcon+ "\\' /> "+ obj.getName()+ "', '', '', '', '"+ userIcon+ "', '', '', '', 'biobject', '"+ obj.getId()+ "', '"+ checked+ "' );\n");
        }
      }
    }
  }
}

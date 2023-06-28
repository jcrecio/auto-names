@Override public Object serialize(Object o,Locale locale) throws SerializationException {
  JSONObject result=null;
  contextName=KnowageSystemConfiguration.getKnowageContext();
  if (!(o instanceof List)) {
    throw new SerializationException("MenuListJSONSerializer is unable to serialize object of type: " + o.getClass().getName());
  }
  try {
    List filteredMenuList=(List)o;
    JSONArray tempFirstLevelMenuList=new JSONArray();
    JSONArray userMenu=new JSONArray();
    if (filteredMenuList != null && !filteredMenuList.isEmpty()) {
      result=new JSONObject();
      JSONArray menuUserList=new JSONArray();
      MessageBuilder msgBuild=new MessageBuilder();
      JSONObject personal=new JSONObject();
      String userMenuMessage=msgBuild.getMessage("menu.UserMenu",locale);
      personal.put(ICON_CLS,"spagobi");
      personal.put(TOOLTIP,userMenuMessage);
      personal.put(ICON_ALIGN,"top");
      personal.put(SCALE,"large");
      personal.put(PATH,userMenuMessage);
      personal.put(TARGET,"_self");
      tempFirstLevelMenuList.put(personal);
      boolean isAdmin=false;
      for (int i=0; i < filteredMenuList.size(); i++) {
        Menu menuElem=(Menu)filteredMenuList.get(i);
        menuUserList=serialize_extraction_1(locale,filteredMenuList,userMenu,menuUserList,msgBuild,personal,menuElem);
      }
    }
    if (!UserUtilities.isTechnicalUser(this.getUserProfile())) {
      userMenu=createEndUserMenu(locale,1,new JSONArray());
    }
    JSONArray fixedMenuPart=createFixedMenu(locale,1,new JSONArray());
    JSONObject wholeMenu=new JSONObject();
    wholeMenu.put("fixedMenu",fixedMenuPart);
    wholeMenu.put("userMenu",userMenu);
    wholeMenu.put("customMenu",tempFirstLevelMenuList);
    result=wholeMenu;
  }
 catch (  Throwable t) {
    throw new SerializationException("An error occurred while serializing object: " + o,t);
  }
 finally {
  }
  return result;
}

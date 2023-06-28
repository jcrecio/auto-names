@Override public Object serialize(Object o,Locale locale) throws SerializationException {
  JSONArray result=null;
  contextName=KnowageSystemConfiguration.getKnowageContext();
  if (!(o instanceof List)) {
    throw new SerializationException("MenuListJSONSerializer is unable to serialize object of type: " + o.getClass().getName());
  }
  try {
    List filteredMenuList=(List)o;
    JSONArray tempFirstLevelMenuList=new JSONArray();
    if (filteredMenuList != null && !filteredMenuList.isEmpty()) {
      result=new JSONArray();
      JSONArray menuUserList=new JSONArray();
      MessageBuilder msgBuild=new MessageBuilder();
      JSONObject home=new JSONObject();
      JSONObject personal=new JSONObject();
      home.put(ICON_CLS,"home");
      home.put(TOOLTIP,"Home");
      home.put(ICON_ALIGN,"top");
      home.put(SCALE,"large");
      home.put(PATH,"Home");
      home.put(LABEL,HOME);
      home.put(TARGET,"_self");
      home.put(HREF,"javascript:goHome(null, 'spagobi');");
      String userMenu=msgBuild.getMessage("menu.UserMenu",locale);
      personal.put(ICON_CLS,"spagobi");
      personal.put(TOOLTIP,userMenu);
      personal.put(ICON_ALIGN,"top");
      personal.put(SCALE,"large");
      personal.put(PATH,userMenu);
      personal.put(TARGET,"_self");
      tempFirstLevelMenuList.put(home);
      tempFirstLevelMenuList.put(personal);
      boolean isAdmin=false;
      for (int i=0; i < filteredMenuList.size(); i++) {
        Menu menuElem=(Menu)filteredMenuList.get(i);
        String path=MenuUtilities.getMenuPath(filteredMenuList,menuElem,locale);
        if (menuElem.getLevel().intValue() == 1) {
          JSONObject temp=new JSONObject();
          if (!menuElem.isAdminsMenu()) {
            menuUserList=createUserMenuElement(filteredMenuList,menuElem,locale,1,menuUserList);
            personal.put(MENU,menuUserList);
            if (menuElem.getHasChildren()) {
              List lstChildrenLev2=menuElem.getLstChildren();
              JSONArray tempMenuList2=(JSONArray)getChildren(filteredMenuList,lstChildrenLev2,1,locale);
              temp.put(MENU,tempMenuList2);
            }
          }
 else {
            serialize_extraction_2(locale,msgBuild,menuElem,path,temp);
            if (menuElem.getHasChildren()) {
              List lstChildrenLev2=menuElem.getLstChildren();
              JSONArray tempMenuList=(JSONArray)getChildren(filteredMenuList,lstChildrenLev2,1,locale);
              temp.put(MENU,tempMenuList);
            }
            tempFirstLevelMenuList.put(temp);
          }
        }
      }
    }
    if (!UserUtilities.isTechnicalUser(this.getUserProfile())) {
      tempFirstLevelMenuList=createEndUserMenu(locale,1,tempFirstLevelMenuList);
    }
    tempFirstLevelMenuList=createFixedMenu(locale,1,tempFirstLevelMenuList);
    result=tempFirstLevelMenuList;
  }
 catch (  Throwable t) {
    throw new SerializationException("An error occurred while serializing object: " + o,t);
  }
 finally {
  }
  return result;
}

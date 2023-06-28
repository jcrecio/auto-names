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
            serialize_extraction_2(locale,filteredMenuList,msgBuild,menuElem,path,temp);
            if (menuElem.getCode().equals("doc_test_angular") && UserUtilities.isAdministrator(this.getUserProfile())) {
              continue;
            }
            userMenu.put(temp);
          }
        }
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

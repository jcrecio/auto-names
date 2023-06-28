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
            isAdmin=true;
            temp.put(ICON_CLS,menuElem.getIconCls());
            String text="";
            if (!menuElem.isAdminsMenu() || !menuElem.getName().startsWith("#"))             text=menuElem.getName();
 else {
              if (menuElem.getName().startsWith("#")) {
                String titleCode=menuElem.getName().substring(1);
                text=msgBuild.getMessage(titleCode,locale);
              }
 else {
                text=menuElem.getName();
              }
            }
            temp.put(TOOLTIP,text);
            temp.put(ICON_ALIGN,"top");
            temp.put(SCALE,"large");
            temp.put(PATH,path);
            temp.put(TARGET,"_self");
            if (menuElem.getCode() != null && (menuElem.getCode().equals("doc_admin_angular") || menuElem.getCode().equals("doc_test_angular"))) {
              temp.put(HREF,"javascript:javascript:execDirectUrl('" + contextName + HREF_DOC_BROWSER_ANGULAR+ "', '"+ text+ "')");
              temp.put(FIRST_URL,contextName + HREF_DOC_BROWSER_ANGULAR);
              temp.put(LINK_TYPE,"execDirectUrl");
            }
            if (menuElem.getCode() != null && menuElem.getCode().equals("workspace")) {
              temp.put(HREF,"javascript:javascript:execDirectUrl('" + contextName + HREF_DOC_BROWSER_WORKSPACE+ "', '"+ text+ "')");
              temp.put(FIRST_URL,contextName + HREF_DOC_BROWSER_WORKSPACE);
              temp.put(LINK_TYPE,"execDirectUrl");
            }
            if (menuElem.getHasChildren()) {
              List lstChildrenLev2=menuElem.getLstChildren();
              JSONArray tempMenuList=(JSONArray)getChildren(filteredMenuList,lstChildrenLev2,1,locale);
              temp.put(MENU,tempMenuList);
            }
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

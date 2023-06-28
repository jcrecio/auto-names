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
      serialize_extraction_1(locale,filteredMenuList,tempFirstLevelMenuList,menuUserList,msgBuild,home,personal);
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

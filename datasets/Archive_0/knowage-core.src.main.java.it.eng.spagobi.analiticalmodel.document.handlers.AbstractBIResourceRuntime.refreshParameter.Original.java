private void refreshParameter(AbstractDriver biparam,JSONObject jsonObject,boolean transientMode){
  logger.debug("IN");
  Assert.assertNotNull(biparam,"Parameter in input is null!!");
  Assert.assertNotNull(jsonObject,"JSONObject in input is null!!");
  String nameUrl=biparam.getParameterUrlName();
  List values=new ArrayList();
  List descrs=new ArrayList();
  try {
    Object o=jsonObject.opt(nameUrl);
    if (o != null) {
      if (o instanceof JSONArray) {
        JSONArray jsonArray=(JSONArray)o;
        for (int c=0; c < jsonArray.length(); c++) {
          Object anObject=jsonArray.get(c);
          if (anObject != null) {
            values.add(anObject.toString());
          }
        }
      }
 else       if (o.toString().startsWith("{;{}")) {
      }
 else {
        String valToInsert=o.toString();
        valToInsert=valToInsert.trim();
        if (!valToInsert.isEmpty()) {
          values.add(valToInsert);
        }
      }
    }
    Object oDescr=jsonObject.opt(nameUrl + "_field_visible_description");
    if (oDescr != null) {
      if (oDescr instanceof JSONArray) {
        JSONArray jsonArray=(JSONArray)oDescr;
        for (int c=0; c < jsonArray.length(); c++) {
          Object anObject=jsonArray.get(c);
          if (anObject != null) {
            descrs.add(anObject.toString());
          }
        }
      }
 else       if (oDescr instanceof Number) {
        descrs.add(oDescr);
      }
 else {
        StringTokenizer stk=new StringTokenizer((String)oDescr,";");
        while (stk.hasMoreTokens()) {
          descrs.add(stk.nextToken());
        }
      }
    }
  }
 catch (  JSONException e) {
    logger.error("Cannot get " + nameUrl + " values from JSON object",e);
    throw new SpagoBIServiceException("Cannot retrieve values for biparameter " + biparam.getLabel(),e);
  }
  if (values.size() > 0) {
    logger.debug("Updating values of biparameter " + biparam.getLabel() + " to "+ values.toString());
    biparam.setParameterValues(values);
    biparam.setParameterValuesDescription(descrs);
  }
 else {
    logger.debug("Erasing values of biparameter " + biparam.getLabel());
    biparam.setParameterValues(null);
    biparam.setParameterValuesDescription(null);
  }
  biparam.setTransientParmeters(transientMode);
  logger.debug("OUT");
}

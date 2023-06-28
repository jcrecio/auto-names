/** 
 * Builds Table list footer, reading all request information.
 * @throws JspException If any Exception occurs.
 */
protected void makeFooterList() throws JspException {
  String pageNumberString=(String)_content.getAttribute("PAGED_LIST.PAGE_NUMBER");
  int pageNumber=1;
  try {
    pageNumber=Integer.parseInt(pageNumberString);
  }
 catch (  NumberFormatException ex) {
    TracerSingleton.log(Constants.NOME_MODULO,TracerSingleton.WARNING,"ListTag::makeNavigationButton:: PAGE_NUMBER nullo");
  }
  String pagesNumberString=(String)_content.getAttribute("PAGED_LIST.PAGES_NUMBER");
  int pagesNumber=1;
  try {
    pagesNumber=Integer.parseInt(pagesNumberString);
  }
 catch (  NumberFormatException ex) {
    TracerSingleton.log(Constants.NOME_MODULO,TracerSingleton.WARNING,"ListTag::makeNavigationButton:: PAGES_NUMBER nullo");
  }
  int prevPage=pageNumber - 1;
  if (prevPage < 1)   prevPage=1;
  int nextPage=pageNumber + 1;
  if (nextPage > pagesNumber)   nextPage=pagesNumber;
  int startRangePages=1;
  int endRangePages=END_RANGE_PAGES;
  int deltaPages=pagesNumber - endRangePages;
  String dotsStart=null;
  String dotsEnd=null;
  if (deltaPages > 0) {
    startRangePages=(pageNumber - 3 > 0) ? pageNumber - 3 : 1;
    endRangePages=((pageNumber + 3 <= pagesNumber) && (pageNumber + 3 > END_RANGE_PAGES)) ? pageNumber + 3 : END_RANGE_PAGES;
    if (pageNumber + 3 <= pagesNumber) {
      if (pageNumber + 3 > END_RANGE_PAGES)       endRangePages=pageNumber + 3;
 else       endRangePages=END_RANGE_PAGES;
    }
 else {
      startRangePages=startRangePages - (pageNumber + 3 - pagesNumber);
      endRangePages=pagesNumber;
    }
    if (endRangePages < pagesNumber)     dotsEnd="... ";
    if (startRangePages > 1)     dotsStart="... ";
  }
 else {
    startRangePages=1;
    endRangePages=pagesNumber;
  }
  _htmlStream.append(" <TABLE CELLPADDING=0 CELLSPACING=0  WIDTH='100%' BORDER=0>\n");
  _htmlStream.append("	<TR>\n");
  String pageLabel=msgBuilder.getMessage("ListTag.pageLable","messages",httpRequest);
  String pageOfLabel=msgBuilder.getMessage("ListTag.pageOfLable","messages",httpRequest);
  _htmlStream.append("		<TD class='portlet-section-footer' style='vertical-align:top;horizontal-align:left;width:30%;'>\n");
  _htmlStream.append("				<font class='aindice'>&nbsp;" + pageLabel + " "+ pageNumber+ " "+ pageOfLabel+ " "+ pagesNumber+ "&nbsp;</font>\n");
  _htmlStream.append("		</TD>\n");
  _htmlStream.append("		<TD  class='portlet-section-footer' style='vertical-align:top;horizontal-align:center;width:40%;'>\n");
  if (pageNumber != 1) {
    _htmlStream.append("			<A href=\"" + _firstUrl + "\"><IMG src='"+ urlBuilder.getResourceLinkByTheme(httpRequest,"/img/commons/2leftarrow.png",currTheme)+ "' border=0></a>\n");
    _htmlStream.append("			<A href=\"" + _prevUrl + "\"><IMG src='"+ urlBuilder.getResourceLinkByTheme(httpRequest,"/img/commons/1leftarrow.png",currTheme)+ "' border=0></a>\n");
  }
 else {
    _htmlStream.append("			<IMG src='" + urlBuilder.getResourceLinkByTheme(httpRequest,"/img/commons/2leftarrow.png",currTheme) + "' border=0 />\n");
    _htmlStream.append("			<IMG src='" + urlBuilder.getResourceLinkByTheme(httpRequest,"/img/commons/1leftarrow.png",currTheme) + "' border=0 />\n");
  }
  if (dotsStart != null) {
    _htmlStream.append("<A style='vertical-align:top;'>" + dotsStart + "</a>\n");
    _htmlStream.append("&nbsp;&nbsp;\n");
  }
  for (int i=startRangePages; i <= endRangePages; i++) {
    HashMap tmpParamsMap=new HashMap();
    tmpParamsMap.putAll(_providerUrlMap);
    tmpParamsMap.put("MESSAGE","LIST_PAGE");
    tmpParamsMap.put("LIST_PAGE",String.valueOf(i));
    String tmpUrl=createUrl(tmpParamsMap);
    String ORDER=(String)_serviceRequest.getAttribute("ORDER");
    String FIELD_ORDER=(String)_serviceRequest.getAttribute("FIELD_ORDER");
    if (FIELD_ORDER != null && ORDER != null) {
      tmpParamsMap.put("FIELD_ORDER",FIELD_ORDER);
      tmpParamsMap.put("ORDER",ORDER);
    }
    String valueFilter=(String)_serviceRequest.getAttribute(SpagoBIConstants.VALUE_FILTER);
    String typeValueFilter=(String)_serviceRequest.getAttribute(SpagoBIConstants.TYPE_VALUE_FILTER);
    String columnFilter=(String)_serviceRequest.getAttribute(SpagoBIConstants.COLUMN_FILTER);
    String typeFilter=(String)_serviceRequest.getAttribute(SpagoBIConstants.TYPE_FILTER);
    if (valueFilter != null && columnFilter != null && typeFilter != null) {
      tmpParamsMap.put(SpagoBIConstants.VALUE_FILTER,valueFilter);
      tmpParamsMap.put(SpagoBIConstants.TYPE_VALUE_FILTER,typeValueFilter);
      tmpParamsMap.put(SpagoBIConstants.COLUMN_FILTER,columnFilter);
      tmpParamsMap.put(SpagoBIConstants.TYPE_FILTER,typeFilter);
      tmpUrl=createUrl(tmpParamsMap);
    }
    tmpUrl=StringEscapeUtils.escapeHtml(tmpUrl);
    _htmlStream.append("	<A style='vertical-align:top;' href=\"" + tmpUrl + "\">"+ String.valueOf(i)+ "</a>\n");
    _htmlStream.append("&nbsp;&nbsp;\n");
  }
  if (dotsEnd != null) {
    _htmlStream.append("<A style='vertical-align:top;'>" + dotsEnd + "</a>\n");
  }
  if (pageNumber != pagesNumber) {
    _htmlStream.append("			<A href=\"" + _nextUrl + "\"><IMG src='"+ urlBuilder.getResourceLinkByTheme(httpRequest,"/img/commons/1rightarrow.png",currTheme)+ "' border=0 /></a>\n");
    _htmlStream.append("			<A href=\"" + _lastUrl + "\"><IMG src='"+ urlBuilder.getResourceLinkByTheme(httpRequest,"/img/commons/2rightarrow.png",currTheme)+ "' border=0 /></a>\n");
  }
 else {
    _htmlStream.append("			<IMG src='" + urlBuilder.getResourceLinkByTheme(httpRequest,"/img/commons/1rightarrow.png",currTheme) + "' border=0>\n");
    _htmlStream.append("			<IMG src='" + urlBuilder.getResourceLinkByTheme(httpRequest,"/img/commons/2rightarrow.png",currTheme) + "' border=0>\n");
  }
  _htmlStream.append("		</TD>\n");
  _htmlStream.append("		<TD class='portlet-section-footer' style='width:30%;'>\n");
  _htmlStream.append("			    &nbsp;\n");
  _htmlStream.append("		</TD>\n");
  _htmlStream.append("	</TR>\n");
  _htmlStream.append("</TABLE>\n");
}

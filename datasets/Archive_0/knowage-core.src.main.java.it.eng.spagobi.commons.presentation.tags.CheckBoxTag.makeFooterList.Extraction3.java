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
  String dotsStart="";
  String dotsEnd="";
  if (deltaPages > 0) {
    startRangePages=makeFooterList_extraction_1(pageNumber,pagesNumber);
    if (pageNumber + 3 <= pagesNumber) {
      endRangePages=makeFooterList_extraction_2(pageNumber);
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
  _htmlStream.append("		<TD class='portlet-section-footer' align='left' width='10%'>\n");
  _htmlStream.append("				<font class='aindice'>&nbsp;" + pageLabel + " "+ pageNumber+ " "+ pageOfLabel+ " "+ pagesNumber+ "&nbsp;</font>\n");
  _htmlStream.append("		</TD>\n");
  _htmlStream.append("		<TD  class='portlet-section-footer' width='28%'>\n");
  _htmlStream.append("			    &nbsp;\n");
  _htmlStream.append("		</TD>\n");
  if (pageNumber != 1) {
    _htmlStream.append("	<TD class='portlet-section-footer' valign='center' align='left' width='1%'>\n");
    _htmlStream.append("<input type='image' " + "name='" + "firstPage"+ "' "+ "src ='" + urlBuilder.getResourceLinkByTheme(httpRequest,"/img/commons/2leftarrow.png",currTheme) + "' "+ "align='right' border='0' "+ "onClick='changePage(this.name)' "+ "alt='"+ "GO To First Page"+ "'>\n");
    _htmlStream.append("	</TD>\n");
    _htmlStream.append("	<TD class='portlet-section-footer' valign='center'  align='left' width='1%'>\n");
    _htmlStream.append("<input type='image' " + "name='" + "prevPage"+ "' "+ "src ='" + urlBuilder.getResourceLinkByTheme(httpRequest,"/img/commons/1leftarrow.png",currTheme) + "' "+ "align='right' border='0' "+ "onClick='changePage(this.name)' "+ "alt='"+ "GO To Previous Page"+ "'>\n");
    _htmlStream.append("	</TD>\n");
  }
 else {
    _htmlStream.append("	<TD class='portlet-section-footer' valign='center' align='left' width='1%'>\n");
    _htmlStream.append("			<IMG src='" + urlBuilder.getResourceLinkByTheme(httpRequest,"/img/commons/2leftarrow.png",currTheme) + "' ALIGN=RIGHT border=0 />\n");
    _htmlStream.append("	</TD>\n");
    _htmlStream.append("	<TD class='portlet-section-footer' valign='center' align='left' width='1%'>\n");
    _htmlStream.append("			<IMG src='" + urlBuilder.getResourceLinkByTheme(httpRequest,"/img/commons/1leftarrow.png",currTheme) + "' ALIGN=RIGHT border=0 />\n");
    _htmlStream.append("	</TD>\n");
  }
  makeFooterList_extraction_3(pageNumber,pagesNumber,startRangePages,endRangePages,dotsStart,dotsEnd);
}

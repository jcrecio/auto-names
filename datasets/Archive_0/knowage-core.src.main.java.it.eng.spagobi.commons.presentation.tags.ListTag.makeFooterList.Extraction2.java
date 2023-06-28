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
  makeFooterList_extraction_3(pageNumber,pagesNumber,startRangePages,endRangePages,dotsStart,dotsEnd);
  _htmlStream.append("			    &nbsp;\n");
  _htmlStream.append("		</TD>\n");
  _htmlStream.append("	</TR>\n");
  _htmlStream.append("</TABLE>\n");
}

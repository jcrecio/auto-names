/** 
 * This was really interesting to write.
 * @author Konloch
 */
public static void search(JTextArea textArea,String search,boolean forwardSearchDirection,boolean caseSensitiveSearch){
  try {
    if (search.isEmpty()) {
      highlight(textArea,"",caseSensitiveSearch);
      return;
    }
    int startLine=textArea.getDocument().getDefaultRootElement().getElementIndex(textArea.getCaretPosition()) + 1;
    int currentLine=1;
    boolean canSearch=false;
    String[] test=textArea.getText().split("\r?\n");
    int lastGoodLine=-1;
    int firstPos=-1;
    boolean found=false;
    if (forwardSearchDirection) {
      search=search_extraction_1(textArea,search,caseSensitiveSearch,startLine,currentLine,canSearch,test,firstPos,found);
    }
 else {
      canSearch=true;
      search=search_extraction_2(textArea,search,caseSensitiveSearch,startLine,currentLine,canSearch,test,lastGoodLine);
    }
    highlight(textArea,search,caseSensitiveSearch);
  }
 catch (  Exception e) {
    BytecodeViewer.handleException(e);
  }
}

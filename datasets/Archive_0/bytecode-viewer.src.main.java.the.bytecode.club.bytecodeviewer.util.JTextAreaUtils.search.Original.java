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
      for (      String s : test) {
        if (!caseSensitiveSearch) {
          s=s.toLowerCase();
          search=search.toLowerCase();
        }
        if (currentLine == startLine) {
          canSearch=true;
        }
 else         if (s.contains(search)) {
          if (canSearch) {
            textArea.setCaretPosition(textArea.getDocument().getDefaultRootElement().getElement(currentLine - 1).getStartOffset());
            canSearch=false;
            found=true;
          }
          if (firstPos == -1)           firstPos=currentLine;
        }
        currentLine++;
      }
      if (!found && firstPos != -1) {
        textArea.setCaretPosition(textArea.getDocument().getDefaultRootElement().getElement(firstPos - 1).getStartOffset());
      }
    }
 else {
      canSearch=true;
      for (      String s : test) {
        if (!caseSensitiveSearch) {
          s=s.toLowerCase();
          search=search.toLowerCase();
        }
        if (s.contains(search)) {
          if (lastGoodLine != -1 && canSearch)           textArea.setCaretPosition(textArea.getDocument().getDefaultRootElement().getElement(lastGoodLine - 1).getStartOffset());
          lastGoodLine=currentLine;
          if (currentLine >= startLine)           canSearch=false;
        }
        currentLine++;
      }
      if (lastGoodLine != -1 && textArea.getDocument().getDefaultRootElement().getElementIndex(textArea.getCaretPosition()) + 1 == startLine) {
        textArea.setCaretPosition(textArea.getDocument().getDefaultRootElement().getElement(lastGoodLine - 1).getStartOffset());
      }
    }
    highlight(textArea,search,caseSensitiveSearch);
  }
 catch (  Exception e) {
    BytecodeViewer.handleException(e);
  }
}

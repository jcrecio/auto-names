/** 
 * Compile all of the compilable panes that're opened.
 * @param message if it should send a message saying it's compiled sucessfully.
 * @return true if no errors, false if it failed to compile.
 */
public static boolean compile(boolean message,boolean successAlert){
  BytecodeViewer.updateBusyStatus(true);
  boolean noErrors=true;
  boolean actuallyTried=false;
  for (  java.awt.Component c : BytecodeViewer.viewer.workPane.getLoadedViewers()) {
    if (c instanceof ClassViewer) {
      ClassViewer cv=(ClassViewer)c;
      if (noErrors && !cv.bytecodeViewPanel1.compile())       noErrors=false;
      if (noErrors && !cv.bytecodeViewPanel2.compile())       noErrors=false;
      if (noErrors && !cv.bytecodeViewPanel3.compile())       noErrors=false;
      actuallyTried=compile_extraction_2(actuallyTried,cv);
      if (cv.bytecodeViewPanel3.textArea != null && cv.bytecodeViewPanel3.textArea.isEditable())       actuallyTried=true;
    }
  }
  if (message) {
    if (actuallyTried) {
      if (noErrors && successAlert)       BytecodeViewer.showMessage("Compiled Successfully.");
    }
 else {
      BytecodeViewer.showMessage("You have no editable panes opened, make one editable and try again.");
    }
  }
  BytecodeViewer.updateBusyStatus(false);
  return true;
}

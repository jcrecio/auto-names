@Override public void dragEnter(final DropTargetDragEvent evt){
  log(out,"FileDrop: dragEnter event.");
  if (isDragOk(out,evt)) {
    if (c instanceof JComponent) {
      final JComponent jc=(JComponent)c;
      normalBorder=jc.getBorder();
      log(out,"FileDrop: normal border saved.");
      jc.setBorder(dragBorder);
      log(out,"FileDrop: drag border set.");
    }
    evt.acceptDrag(DnDConstants.ACTION_COPY);
    log(out,"FileDrop: event accepted.");
  }
 else {
    evt.rejectDrag();
    log(out,"FileDrop: event rejected.");
  }
}

@Override public void run(CommandLine commandLine) throws Exception {
  TypedProperties properties=TypedProperties.withProperty("direction",commandLine.getOptionValue("direction"));
  int[] directions=properties.getIntArray("direction",null);
  outer:   for (  String filename : commandLine.getArgs()) {
    List<String> lines=new ArrayList<String>();
    String entry=null;
    run_extraction_1(directions,filename,lines);
  }
}

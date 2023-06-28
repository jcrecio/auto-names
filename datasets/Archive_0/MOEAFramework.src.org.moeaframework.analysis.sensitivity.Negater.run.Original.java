@Override public void run(CommandLine commandLine) throws Exception {
  TypedProperties properties=TypedProperties.withProperty("direction",commandLine.getOptionValue("direction"));
  int[] directions=properties.getIntArray("direction",null);
  outer:   for (  String filename : commandLine.getArgs()) {
    List<String> lines=new ArrayList<String>();
    String entry=null;
    BufferedReader reader=null;
    PrintStream writer=null;
    try {
      reader=new BufferedReader(new FileReader(filename));
      while ((entry=reader.readLine()) != null) {
        lines.add(entry);
      }
    }
  finally {
      if (reader != null) {
        reader.close();
      }
    }
    for (    String line : lines) {
      try {
        if (!line.startsWith("#") && !line.startsWith("//")) {
          String[] tokens=line.split("\\s+");
          if (tokens.length != directions.length) {
            System.err.println("unable to negate values in " + filename + ", incorrect number of values in a row");
            continue outer;
          }
          for (int j=0; j < tokens.length; j++) {
            if (directions[j] != 0) {
              Double.parseDouble(tokens[j]);
            }
          }
        }
      }
 catch (      NumberFormatException e) {
        System.err.println("unable to negate values in " + filename + ", unable to parse number");
        continue outer;
      }
    }
    try {
      writer=new PrintStream(new File(filename));
      for (      String line : lines) {
        if (line.startsWith("#") || line.startsWith("//")) {
          writer.println(line);
        }
 else {
          String[] tokens=line.split("\\s+");
          for (int j=0; j < tokens.length; j++) {
            if (j > 0) {
              writer.print(' ');
            }
            if (directions[j] == 0) {
              writer.print(tokens[j]);
            }
 else {
              double value=Double.parseDouble(tokens[j]);
              writer.print(-value);
            }
          }
          writer.println();
        }
      }
    }
  finally {
      if (writer != null) {
        writer.close();
      }
    }
  }
}

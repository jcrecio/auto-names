protected DateTime parseZonedDateTime(String text,DateTimeFormatter formatter){
  if (formatter == null) {
    if (text.length() == 19) {
      char c4=text.charAt(4);
      char c7=text.charAt(7);
      char c10=text.charAt(10);
      char c13=text.charAt(13);
      char c16=text.charAt(16);
      if (c13 == ':' && c16 == ':') {
        if (c4 == '-' && c7 == '-') {
          if (c10 == 'T') {
            formatter=formatter_iso8601;
          }
 else           if (c10 == ' ') {
            formatter=defaultFormatter;
          }
        }
 else         if (c4 == '/' && c7 == '/') {
          formatter=formatter_dt19_tw;
        }
 else {
          char c0=text.charAt(0);
          char c1=text.charAt(1);
          char c2=text.charAt(2);
          char c3=text.charAt(3);
          char c5=text.charAt(5);
          if (c2 == '/' && c5 == '/') {
            int v0=(c0 - '0') * 10 + (c1 - '0');
            int v1=(c3 - '0') * 10 + (c4 - '0');
            if (v0 > 12) {
              formatter=formatter_dt19_eur;
            }
 else             if (v1 > 12) {
              formatter=formatter_dt19_us;
            }
 else {
              String country=Locale.getDefault().getCountry();
              if (country.equals("US")) {
                formatter=formatter_dt19_us;
              }
 else               if (country.equals("BR") || country.equals("AU")) {
                formatter=formatter_dt19_eur;
              }
            }
          }
 else           if (c2 == '.' && c5 == '.') {
            formatter=formatter_dt19_de;
          }
 else           if (c2 == '-' && c5 == '-') {
            formatter=formatter_dt19_in;
          }
        }
      }
    }
    if (text.length() >= 17) {
      char c4=text.charAt(4);
      if (c4 == '年') {
        if (text.charAt(text.length() - 1) == '秒') {
          formatter=formatter_dt19_cn_1;
        }
 else {
          formatter=formatter_dt19_cn;
        }
      }
 else       if (c4 == '년') {
        formatter=formatter_dt19_kr;
      }
    }
  }
  return formatter == null ? DateTime.parse(text) : DateTime.parse(text,formatter);
}

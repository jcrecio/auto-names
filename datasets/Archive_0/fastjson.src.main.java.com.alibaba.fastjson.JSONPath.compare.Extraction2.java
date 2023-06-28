static int compare(Object a,Object b){
  if (a.getClass() == b.getClass()) {
    return ((Comparable)a).compareTo(b);
  }
  Class typeA=a.getClass();
  Class typeB=b.getClass();
  if (typeA == BigDecimal.class) {
    if (typeB == Integer.class) {
      b=new BigDecimal((Integer)b);
    }
 else     if (typeB == Long.class) {
      b=new BigDecimal((Long)b);
    }
 else     if (typeB == Float.class) {
      b=new BigDecimal((Float)b);
    }
 else     if (typeB == Double.class) {
      b=new BigDecimal((Double)b);
    }
  }
 else   if (typeA == Long.class) {
    if (typeB == Integer.class) {
      b=new Long((Integer)b);
    }
 else     if (typeB == BigDecimal.class) {
      a=new BigDecimal((Long)a);
    }
 else     if (typeB == Float.class) {
      a=new Float((Long)a);
    }
 else     if (typeB == Double.class) {
      a=new Double((Long)a);
    }
  }
 else   if (typeA == Integer.class) {
    a=compare_extraction_2(a,typeB);
  }
 else   if (typeA == Double.class) {
    b=compare_extraction_3(b,typeB);
  }
 else   if (typeA == Float.class) {
    if (typeB == Integer.class) {
      b=new Float((Integer)b);
    }
 else     if (typeB == Long.class) {
      b=new Float((Long)b);
    }
 else     if (typeB == Double.class) {
      a=new Double((Float)a);
    }
  }
  return ((Comparable)a).compareTo(b);
}

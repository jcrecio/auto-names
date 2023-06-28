/** 
 * Symmetric tridiagonal QL algorithm, taken from JAMA package. This is derived from the Algol procedures tql2, by Bowdler, Martin, Reinsch, and Wilkinson, Handbook for Auto. Comp., Vol.ii-Linear Algebra, and the corresponding Fortran subroutine in EISPACK.
 */
public static void tql2(int n,double[] d,double[] e,double[][] V){
  for (int i=1; i < n; i++) {
    e[i - 1]=e[i];
  }
  e[n - 1]=0.0;
  double f=0.0;
  double tst1=0.0;
  double eps=Math.pow(2.0,-52.0);
  for (int l=0; l < n; l++) {
    tst1=Math.max(tst1,Math.abs(d[l]) + Math.abs(e[l]));
    int m=l;
    while (m < n) {
      if (Math.abs(e[m]) <= eps * tst1) {
        break;
      }
      m++;
    }
    if (m > l) {
      int iter=0;
      f=tql2_extraction_1(n,d,e,V,f,tst1,eps,l,m,iter);
    }
    d[l]=d[l] + f;
    e[l]=0.0;
  }
  for (int i=0; i < n - 1; i++) {
    int k=i;
    double p=d[i];
    for (int j=i + 1; j < n; j++) {
      if (d[j] < p) {
        k=j;
        p=d[j];
      }
    }
    tql2_extraction_2(n,d,V,i,k,p);
  }
}

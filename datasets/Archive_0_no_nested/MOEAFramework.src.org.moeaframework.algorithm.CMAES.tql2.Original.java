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
      do {
        iter=iter + 1;
        double g=d[l];
        double p=(d[l + 1] - g) / (2.0 * e[l]);
        double r=hypot(p,1.0);
        if (p < 0) {
          r=-r;
        }
        d[l]=e[l] / (p + r);
        d[l + 1]=e[l] * (p + r);
        double dl1=d[l + 1];
        double h=g - d[l];
        for (int i=l + 2; i < n; i++) {
          d[i]-=h;
        }
        f=f + h;
        p=d[m];
        double c=1.0;
        double c2=c;
        double c3=c;
        double el1=e[l + 1];
        double s=0.0;
        double s2=0.0;
        for (int i=m - 1; i >= l; i--) {
          c3=c2;
          c2=c;
          s2=s;
          g=c * e[i];
          h=c * p;
          r=hypot(p,e[i]);
          e[i + 1]=s * r;
          s=e[i] / r;
          c=p / r;
          p=c * d[i] - s * g;
          d[i + 1]=h + s * (c * g + s * d[i]);
          for (int k=0; k < n; k++) {
            h=V[k][i + 1];
            V[k][i + 1]=s * V[k][i] + c * h;
            V[k][i]=c * V[k][i] - s * h;
          }
        }
        p=-s * s2 * c3* el1* e[l] / dl1;
        e[l]=s * p;
        d[l]=c * p;
      }
 while (Math.abs(e[l]) > eps * tst1);
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
    if (k != i) {
      d[k]=d[i];
      d[i]=p;
      for (int j=0; j < n; j++) {
        p=V[j][i];
        V[j][i]=V[j][k];
        V[j][k]=p;
      }
    }
  }
}

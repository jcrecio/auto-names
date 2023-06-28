/** 
 * Symmetric Householder reduction to tridiagonal form, taken from JAMA package. This is derived from the Algol procedures tred2 by Bowdler, Martin, Reinsch, and Wilkinson, Handbook for Auto. Comp., Vol.ii-Linear Algebra, and the corresponding Fortran subroutine in EISPACK.
 */
public static void tred2(int n,double[][] V,double[] d,double[] e){
  for (int j=0; j < n; j++) {
    d[j]=V[n - 1][j];
  }
  for (int i=n - 1; i > 0; i--) {
    double scale=0.0;
    double h=0.0;
    for (int k=0; k < i; k++) {
      scale=scale + Math.abs(d[k]);
    }
    if (scale == 0.0) {
      e[i]=d[i - 1];
      for (int j=0; j < i; j++) {
        d[j]=V[i - 1][j];
        V[i][j]=0.0;
        V[j][i]=0.0;
      }
    }
 else {
      for (int k=0; k < i; k++) {
        d[k]/=scale;
        h+=d[k] * d[k];
      }
      double f=d[i - 1];
      double g=Math.sqrt(h);
      h=tred2_extraction_1(V,d,e,i,scale,h,f,g);
    }
    d[i]=h;
  }
  for (int i=0; i < n - 1; i++) {
    V[n - 1][i]=V[i][i];
    tred2_extraction_2(V,d,i);
  }
  for (int j=0; j < n; j++) {
    d[j]=V[n - 1][j];
    V[n - 1][j]=0.0;
  }
  V[n - 1][n - 1]=1.0;
  e[0]=0.0;
}

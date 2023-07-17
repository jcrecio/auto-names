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
      if (f > 0) {
        g=-g;
      }
      e[i]=scale * g;
      h=h - f * g;
      d[i - 1]=f - g;
      for (int j=0; j < i; j++) {
        e[j]=0.0;
      }
      for (int j=0; j < i; j++) {
        f=d[j];
        V[j][i]=f;
        g=e[j] + V[j][j] * f;
        for (int k=j + 1; k <= i - 1; k++) {
          g+=V[k][j] * d[k];
          e[k]+=V[k][j] * f;
        }
        e[j]=g;
      }
      f=0.0;
      for (int j=0; j < i; j++) {
        e[j]/=h;
        f+=e[j] * d[j];
      }
      double hh=f / (h + h);
      for (int j=0; j < i; j++) {
        e[j]-=hh * d[j];
      }
      for (int j=0; j < i; j++) {
        f=d[j];
        g=e[j];
        for (int k=j; k <= i - 1; k++) {
          V[k][j]-=(f * e[k] + g * d[k]);
        }
        d[j]=V[i - 1][j];
        V[i][j]=0.0;
      }
    }
    d[i]=h;
  }
  for (int i=0; i < n - 1; i++) {
    V[n - 1][i]=V[i][i];
    V[i][i]=1.0;
    double h=d[i + 1];
    if (h != 0.0) {
      for (int k=0; k <= i; k++) {
        d[k]=V[k][i + 1] / h;
      }
      for (int j=0; j <= i; j++) {
        double g=0.0;
        for (int k=0; k <= i; k++) {
          g+=V[k][i + 1] * V[k][j];
        }
        for (int k=0; k <= i; k++) {
          V[k][j]-=g * d[k];
        }
      }
    }
    for (int k=0; k <= i; k++) {
      V[k][i + 1]=0.0;
    }
  }
  for (int j=0; j < n; j++) {
    d[j]=V[n - 1][j];
    V[n - 1][j]=0.0;
  }
  V[n - 1][n - 1]=1.0;
  e[0]=0.0;
}
public static boolean if_infeasible(double[] x){
  boolean infeasible=false;
  for (int i=0; i < pindex9.length - 1; i++) {
    double[][] p=new double[pindex9[i + 1] - pindex9[i]][2];
    for (int j=pindex9[i]; j < pindex9[i + 1]; j++) {
      p[j - pindex9[i]]=oth_poly_points9[j];
    }
    infeasible=if_inside_polygon(x,p);
    if (infeasible) {
      break;
    }
  }
  if (infeasible) {
    for (int i=0; i < r_polyline9.length - 1; i++) {
      if (r_polyline9[i][0] == 1) {
        if (x[0] == r_polyline9[i][1] && x[1] >= rangey9[i][0] && x[1] <= rangey9[i][1]) {
          infeasible=false;
          break;
        }
      }
 else {
        if ((x[0] * r_polyline9[i][1] + r_polyline9[i][2] == x[1]) && x[1] >= rangey9[i][0] && x[1] <= rangey9[i][1] && x[0] >= rangex9[i][0] && x[0] <= rangex9[i][1]) {
          infeasible=false;
          break;
        }
      }
    }
    if (r_polyline9[M9 - 1][0] == 1) {
      if (x[0] == r_polyline9[M9 - 1][1] && x[1] >= rangey9[M9 - 1][0] && x[1] <= rangey9[M9 - 1][1]) {
        infeasible=false;
      }
    }
 else {
      if (x[0] * r_polyline9[M9 - 1][1] + r_polyline9[M9 - 1][2] == x[1] && x[1] >= rangey9[M9 - 1][0] && x[1] <= rangey9[M9 - 1][1] && x[0] >= rangex9[M9 - 1][0] && x[0] <= rangex9[M9 - 1][1]) {
        infeasible=false;
      }
    }
  }
  return infeasible;
}

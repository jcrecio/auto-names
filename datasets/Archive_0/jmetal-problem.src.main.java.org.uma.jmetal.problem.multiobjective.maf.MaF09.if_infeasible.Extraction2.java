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
    infeasible=if_infeasible_extraction_1(x,infeasible);
  }
  return infeasible;
}

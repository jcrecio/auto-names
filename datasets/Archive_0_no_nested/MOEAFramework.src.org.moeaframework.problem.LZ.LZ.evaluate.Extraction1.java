/** 
 * Evaluates the decision variables and returns the objectives.
 */
private double[] evaluate(double[] x_var){
  double[] y_obj=new double[numberOfObjectives];
  if (numberOfObjectives == 2) {
    if ((lType == 21) || (lType == 22) || (lType == 23)|| (lType == 24)|| (lType == 26)) {
      List<Double> aa=new ArrayList<Double>();
      List<Double> bb=new ArrayList<Double>();
      for (int n=1; n < numberOfVariables; n++) {
        if (n % 2 == 0) {
          aa.add(psfunc2(x_var[n],x_var[0],n,1));
        }
 else {
          bb.add(psfunc2(x_var[n],x_var[0],n,2));
        }
      }
      double g=betafunction(toArray(aa));
      double h=betafunction(toArray(bb));
      double[] alpha=alphafunction(x_var);
      y_obj[0]=alpha[0] + h;
      y_obj[1]=alpha[1] + g;
    }
 else     if (lType == 25) {
      List<Double> aa=new ArrayList<Double>();
      List<Double> bb=new ArrayList<Double>();
      for (int n=1; n < numberOfVariables; n++) {
        if (n % 3 == 0) {
          aa.add(psfunc2(x_var[n],x_var[0],n,1));
        }
 else         if (n % 3 == 1) {
          bb.add(psfunc2(x_var[n],x_var[0],n,2));
        }
 else {
          double c=psfunc2(x_var[n],x_var[0],n,3);
          if (n % 2 == 0) {
            aa.add(c);
          }
 else {
            bb.add(c);
          }
        }
      }
      double g=betafunction(toArray(aa));
      double h=betafunction(toArray(bb));
      double[] alpha=alphafunction(x_var);
      y_obj[0]=alpha[0] + h;
      y_obj[1]=alpha[1] + g;
    }
 else {
      throw new IllegalStateException();
    }
  }
 else   evaluate_extraction_2(x_var,y_obj);
  return y_obj;
}

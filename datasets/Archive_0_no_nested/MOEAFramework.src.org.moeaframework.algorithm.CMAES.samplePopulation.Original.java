/** 
 * Samples a new population.
 */
private void samplePopulation(){
  boolean feasible=true;
  int N=problem.getNumberOfVariables();
  if ((iteration - lastEigenupdate) > 1.0 / ccov / N/ 5.0) {
    eigendecomposition();
  }
  if (checkConsistency) {
    testAndCorrectNumerics();
  }
  population.clear();
  for (int i=0; i < lambda; i++) {
    Solution solution=problem.newSolution();
    if (diagonalIterations >= iteration) {
      do {
        feasible=true;
        for (int j=0; j < N; j++) {
          RealVariable variable=(RealVariable)solution.getVariable(j);
          double value=xmean[j] + sigma * diagD[j] * PRNG.nextGaussian();
          if (value < variable.getLowerBound() || value > variable.getUpperBound()) {
            feasible=false;
            break;
          }
          variable.setValue(value);
        }
      }
 while (!feasible);
    }
 else {
      double[] artmp=new double[N];
      do {
        feasible=true;
        for (int j=0; j < N; j++) {
          artmp[j]=diagD[j] * PRNG.nextGaussian();
        }
        for (int j=0; j < N; j++) {
          RealVariable variable=(RealVariable)solution.getVariable(j);
          double sum=0.0;
          for (int k=0; k < N; k++) {
            sum+=B[j][k] * artmp[k];
          }
          double value=xmean[j] + sigma * sum;
          if (value < variable.getLowerBound() || value > variable.getUpperBound()) {
            feasible=false;
            break;
          }
          variable.setValue(value);
        }
      }
 while (!feasible);
    }
    population.add(solution);
  }
  iteration++;
}

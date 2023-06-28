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
        feasible=samplePopulation_extraction_1(feasible,N,solution);
      }
 while (!feasible);
    }
 else {
      double[] artmp=new double[N];
      do {
        feasible=samplePopulation_extraction_2(N,solution,artmp);
      }
 while (!feasible);
    }
    population.add(solution);
  }
  iteration++;
}

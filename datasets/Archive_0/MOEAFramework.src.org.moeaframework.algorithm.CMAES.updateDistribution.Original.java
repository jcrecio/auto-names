/** 
 * Updates the internal parameters given the evaluated population.
 */
private void updateDistribution(){
  int N=problem.getNumberOfVariables();
  double[] xold=Arrays.copyOf(xmean,xmean.length);
  double[] BDz=new double[N];
  double[] artmp=new double[N];
  if (problem.getNumberOfObjectives() == 1) {
    population.sort(new SingleObjectiveComparator());
  }
 else {
    if (fitnessEvaluator == null) {
      population.sort(new NondominatedSortingComparator());
    }
 else {
      population.sort(new NondominatedFitnessComparator());
    }
  }
  for (int i=0; i < N; i++) {
    xmean[i]=0;
    for (int j=0; j < mu; j++) {
      xmean[i]+=weights[j] * EncodingUtils.getReal(population.get(j).getVariable(i));
    }
    BDz[i]=Math.sqrt(mueff) * (xmean[i] - xold[i]) / sigma;
  }
  if (diagonalIterations >= iteration) {
    for (int i=0; i < N; i++) {
      ps[i]=(1.0 - cs) * ps[i] + Math.sqrt(cs * (2.0 - cs)) * BDz[i] / diagD[i];
    }
  }
 else {
    for (int i=0; i < N; i++) {
      double sum=0.0;
      for (int j=0; j < N; j++) {
        sum+=B[j][i] * BDz[j];
      }
      artmp[i]=sum / diagD[i];
    }
    for (int i=0; i < N; i++) {
      double sum=0.0;
      for (int j=0; j < N; j++) {
        sum+=B[i][j] * artmp[j];
      }
      ps[i]=(1.0 - cs) * ps[i] + Math.sqrt(cs * (2.0 - cs)) * sum;
    }
  }
  double psxps=0;
  for (int i=0; i < N; i++) {
    psxps+=ps[i] * ps[i];
  }
  int hsig=0;
  if (Math.sqrt(psxps) / Math.sqrt(1.0 - Math.pow(1.0 - cs,2.0 * iteration)) / chiN < 1.4 + 2.0 / (N + 1)) {
    hsig=1;
  }
  for (int i=0; i < N; i++) {
    pc[i]=(1.0 - cc) * pc[i] + hsig * Math.sqrt(cc * (2.0 - cc)) * BDz[i];
  }
  for (int i=0; i < N; i++) {
    for (int j=(diagonalIterations >= iteration ? i : 0); j <= i; j++) {
      C[i][j]=(1.0 - (diagonalIterations >= iteration ? ccovsep : ccov)) * C[i][j] + ccov * (1.0 / mueff) * (pc[i] * pc[j] + (1 - hsig) * cc * (2.0 - cc)* C[i][j]);
      for (int k=0; k < mu; k++) {
        C[i][j]+=ccov * (1 - 1.0 / mueff) * weights[k]* (EncodingUtils.getReal(population.get(k).getVariable(i)) - xold[i])* (EncodingUtils.getReal(population.get(k).getVariable(j)) - xold[j]) / sigma / sigma;
      }
    }
  }
  sigma*=Math.exp(((Math.sqrt(psxps) / chiN) - 1) * cs / damps);
}

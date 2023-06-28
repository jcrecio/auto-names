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
  double psxps=updateDistribution_extraction_1(N,BDz,artmp);
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
    updateDistribution_extraction_2(xold,hsig,i);
  }
  sigma*=Math.exp(((Math.sqrt(psxps) / chiN) - 1) * cs / damps);
}

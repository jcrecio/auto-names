@Override public Ranking<S> computeRanking(List<S> population){
  int numberOfRanksForFeasibleSolutions, numberOfRanksForUnfeasibleSolutions, rank, indexOfBestSolution;
  int index, indexOfWeight;
  int numberOfWeights=this.utilityFunctionsNadir.getSize() + this.utilityFunctionsUtopia.getSize();
  int[] rankForUnfeasibleSolutions;
  double value, minimumValue;
  List<S> feasibleSolutions=new LinkedList<>();
  List<S> unfeasibleSolutions=new LinkedList<>();
  S solutionToInsert;
  for (  S solution : population) {
    if ((numberOfViolatedConstraints.getAttribute(solution) != null && numberOfViolatedConstraints.getAttribute(solution) > 0)) {
      unfeasibleSolutions.add(solution);
    }
 else {
      feasibleSolutions.add(solution);
    }
  }
  if (feasibleSolutions.size() > 0) {
    if (feasibleSolutions.size() > numberOfWeights) {
      numberOfRanksForFeasibleSolutions=(feasibleSolutions.size() + 1) / numberOfWeights;
    }
 else {
      numberOfRanksForFeasibleSolutions=1;
    }
  }
 else {
    numberOfRanksForFeasibleSolutions=0;
  }
  numberOfRanksForUnfeasibleSolutions=unfeasibleSolutions.size();
  this.numberOfRanks=numberOfRanksForFeasibleSolutions + numberOfRanksForUnfeasibleSolutions;
  this.rankedSubpopulations=new ArrayList<>(this.numberOfRanks);
  computeRanking_extraction_1(numberOfRanksForFeasibleSolutions,numberOfWeights,feasibleSolutions);
  if (!unfeasibleSolutions.isEmpty()) {
    rankForUnfeasibleSolutions=rankUnfeasibleSolutions(unfeasibleSolutions);
    for (index=0; index < rankForUnfeasibleSolutions.length; index++) {
      solutionToInsert=unfeasibleSolutions.get(index);
      rank=rankForUnfeasibleSolutions[index] + numberOfRanksForFeasibleSolutions;
      setAttribute(solutionToInsert,rank);
      this.rankedSubpopulations.get(rank).add(solutionToInsert);
    }
  }
  return this;
}

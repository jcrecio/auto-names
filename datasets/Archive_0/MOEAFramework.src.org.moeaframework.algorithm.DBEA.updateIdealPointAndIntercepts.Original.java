/** 
 * Updates the ideal point and intercepts given the new solution.
 * @param solution the new solution
 */
void updateIdealPointAndIntercepts(Solution solution){
  if (!solution.violatesConstraints()) {
    for (int j=0; j < problem.getNumberOfObjectives(); j++) {
      idealPoint[j]=Math.min(idealPoint[j],solution.getObjective(j));
      intercepts[j]=Math.max(intercepts[j],solution.getObjective(j));
    }
    Population feasibleSolutions=getFeasibleSolutions(population);
    feasibleSolutions.add(solution);
    Population nondominatedSolutions=getNondominatedFront(feasibleSolutions);
    if (!nondominatedSolutions.isEmpty()) {
      Population extremePoints=new Population();
      for (int i=0; i < problem.getNumberOfObjectives(); i++) {
        extremePoints.add(largestObjectiveValue(i,nondominatedSolutions));
      }
      if (numberOfUniqueSolutions(extremePoints) != problem.getNumberOfObjectives()) {
        for (int i=0; i < problem.getNumberOfObjectives(); i++) {
          intercepts[i]=extremePoints.get(i).getObjective(i);
        }
      }
 else {
        try {
          RealMatrix b=new Array2DRowRealMatrix(problem.getNumberOfObjectives(),1);
          RealMatrix A=new Array2DRowRealMatrix(problem.getNumberOfObjectives(),problem.getNumberOfObjectives());
          for (int i=0; i < problem.getNumberOfObjectives(); i++) {
            b.setEntry(i,0,1.0);
            for (int j=0; j < problem.getNumberOfObjectives(); j++) {
              A.setEntry(i,j,extremePoints.get(i).getObjective(j));
            }
          }
          double numerator=new LUDecomposition(A).getDeterminant();
          b.scalarMultiply(numerator);
          RealMatrix normal=MatrixUtils.inverse(A).multiply(b);
          for (int i=0; i < problem.getNumberOfObjectives(); i++) {
            intercepts[i]=numerator / normal.getEntry(i,0);
            if (intercepts[i] <= 0 || Double.isNaN(intercepts[i]) || Double.isInfinite(intercepts[i])) {
              intercepts[i]=extremePoints.get(i).getObjective(i);
            }
          }
        }
 catch (        RuntimeException e) {
          for (int i=0; i < problem.getNumberOfObjectives(); i++) {
            intercepts[i]=extremePoints.get(i).getObjective(i);
          }
        }
      }
    }
  }
}

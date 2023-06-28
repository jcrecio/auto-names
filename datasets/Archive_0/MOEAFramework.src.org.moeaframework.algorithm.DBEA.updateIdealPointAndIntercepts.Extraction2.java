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
      updateIdealPointAndIntercepts_extraction_1(nondominatedSolutions,extremePoints);
    }
  }
}

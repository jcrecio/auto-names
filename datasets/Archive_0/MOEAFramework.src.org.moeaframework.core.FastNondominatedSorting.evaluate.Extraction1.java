@Override public void evaluate(Population population){
  int N=population.size();
  int[][] dominanceChecks=new int[N][N];
  for (int i=0; i < N; i++) {
    Solution si=population.get(i);
    for (int j=i + 1; j < N; j++) {
      if (i != j) {
        Solution sj=population.get(j);
        dominanceChecks[i][j]=comparator.compare(si,sj);
        dominanceChecks[j][i]=-dominanceChecks[i][j];
      }
    }
  }
  int[] dominatedCounts=new int[N];
  List<List<Integer>> dominatesList=new ArrayList<List<Integer>>();
  List<Integer> currentFront=new ArrayList<Integer>();
  for (int i=0; i < N; i++) {
    List<Integer> dominates=new ArrayList<Integer>();
    int dominatedCount=0;
    for (int j=0; j < N; j++) {
      if (i != j) {
        if (dominanceChecks[i][j] < 0) {
          dominates.add(j);
        }
 else         if (dominanceChecks[j][i] < 0) {
          dominatedCount+=1;
        }
      }
    }
    if (dominatedCount == 0) {
      currentFront.add(i);
    }
    dominatesList.add(dominates);
    dominatedCounts[i]=dominatedCount;
  }
  int rank=0;
  while (!currentFront.isEmpty()) {
    List<Integer> nextFront=new ArrayList<Integer>();
    Population solutionsInFront=new Population();
    for (int i=0; i < currentFront.size(); i++) {
      Solution solution=population.get(currentFront.get(i));
      solution.setAttribute(RANK_ATTRIBUTE,rank);
      evaluate_extraction_2(dominatedCounts,dominatesList,currentFront,nextFront,i);
      solutionsInFront.add(solution);
    }
    updateCrowdingDistance(solutionsInFront);
    rank+=1;
    currentFront=nextFront;
  }
}

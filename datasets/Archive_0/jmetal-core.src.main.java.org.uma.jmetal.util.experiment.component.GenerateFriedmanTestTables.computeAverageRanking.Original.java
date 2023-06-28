private double[] computeAverageRanking(Vector<Vector<Double>> data){
  double[][] mean=new double[numberOfProblems][numberOfAlgorithms];
  for (int j=0; j < numberOfAlgorithms; j++) {
    for (int i=0; i < numberOfProblems; i++) {
      mean[i][j]=data.elementAt(j).elementAt(i);
    }
  }
  List<List<Pair<Integer,Double>>> order=new ArrayList<List<Pair<Integer,Double>>>(numberOfProblems);
  for (int i=0; i < numberOfProblems; i++) {
    order.add(new ArrayList<Pair<Integer,Double>>(numberOfAlgorithms));
    for (int j=0; j < numberOfAlgorithms; j++) {
      order.get(i).add(new ImmutablePair<Integer,Double>(j,mean[i][j]));
    }
    Collections.sort(order.get(i),new Comparator<Pair<Integer,Double>>(){
      @Override public int compare(      Pair<Integer,Double> pair1,      Pair<Integer,Double> pair2){
        if (Math.abs(pair1.getValue()) > Math.abs(pair2.getValue())) {
          return 1;
        }
 else         if (Math.abs(pair1.getValue()) < Math.abs(pair2.getValue())) {
          return -1;
        }
 else {
          return 0;
        }
      }
    }
);
  }
  List<List<MutablePair<Double,Double>>> rank=new ArrayList<List<MutablePair<Double,Double>>>(numberOfProblems);
  int position=0;
  for (int i=0; i < numberOfProblems; i++) {
    rank.add(new ArrayList<MutablePair<Double,Double>>(numberOfAlgorithms));
    for (int j=0; j < numberOfAlgorithms; j++) {
      boolean found=false;
      for (int k=0; k < numberOfAlgorithms && !found; k++) {
        if (order.get(i).get(k).getKey() == j) {
          found=true;
          position=k + 1;
        }
      }
      rank.get(i).add(new MutablePair<Double,Double>((double)position,order.get(i).get(position - 1).getValue()));
    }
  }
  for (int i=0; i < numberOfProblems; i++) {
    boolean[] hasBeenVisited=new boolean[numberOfAlgorithms];
    Vector<Integer> pendingToVisit=new Vector<Integer>();
    Arrays.fill(hasBeenVisited,false);
    for (int j=0; j < numberOfAlgorithms; j++) {
      pendingToVisit.removeAllElements();
      double sum=rank.get(i).get(j).getKey();
      hasBeenVisited[j]=true;
      int ig=1;
      for (int k=j + 1; k < numberOfAlgorithms; k++) {
        if (rank.get(i).get(j).getValue() == rank.get(i).get(k).getValue() && !hasBeenVisited[k]) {
          sum+=rank.get(i).get(k).getKey();
          ig++;
          pendingToVisit.add(k);
          hasBeenVisited[k]=true;
        }
      }
      sum/=(double)ig;
      rank.get(i).get(j).setLeft(sum);
      for (int k=0; k < pendingToVisit.size(); k++) {
        rank.get(i).get(pendingToVisit.elementAt(k)).setLeft(sum);
      }
    }
  }
  double[] averageRanking=new double[numberOfAlgorithms];
  for (int i=0; i < numberOfAlgorithms; i++) {
    averageRanking[i]=0;
    for (int j=0; j < numberOfProblems; j++) {
      averageRanking[i]+=rank.get(j).get(i).getKey() / ((double)numberOfProblems);
    }
  }
  return averageRanking;
}

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
  computeAverageRanking_extraction_1(order,rank,position);
  double[] averageRanking=new double[numberOfAlgorithms];
  for (int i=0; i < numberOfAlgorithms; i++) {
    averageRanking[i]=0;
    for (int j=0; j < numberOfProblems; j++) {
      averageRanking[i]+=rank.get(j).get(i).getKey() / ((double)numberOfProblems);
    }
  }
  return averageRanking;
}

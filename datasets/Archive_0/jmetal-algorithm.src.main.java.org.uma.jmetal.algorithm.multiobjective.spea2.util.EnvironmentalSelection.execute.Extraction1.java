@Override public List<S> execute(List<S> source2){
  int size;
  List<S> source=new ArrayList<>(source2.size());
  source.addAll(source2);
  if (source2.size() < this.solutionsToSelect) {
    size=source.size();
  }
 else {
    size=this.solutionsToSelect;
  }
  List<S> aux=new ArrayList<>(source.size());
  int i=0;
  while (i < source.size()) {
    double fitness=(double)this.strengthRawFitness.getAttribute(source.get(i));
    if (fitness < 1.0) {
      aux.add(source.get(i));
      source.remove(i);
    }
 else {
      i++;
    }
  }
  if (aux.size() < size) {
    StrengthFitnessComparator<S> comparator=new StrengthFitnessComparator<S>();
    Collections.sort(source,comparator);
    int remain=size - aux.size();
    for (i=0; i < remain; i++) {
      aux.add(source.get(i));
    }
    return aux;
  }
 else   if (aux.size() == size) {
    return aux;
  }
  double[][] distance=SolutionListUtils.distanceMatrix(aux);
  List<List<Pair<Integer,Double>>> distanceList=new ArrayList<>();
  LocationAttribute<S> location=new LocationAttribute<S>(aux);
  for (int pos=0; pos < aux.size(); pos++) {
    List<Pair<Integer,Double>> distanceNodeList=new ArrayList<>();
    for (int ref=0; ref < aux.size(); ref++) {
      if (pos != ref) {
        distanceNodeList.add(Pair.of(ref,distance[pos][ref]));
      }
    }
    distanceList.add(distanceNodeList);
  }
  for (int q=0; q < distanceList.size(); q++) {
    Collections.sort(distanceList.get(q),new Comparator<Pair<Integer,Double>>(){
      @Override public int compare(      Pair<Integer,Double> pair1,      Pair<Integer,Double> pair2){
        if (pair1.getRight() < pair2.getRight()) {
          return -1;
        }
 else         if (pair1.getRight() > pair2.getRight()) {
          return 1;
        }
 else {
          return 0;
        }
      }
    }
);
  }
  while (aux.size() > size) {
    double minDistance=Double.MAX_VALUE;
    int toRemove=0;
    i=0;
    Iterator<List<Pair<Integer,Double>>> iterator=distanceList.iterator();
    toRemove=execute_extraction_3(i,distanceList,minDistance,toRemove,iterator);
    int tmp=(int)location.getAttribute(aux.get(toRemove));
    aux.remove(toRemove);
    distanceList.remove(toRemove);
    Iterator<List<Pair<Integer,Double>>> externIterator=distanceList.iterator();
    while (externIterator.hasNext()) {
      Iterator<Pair<Integer,Double>> interIterator=externIterator.next().iterator();
      while (interIterator.hasNext()) {
        if (interIterator.next().getLeft() == tmp) {
          interIterator.remove();
          continue;
        }
      }
    }
  }
  return aux;
}

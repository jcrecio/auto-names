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
  execute_extraction_1(aux,distance,distanceList);
  execute_extraction_2(size,aux,distanceList,location);
  return aux;
}

/** 
 * Select two parents for reproduction
 */
public List<S> matingSelection(int cid,int type){
  int rnd1, rnd2;
  List<S> parents=new ArrayList<>(2);
  int nLength=neighborhood[cid].length;
  ArrayList<Integer> activeList=new ArrayList<>();
  if (type == 1) {
    for (int i=0; i < nLength; i++) {
      int idx=neighborhood[cid][i];
      for (int j=0; j < populationSize; j++) {
        if (subregionIdx[idx][j] == 1) {
          activeList.add(idx);
          break;
        }
      }
    }
    if (activeList.size() < 2) {
      activeList.clear();
      for (int i=0; i < populationSize; i++) {
        for (int j=0; j < populationSize; j++) {
          if (subregionIdx[i][j] == 1) {
            activeList.add(i);
            break;
          }
        }
      }
    }
    int activeSize=activeList.size();
    rnd1=randomGenerator.nextInt(0,activeSize - 1);
    do {
      rnd2=randomGenerator.nextInt(0,activeSize - 1);
    }
 while (rnd1 == rnd2);
    ArrayList<Integer> list1=new ArrayList<>();
    ArrayList<Integer> list2=new ArrayList<>();
    int id1=activeList.get(rnd1);
    int id2=activeList.get(rnd2);
    for (int i=0; i < populationSize; i++) {
      if (subregionIdx[id1][i] == 1) {
        list1.add(i);
      }
      if (subregionIdx[id2][i] == 1) {
        list2.add(i);
      }
    }
    int p1=randomGenerator.nextInt(0,list1.size() - 1);
    int p2=randomGenerator.nextInt(0,list2.size() - 1);
    parents.add(population.get(list1.get(p1)));
    parents.add(population.get(list2.get(p2)));
  }
 else {
    matingSelection_extraction_3(parents,activeList);
  }
  return parents;
}

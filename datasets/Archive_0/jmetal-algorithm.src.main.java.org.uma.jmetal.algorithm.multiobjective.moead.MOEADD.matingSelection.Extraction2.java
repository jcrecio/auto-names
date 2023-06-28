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
    matingSelection_extraction_2(parents,activeList,activeSize);
  }
 else {
    matingSelection_extraction_3(parents,activeList);
  }
  return parents;
}

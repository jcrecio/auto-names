/** 
 * delete a solution from the most crowded subregion (this function happens when: it should delete the solution in the 'parentLocation' subregion, but since this subregion only has one solution, it should be kept)
 */
public void deleteCrowdRegion2(S indiv,int location){
  double indivFitness=fitnessFunction(indiv,lambda[location]);
  ArrayList<Integer> crowdList=new ArrayList<>();
  int crowdIdx;
  int nicheCount=countOnes(0);
  if (location == 0) {
    nicheCount++;
  }
  crowdList.add(0);
  for (int i=1; i < populationSize; i++) {
    int curSize=countOnes(i);
    if (location == i) {
      curSize++;
    }
    if (curSize > nicheCount) {
      crowdList.clear();
      nicheCount=curSize;
      crowdList.add(i);
    }
 else     if (curSize == nicheCount) {
      crowdList.add(i);
    }
  }
  if (crowdList.size() == 1) {
    crowdIdx=crowdList.get(0);
  }
 else {
    int listLength=crowdList.size();
    crowdIdx=crowdList.get(0);
    double sumFitness=sumFitness(crowdIdx);
    if (crowdIdx == location) {
      sumFitness=sumFitness + indivFitness;
    }
    for (int i=1; i < listLength; i++) {
      int curIdx=crowdList.get(i);
      double curFitness=sumFitness(curIdx);
      if (curIdx == location) {
        curFitness=curFitness + indivFitness;
      }
      if (curFitness > sumFitness) {
        crowdIdx=curIdx;
        sumFitness=curFitness;
      }
    }
  }
  ArrayList<Integer> indList=new ArrayList<>();
  for (int i=0; i < populationSize; i++) {
    if (subregionIdx[crowdIdx][i] == 1) {
      indList.add(i);
    }
  }
  if (crowdIdx == location) {
    int temp=-1;
    indList.add(temp);
  }
  ArrayList<Integer> maxRankList=new ArrayList<>();
  int maxRank=(int)population.get(indList.get(0)).getAttribute(ranking.getAttributeIdentifier());
  int targetIdx=deleteCrowdRegion2_extraction_2(indiv,indivFitness,crowdIdx,indList,maxRankList,maxRank);
  if (targetIdx == -1) {
    nondominated_sorting_delete(indiv);
  }
 else {
    int indivRank=(int)indiv.getAttribute(ranking.getAttributeIdentifier());
    int targetRank=(int)population.get(targetIdx).getAttribute(ranking.getAttributeIdentifier());
    rankIdx[targetRank][targetIdx]=0;
    rankIdx[indivRank][targetIdx]=1;
    S targetSol=population.get(targetIdx);
    replace(targetIdx,indiv);
    subregionIdx[crowdIdx][targetIdx]=0;
    subregionIdx[location][targetIdx]=1;
    nondominated_sorting_delete(targetSol);
  }
}

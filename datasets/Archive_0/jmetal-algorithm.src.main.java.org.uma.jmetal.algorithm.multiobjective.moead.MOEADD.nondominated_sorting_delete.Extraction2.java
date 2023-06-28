/** 
 * update the non-domination level structure after deleting a solution
 */
public void nondominated_sorting_delete(S indiv){
  int indivRank=(int)indiv.getAttribute(ranking.getAttributeIdentifier());
  ArrayList<Integer> curLevel=new ArrayList<>();
  ArrayList<Integer> dominateList=new ArrayList<>();
  for (int i=0; i < populationSize; i++) {
    if (rankIdx[indivRank][i] == 1) {
      curLevel.add(i);
    }
  }
  int flag;
  int investigateRank=indivRank + 1;
  if (investigateRank < numRanks) {
    for (int i=0; i < populationSize; i++) {
      if (rankIdx[investigateRank][i] == 1) {
        flag=0;
        if (checkDominance(indiv,population.get(i)) == 1) {
          for (int j=0; j < curLevel.size(); j++) {
            if (checkDominance(population.get(i),population.get(curLevel.get(j))) == -1) {
              flag=1;
              break;
            }
          }
          if (flag == 0) {
            dominateList.add(i);
            rankIdx[investigateRank][i]=0;
            rankIdx[investigateRank - 1][i]=1;
            population.get(i).setAttribute(ranking.getAttributeIdentifier(),investigateRank - 1);
          }
        }
      }
    }
  }
  int curIdx;
  int curListSize=dominateList.size();
  while (curListSize != 0) {
    curLevel.clear();
    for (int i=0; i < populationSize; i++) {
      if (rankIdx[investigateRank][i] == 1) {
        curLevel.add(i);
      }
    }
    investigateRank=investigateRank + 1;
    nondominated_sorting_delete_extraction_2(curLevel,dominateList,investigateRank,curListSize);
    curListSize=dominateList.size();
  }
}

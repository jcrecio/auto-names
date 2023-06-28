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
    if (investigateRank < numRanks) {
      for (int i=0; i < curListSize; i++) {
        curIdx=dominateList.get(i);
        for (int j=0; j < populationSize; j++) {
          if (j == populationSize) {
            System.err.println("There are problems");
          }
          if (rankIdx[investigateRank][j] == 1) {
            flag=0;
            if (checkDominance(population.get(curIdx),population.get(j)) == 1) {
              for (int k=0; k < curLevel.size(); k++) {
                if (checkDominance(population.get(j),population.get(curLevel.get(k))) == -1) {
                  flag=1;
                  break;
                }
              }
              if (flag == 0) {
                dominateList.add(j);
                rankIdx[investigateRank][j]=0;
                rankIdx[investigateRank - 1][j]=1;
                population.get(j).setAttribute(ranking.getAttributeIdentifier(),investigateRank - 1);
              }
            }
          }
        }
      }
    }
    for (int i=0; i < curListSize; i++) {
      dominateList.remove(0);
    }
    curListSize=dominateList.size();
  }
}

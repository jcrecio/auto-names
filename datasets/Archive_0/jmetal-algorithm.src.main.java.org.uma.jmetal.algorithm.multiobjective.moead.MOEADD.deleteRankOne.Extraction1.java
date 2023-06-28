/** 
 * if there is only one non-domination level (i.e., all solutions are non-dominated with each other), we should delete a solution from the most crowded subregion
 */
public void deleteRankOne(S indiv,int location){
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
switch (nicheCount) {
case 0:    System.out.println("Empty subregion!!!");
  break;
case 1:int targetIdx;
for (targetIdx=0; targetIdx < populationSize; targetIdx++) {
if (subregionIdx[location][targetIdx] == 1) {
  break;
}
}
double prev_func=fitnessFunction(population.get(targetIdx),lambda[location]);
if (indivFitness < prev_func) {
replace(targetIdx,indiv);
}
break;
default:deleteRankOne_extraction_2(indiv,location,indivFitness,crowdIdx,nicheCount);
break;
}
}

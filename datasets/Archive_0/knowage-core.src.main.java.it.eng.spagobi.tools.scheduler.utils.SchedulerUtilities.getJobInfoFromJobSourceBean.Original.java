/** 
 * Gets the job info from job source bean.
 * @param jobDetSB the job det sb
 * @return the job info from job source bean
 */
public static JobInfo getJobInfoFromJobSourceBean(SourceBean jobDetSB){
  JobInfo jobInfo=new JobInfo();
  try {
    List biobjects=new ArrayList();
    String jobNameRecovered=(String)jobDetSB.getAttribute("jobName");
    String jobDescriptionRecovered=(String)jobDetSB.getAttribute("jobDescription");
    String jobGroupNameRecovered=(String)jobDetSB.getAttribute("jobGroupName");
    jobInfo.setJobName(jobNameRecovered);
    jobInfo.setJobDescription(jobDescriptionRecovered);
    jobInfo.setJobGroupName(jobGroupNameRecovered);
    SourceBean jobParSB=(SourceBean)jobDetSB.getAttribute("JOB_PARAMETERS");
    if (jobParSB != null) {
      IBIObjectDAO biobjdao=DAOFactory.getBIObjectDAO();
      IBIObjectParameterDAO biobjpardao=DAOFactory.getBIObjectParameterDAO();
      SourceBean docLblSB=(SourceBean)jobParSB.getFilteredSourceBeanAttribute("JOB_PARAMETER","name","documentLabels");
      String docLblStr=(String)docLblSB.getAttribute("value");
      String[] docLbls=docLblStr.split(",");
      for (int i=0; i < docLbls.length; i++) {
        BIObject biobj=biobjdao.loadBIObjectByLabel(docLbls[i].substring(0,docLbls[i].lastIndexOf("__")));
        List biobjpars=biobjpardao.loadBIObjectParametersById(biobj.getId());
        biobj.setDrivers(biobjpars);
        String biobjlbl=biobj.getLabel() + "__" + (i + 1);
        SourceBean queryStringSB=(SourceBean)jobParSB.getFilteredSourceBeanAttribute("JOB_PARAMETER","name",biobjlbl);
        SourceBean iterativeSB=(SourceBean)jobParSB.getFilteredSourceBeanAttribute("JOB_PARAMETER","name",biobjlbl + "_iterative");
        List iterativeParameters=new ArrayList();
        if (iterativeSB != null) {
          String iterativeParametersStr=(String)iterativeSB.getAttribute("value");
          String[] iterativeParametersArray=iterativeParametersStr.split(";");
          iterativeParameters.addAll(Arrays.asList(iterativeParametersArray));
        }
        SourceBean loadAtRuntimeSB=(SourceBean)jobParSB.getFilteredSourceBeanAttribute("JOB_PARAMETER","name",biobjlbl + "_loadAtRuntime");
        Map<String,String> loadAtRuntimeParameters=new HashMap<String,String>();
        if (loadAtRuntimeSB != null) {
          String loadAtRuntimeStr=(String)loadAtRuntimeSB.getAttribute("value");
          String[] loadAtRuntimeArray=loadAtRuntimeStr.split(";");
          for (int count=0; count < loadAtRuntimeArray.length; count++) {
            String loadAtRuntime=loadAtRuntimeArray[count];
            int parameterUrlNameIndex=loadAtRuntime.lastIndexOf("(");
            String parameterUrlName=loadAtRuntime.substring(0,parameterUrlNameIndex);
            String userAndRole=loadAtRuntime.substring(parameterUrlNameIndex + 1,loadAtRuntime.length() - 1);
            loadAtRuntimeParameters.put(parameterUrlName,userAndRole);
          }
        }
        SourceBean useFormulaSB=(SourceBean)jobParSB.getFilteredSourceBeanAttribute("JOB_PARAMETER","name",biobjlbl + "_useFormula");
        Map<String,String> useFormulaParameters=new HashMap<String,String>();
        if (useFormulaSB != null) {
          String useFormulaStr=(String)useFormulaSB.getAttribute("value");
          String[] useFormulaArray=useFormulaStr.split(";");
          for (int count=0; count < useFormulaArray.length; count++) {
            String useFormula=useFormulaArray[count];
            int parameterUrlNameIndex=useFormula.lastIndexOf("(");
            String parameterUrlName=useFormula.substring(0,parameterUrlNameIndex);
            String fName=useFormula.substring(parameterUrlNameIndex + 1,useFormula.length() - 1);
            useFormulaParameters.put(parameterUrlName,fName);
          }
        }
        String queryString=(String)queryStringSB.getAttribute("value");
        String[] parCouples=queryString.split("%26");
        Iterator iterbiobjpar=biobjpars.iterator();
        while (iterbiobjpar.hasNext()) {
          BIObjectParameter biobjpar=(BIObjectParameter)iterbiobjpar.next();
          if (iterativeParameters.contains(biobjpar.getParameterUrlName())) {
            biobjpar.setIterative(true);
          }
 else {
            biobjpar.setIterative(false);
          }
          if (loadAtRuntimeParameters.containsKey(biobjpar.getParameterUrlName())) {
            RuntimeLoadingParameterValuesRetriever strategy=new RuntimeLoadingParameterValuesRetriever();
            String serialiedUserAndRole=loadAtRuntimeParameters.get(biobjpar.getParameterUrlName());
            String[] splitted=serialiedUserAndRole.split("\\|");
            String serializedUser=splitted[0];
            UserProfile profile=SchedulerUtilitiesV2.deserializeUserProfile(serializedUser);
            strategy.setUserProfile(profile);
            strategy.setRoleToBeUsed(splitted[1]);
            biobjpar.setParameterValuesRetriever(strategy);
          }
 else           if (useFormulaParameters.containsKey(biobjpar.getParameterUrlName())) {
            FormulaParameterValuesRetriever strategy=new FormulaParameterValuesRetriever();
            String fName=useFormulaParameters.get(biobjpar.getParameterUrlName());
            Formula f=Formula.getFormula(fName);
            strategy.setFormula(f);
            biobjpar.setParameterValuesRetriever(strategy);
          }
 else {
            for (int j=0; j < parCouples.length; j++) {
              String parCouple=parCouples[j];
              String[] parDef=parCouple.split("=");
              if (biobjpar.getParameterUrlName().equals(parDef[0])) {
                String parameterValues=(parDef.length == 2 ? parDef[1] : "");
                String[] valuesArr=parameterValues.split(";");
                List values=Arrays.asList(valuesArr);
                biobjpar.setParameterValues(values);
                break;
              }
            }
          }
        }
        biobjects.add(biobj);
      }
      jobInfo.setDocuments(biobjects);
    }
  }
 catch (  Exception e) {
    SpagoBITracer.major(SpagoBIConstants.NAME_MODULE,SchedulerUtilities.class.getName(),"getJobInfoFromJobSourceBean","Error while extracting job info from xml",e);
  }
  return jobInfo;
}

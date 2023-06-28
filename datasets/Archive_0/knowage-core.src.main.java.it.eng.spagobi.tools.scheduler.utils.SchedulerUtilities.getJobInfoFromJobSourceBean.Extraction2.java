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
      getJobInfoFromJobSourceBean_extraction_1(jobInfo,biobjects,jobParSB,biobjdao,biobjpardao,docLbls);
    }
  }
 catch (  Exception e) {
    SpagoBITracer.major(SpagoBIConstants.NAME_MODULE,SchedulerUtilities.class.getName(),"getJobInfoFromJobSourceBean","Error while extracting job info from xml",e);
  }
  return jobInfo;
}

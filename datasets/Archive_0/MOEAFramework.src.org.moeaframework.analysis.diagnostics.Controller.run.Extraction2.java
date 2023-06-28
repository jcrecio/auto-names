/** 
 * Launches a thread to run the current evaluation job.
 */
public void run(){
  if (thread != null) {
    System.err.println("job already running");
    return;
  }
  final String problemName=frame.getProblem();
  final String algorithmName=frame.getAlgorithm();
  final int numberOfEvaluations=frame.getNumberOfEvaluations();
  final int numberOfSeeds=frame.getNumberOfSeeds();
  thread=new Thread(){
    public void run(){
      try {
        updateProgress(0,0,numberOfEvaluations,numberOfSeeds);
        Instrumenter instrumenter=new Instrumenter().withFrequency(100).withProblem(problemName);
        if (getIncludeHypervolume()) {
          instrumenter.attachHypervolumeCollector();
        }
        if (getIncludeGenerationalDistance()) {
          instrumenter.attachGenerationalDistanceCollector();
        }
        if (getIncludeInvertedGenerationalDistance()) {
          instrumenter.attachInvertedGenerationalDistanceCollector();
        }
        if (getIncludeSpacing()) {
          instrumenter.attachSpacingCollector();
        }
        if (getIncludeAdditiveEpsilonIndicator()) {
          instrumenter.attachAdditiveEpsilonIndicatorCollector();
        }
        run_extraction_1(instrumenter);
        run_extraction_2(problemName,algorithmName,numberOfEvaluations,numberOfSeeds,instrumenter);
      }
 catch (      Exception e) {
        handleException(e);
      }
 finally {
        thread=null;
        fireStateChangedEvent();
      }
    }
    private void run_extraction_1(    Instrumenter instrumenter){
      if (getIncludeContribution()) {
        instrumenter.attachContributionCollector();
      }
      if (getIncludeR1()) {
        instrumenter.attachR1Collector();
      }
      if (getIncludeR2()) {
        instrumenter.attachR2Collector();
      }
      if (getIncludeR3()) {
        instrumenter.attachR3Collector();
      }
      if (getIncludeEpsilonProgress()) {
        instrumenter.attachEpsilonProgressCollector();
      }
      if (getIncludeAdaptiveMultimethodVariation()) {
        instrumenter.attachAdaptiveMultimethodVariationCollector();
      }
    }
    private void run_extraction_2(    final String problemName,    final String algorithmName,    final int numberOfEvaluations,    final int numberOfSeeds,    Instrumenter instrumenter){
      if (getIncludeAdaptiveTimeContinuation()) {
        instrumenter.attachAdaptiveTimeContinuationCollector();
      }
      if (getIncludeElapsedTime()) {
        instrumenter.attachElapsedTimeCollector();
      }
      if (getIncludeApproximationSet()) {
        instrumenter.attachApproximationSetCollector();
      }
      if (getIncludePopulationSize()) {
        instrumenter.attachPopulationSizeCollector();
      }
      Problem problem=null;
      try {
        problem=ProblemFactory.getInstance().getProblem(problemName);
        instrumenter.withEpsilon(EpsilonHelper.getEpsilon(problem));
      }
  finally {
        if (problem != null) {
          problem.close();
        }
      }
      ProgressListener listener=new ProgressListener(){
        @Override public void progressUpdate(        ProgressEvent event){
          updateProgress(event.getCurrentNFE(),event.getCurrentSeed(),event.getMaxNFE(),event.getTotalSeeds());
          if (event.isSeedFinished()) {
            Executor executor=event.getExecutor();
            Instrumenter instrumenter=executor.getInstrumenter();
            add(algorithmName,problemName,instrumenter.getLastAccumulator());
          }
        }
      }
;
      executor=new Executor().withSameProblemAs(instrumenter).withInstrumenter(instrumenter).withAlgorithm(algorithmName).withMaxEvaluations(numberOfEvaluations).withProgressListener(listener);
      executor.runSeeds(numberOfSeeds);
    }
  }
;
  thread.setDaemon(true);
  thread.start();
  fireStateChangedEvent();
}

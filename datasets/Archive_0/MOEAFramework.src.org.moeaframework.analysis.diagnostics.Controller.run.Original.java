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
      @Override public void progressUpdate(      ProgressEvent event){
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
 catch (  Exception e) {
    handleException(e);
  }
 finally {
    thread=null;
    fireStateChangedEvent();
  }
}

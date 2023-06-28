private T runWithRetries(final int slot){
  Instant deadline=Instant.now().plus(maxTotalRetriesDuration);
  JedisRedirectionException redirect=null;
  int consecutiveConnectionFailures=0;
  Exception lastException=null;
  for (int attemptsLeft=this.maxAttempts; attemptsLeft > 0; attemptsLeft--) {
    Jedis connection=null;
    try {
      connection=runWithRetries_extraction_1(slot,redirect);
      return execute(connection);
    }
 catch (    JedisConnectionException jce) {
      lastException=jce;
      ++consecutiveConnectionFailures;
      LOG.debug("Failed connecting to Redis: {}",connection,jce);
      boolean reset=handleConnectionProblem(attemptsLeft - 1,consecutiveConnectionFailures,deadline);
      runWithRetries_extraction_2(reset);
    }
catch (    JedisRedirectionException jre) {
      if (lastException == null || lastException instanceof JedisRedirectionException) {
        lastException=jre;
      }
      LOG.debug("Redirected by server to {}",jre.getTargetNode());
      consecutiveConnectionFailures=0;
      redirect=jre;
      if (jre instanceof JedisMovedDataException) {
        this.connectionHandler.renewSlotCache(connection);
      }
    }
 finally {
      releaseConnection(connection);
    }
    if (Instant.now().isAfter(deadline)) {
      throw new JedisClusterOperationException("Cluster retry deadline exceeded.");
    }
  }
  JedisClusterMaxAttemptsException maxAttemptsException=new JedisClusterMaxAttemptsException("No more cluster attempts left.");
  maxAttemptsException.addSuppressed(lastException);
  throw maxAttemptsException;
}

private T runWithRetries(final int slot){
  Instant deadline=Instant.now().plus(maxTotalRetriesDuration);
  JedisRedirectionException redirect=null;
  int consecutiveConnectionFailures=0;
  Exception lastException=null;
  for (int attemptsLeft=this.maxAttempts; attemptsLeft > 0; attemptsLeft--) {
    Jedis connection=null;
    try {
      if (redirect != null) {
        connection=connectionHandler.getConnectionFromNode(redirect.getTargetNode());
        if (redirect instanceof JedisAskDataException) {
          connection.asking();
        }
      }
 else {
        connection=connectionHandler.getConnectionFromSlot(slot);
      }
      return execute(connection);
    }
 catch (    JedisConnectionException jce) {
      lastException=jce;
      ++consecutiveConnectionFailures;
      LOG.debug("Failed connecting to Redis: {}",connection,jce);
      boolean reset=handleConnectionProblem(attemptsLeft - 1,consecutiveConnectionFailures,deadline);
      if (reset) {
        consecutiveConnectionFailures=0;
        redirect=null;
      }
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

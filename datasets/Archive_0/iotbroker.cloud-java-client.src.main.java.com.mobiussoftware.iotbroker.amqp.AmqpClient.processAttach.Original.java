public void processAttach(String name,RoleCode role,Long handle){
  if (role != null) {
    if (role == RoleCode.RECEIVER) {
      Long realHandle=usedOutgoingMappings.get(name);
      if (realHandle != null) {
        for (int i=0; i < pendingMessages.size(); i++) {
          AMQPTransfer currMessage=pendingMessages.get(i);
          if (currMessage.getHandle().equals(realHandle)) {
            pendingMessages.remove(i);
            i--;
            timers.store(currMessage);
            if (currMessage.getSettled())             timers.remove(currMessage.getDeliveryId().intValue());
            client.send(currMessage);
          }
        }
      }
    }
 else {
      usedIncomingMappings.put(name,handle);
      usedIncomingHandles.put(handle,name);
      Long currHandle=pendingSubscribes.remove(name);
      if (currHandle != null) {
        byte qos=(byte)QoS.AT_LEAST_ONCE.getValue();
        try {
          DBTopic topic=dbInterface.getTopicByName(name,account);
          if (topic != null) {
            topic.setQos(qos);
            dbInterface.updateTopic(topic);
          }
 else {
            topic=new DBTopic(account,name,qos);
            dbInterface.createTopic(topic);
          }
          if (topicListener != null)           topicListener.finishAddingTopic(topic.getName(),topic.getQos());
        }
 catch (        Exception ex) {
          logger.error("An error occured while saving topic," + ex.getMessage(),ex);
        }
      }
    }
  }
}

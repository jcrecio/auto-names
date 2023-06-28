public void processAttach(String name,RoleCode role,Long handle){
  if (role != null) {
    if (role == RoleCode.RECEIVER) {
      processAttach_extraction_2(name);
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

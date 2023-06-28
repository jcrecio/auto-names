public void packetReceived(CoapMessage message){
  CoapType type=message.getType();
  if ((message.getCode() == CoapCode.POST || message.getCode() == CoapCode.PUT) && type != CoapType.ACKNOWLEDGEMENT) {
    String topic=message.options().fetchUriPath();
    byte qos=message.options().fetchAccept().byteValue();
    byte[] content=message.getPayload();
    if (topic == null) {
      byte[] textBytes="text/plain".getBytes();
      byte[] nodeIdBytes=account.getClientId().getBytes();
      CoapMessage ack=CoapMessage.builder().version(VERSION).type(CoapType.ACKNOWLEDGEMENT).code(CoapCode.BAD_OPTION).messageID(message.getMessageID()).token(message.getToken()).payload(new byte[0]).option(new CoapOption(CoapOptionType.CONTENT_FORMAT.getValue(),textBytes.length,textBytes)).option(new CoapOption(CoapOptionType.NODE_ID.getValue(),nodeIdBytes.length,nodeIdBytes)).build();
      client.send(ack);
      return;
    }
    Message dbMessage=new Message(account,topic,new String(content),true,qos,false,false);
    try {
      dbInterface.saveMessage(dbMessage);
    }
 catch (    Exception ex) {
      logger.error("An error occured while deleting topic," + ex.getMessage(),ex);
    }
    if (listener != null)     listener.messageReceived(dbMessage);
  }
switch (type) {
case CONFIRMABLE:    byte[] nodeIdBytes=account.getClientId().getBytes();
  CoapMessage response=CoapMessage.builder().version(message.getVersion()).type(CoapType.ACKNOWLEDGEMENT).code(message.getCode()).messageID(message.getMessageID()).token(message.getToken()).payload(new byte[0]).option(new CoapOption(CoapOptionType.NODE_ID.getValue(),nodeIdBytes.length,nodeIdBytes)).build();
client.send(response);
break;
case NON_CONFIRMABLE:timers.remove(message.getToken());
break;
case ACKNOWLEDGEMENT:if (message.getCode() == CoapCode.GET) {
packetReceived_extraction_1(message);
}
 else if (message.getCode() == CoapCode.PUT && message.getMessageID() == 0) {
packetReceived_extraction_3();
}
 else if (message.getToken() != null) timers.remove(message.getToken());
break;
case RESET:timers.remove(message.getToken());
break;
}
}

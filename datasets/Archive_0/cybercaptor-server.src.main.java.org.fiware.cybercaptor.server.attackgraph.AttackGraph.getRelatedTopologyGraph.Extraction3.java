/** 
 * @param informationSystem the information system
 * @return The topology Graph associated to this attack path
 * @throws Exception
 */
public InformationSystemGraph getRelatedTopologyGraph(InformationSystem informationSystem) throws Exception {
  InformationSystemGraph result=new InformationSystemGraph();
  List<Vertex> vertices=new ArrayList<Vertex>(this.vertices.values());
  for (  Vertex vertex : vertices) {
    if (vertex.fact.type == FactType.DATALOG_FACT && vertex.fact.datalogCommand != null) {
      DatalogCommand command=vertex.fact.datalogCommand;
switch (command.command) {
case "hacl":        InformationSystemGraphVertex from=null;
      InformationSystemGraphVertex to=null;
    String relatedVulneravility=null;
  getRelatedTopologyGraph_extraction_1(informationSystem,result,vertex,command,from,to,relatedVulneravility);
break;
case "attackerLocated":InformationSystemGraphVertex attackerVertex=null;
attackerVertex=getRelatedTopologyGraph_extraction_3(informationSystem,result,command,attackerVertex);
if (attackerVertex != null) attackerVertex.setMachineOfAttacker(true);
break;
case "vulExists":InformationSystemHost machine=informationSystem.getHostByNameOrIPAddress(command.params[0]);
if (machine != null) {
result.getMachineVertex(machine).setCompromised(true);
}
break;
}
}
}
return result;
}

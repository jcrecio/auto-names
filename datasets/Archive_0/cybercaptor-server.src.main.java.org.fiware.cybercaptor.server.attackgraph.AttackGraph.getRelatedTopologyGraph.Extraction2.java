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
  if (command.params[0].equals("internet") || command.params[0].equals("1.1.1.1") || command.params[0].equals("internet_host")) {
    from=result.getMachineVertex(informationSystem.getHostByNameOrIPAddress("internet_host"));
  }
 else {
    InformationSystemHost machine=informationSystem.getHostByNameOrIPAddress(command.params[0]);
    if (machine != null) {
      from=result.getMachineVertex(machine);
    }
  }
if (command.params[1].equals("internet") || command.params[1].equals("1.1.1.1")) to=result.getMachineVertex(informationSystem.getHostByNameOrIPAddress("1.1.1.1"));
 else {
  InformationSystemHost machine=informationSystem.getHostByNameOrIPAddress(command.params[1]);
  if (machine != null) {
    to=result.getMachineVertex(machine);
  }
}
if (from != null && to != null) {
InformationSystemGraphArc arc=getRelatedTopologyGraph_extraction_2(vertex,from,to,relatedVulneravility);
if (!result.getArcs().contains(arc)) result.getArcs().add(arc);
}
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

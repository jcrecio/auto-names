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
InformationSystemGraphArc arc=new InformationSystemGraphArc();
arc.setSource(from);
arc.setDestination(to);
vertex.computeParentsAndChildren(this);
Vertex directAccessChild=vertex.childOfType(true,"direct network access");
if (directAccessChild == null) {
  directAccessChild=vertex.childOfType(true,"multi-hop access");
}
if (directAccessChild != null) {
  directAccessChild.computeParentsAndChildren(this);
  Vertex netAccessChild=directAccessChild.childOfType(false,"netAccess");
  if (netAccessChild != null) {
    netAccessChild.computeParentsAndChildren(this);
    Vertex remoteExploitChild=netAccessChild.childOfType(true,"remote exploit of a server program");
    if (remoteExploitChild != null) {
      remoteExploitChild.computeParentsAndChildren(this);
      Vertex vulnExistParent=remoteExploitChild.parentOfType(false,"vulExists");
      if (vulnExistParent != null && vulnExistParent.fact.datalogCommand.params.length > 2) {
        relatedVulneravility=vulnExistParent.fact.datalogCommand.params[1];
      }
    }
  }
}
if (relatedVulneravility != null) arc.setRelatedVulnerability(relatedVulneravility);
if (!result.getArcs().contains(arc)) result.getArcs().add(arc);
}
break;
case "attackerLocated":InformationSystemGraphVertex attackerVertex=null;
if (command.params[0].equals("internet") || command.params[0].equals("1.1.1.1")) attackerVertex=result.getMachineVertex(informationSystem.getHostByNameOrIPAddress("1.1.1.1"));
 else {
InformationSystemHost machine=informationSystem.getHostByNameOrIPAddress(command.params[0]);
if (machine != null) {
attackerVertex=result.getMachineVertex(machine);
}
}
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

/** 
 * Explore the attack path from node V
 * @param V          the starting node
 * @param Forbidden  the list of forbidden vertices
 * @param graph      the attack graph
 * @param AttackPath the attack path to build
 * @return the attack path
 * @deprecated use {@link org.fiware.cybercaptor.server.scoring.math.AttackPaths#exploreAttackPath2(org.fiware.cybercaptor.server.scoring.types.Vertex,org.fiware.cybercaptor.server.scoring.types.Vertex[],org.fiware.cybercaptor.server.scoring.types.Graph)}
 */
@Deprecated public static Graph exploreAttackPath(Vertex V,Vertex[] Forbidden,Graph graph,Graph AttackPath){
  Vertex[] vertices=new Vertex[graph.getVertices().length];
  Arc[] arcs=new Arc[graph.getArcs().length];
  Vertex LEAFVertex=new Vertex(0.0,"",0.0,"LEAF");
  Vertex ORVertex=new Vertex(0.0,"",0.0,"OR");
  Vertex ANDVertex=new Vertex(0.0,"",0.0,"AND");
  for (int m=0; m < vertices.length; m++) {
    vertices[m]=new Vertex(graph.getVertices()[m]);
  }
  for (int m=0; m < arcs.length; m++) {
    arcs[m]=new Arc(graph.getArcs()[m].getSource(),graph.getArcs()[m].getDestination());
  }
  Vertex[] V_Predecessors=Graph.getPredecessors(arcs,vertices,V.getID());
  if (V.getType().equals("OR") && Forbidden == null) {
    Vertex ForbiddenVertex=new Vertex(V);
    Forbidden=new Vertex[1];
    Forbidden[0]=ForbiddenVertex;
  }
  if (V_Predecessors != null) {
    AttackPath=exploreAttackPath_extraction_1(V,Forbidden,graph,AttackPath,LEAFVertex,ORVertex,ANDVertex,V_Predecessors);
  }
  return AttackPath;
}

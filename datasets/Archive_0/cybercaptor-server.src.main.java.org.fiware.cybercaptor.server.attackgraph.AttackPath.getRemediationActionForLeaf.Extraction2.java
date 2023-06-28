/** 
 * @param leaf         An attack path leaf
 * @param topology     the network topology
 * @param conn         the database connection
 * @param useSnortRule : if true, use the snort rules else don't use it for remediation
 * @return the possible remediation action to remediate this leaf. To remediate the leaf, we can apply remediation[1] OR remadiation[2] OR remediation[3]the remediation[1] is remediation[1][1] AND remediation[1][2] AND remediation[1][3] etc...
 * @throws Exception
 */
public List<List<RemediationAction>> getRemediationActionForLeaf(Vertex leaf,InformationSystem topology,Connection conn,String costParametersFolder,boolean useSnortRule) throws Exception {
  List<List<RemediationAction>> result=new ArrayList<List<RemediationAction>>();
  if (leaf.fact != null && leaf.fact.type == FactType.DATALOG_FACT && leaf.fact.datalogCommand != null) {
    DatalogCommand command=leaf.fact.datalogCommand;
switch (command.command) {
case "vulExists":{
        List<RemediationAction> remediateVulnerability=new ArrayList<RemediationAction>();
        Vulnerability vulnerability=new Vulnerability(conn,Vulnerability.getIdVulnerabilityFromCVE(command.params[1],conn));
        List<List<InformationSystemHost>> attackerPath=getAttackerRouteToAVulnerability(leaf,topology);
        List<Patch> patches=vulnerability.getPatchs(conn);
        List<Rule> rules=vulnerability.getRules(conn);
        if (patches.size() > 0) {
          RemediationAction remediation=new RemediationAction(ActionType.APPLY_PATCH,costParametersFolder);
          remediation.setRelatedVertex(leaf);
          remediation.getPossibleMachines().add(leaf.getRelatedMachine(topology));
          for (          Patch patche : patches) {
            remediation.getRemediationParameters().add(patche);
          }
          remediateVulnerability.add(remediation);
          result.add(remediateVulnerability);
        }
        if (rules.size() > 0 && useSnortRule) {
          List<RemediationAction> detectVulnerabilityOnAllPath=new ArrayList<RemediationAction>();
          for (          List<InformationSystemHost> anAttackerPath : attackerPath) {
            RemediationAction remediation=new RemediationAction(ActionType.DEPLOY_SNORT_RULE,costParametersFolder);
            remediation.setRelatedVertex(leaf);
            for (            Rule rule : rules) {
              rule.setRule(rule.getRule().replaceFirst("alert","reject"));
              remediation.getRemediationParameters().add(rule);
            }
            for (            InformationSystemHost anAnAttackerPath : anAttackerPath) {
              remediation.getPossibleMachines().add(anAnAttackerPath);
            }
            detectVulnerabilityOnAllPath.add(remediation);
          }
          result.add(detectVulnerabilityOnAllPath);
        }
        break;
      }
case "inCompetent":    List<RemediationAction> trainUser=new ArrayList<RemediationAction>();
  RemediationAction remediation=new RemediationAction(ActionType.TRAIN_USER,costParametersFolder);
remediation.setRelatedVertex(leaf);
remediation.getRemediationParameters().add(command.params[0]);
trainUser.add(remediation);
result.add(trainUser);
break;
case "hacl":case "haclprimit":{
InformationSystemHost from=topology.getHostByNameOrIPAddress(command.params[0]);
InformationSystemHost to=topology.getHostByNameOrIPAddress(command.params[1]);
List<List<InformationSystemHost>> attackerPath=command.getRoutesBetweenHostsOfHacl(topology);
List<RemediationAction> blockAttackerOnAllPath=new ArrayList<RemediationAction>();
for (List<InformationSystemHost> anAttackerPath1 : attackerPath) {
remediation=new RemediationAction(ActionType.DEPLOY_FIREWALL_RULE,costParametersFolder);
remediation.setRelatedVertex(leaf);
getRemediationActionForLeaf_extraction_2(topology,command,remediation,from,to,anAttackerPath1);
blockAttackerOnAllPath.add(remediation);
}
result.add(blockAttackerOnAllPath);
blockAttackerOnAllPath=new ArrayList<RemediationAction>();
for (List<InformationSystemHost> anAttackerPath : attackerPath) {
remediation=new RemediationAction(ActionType.DEPLOY_FIREWALL_RULE,costParametersFolder);
remediation.setRelatedVertex(leaf);
getRemediationActionForLeaf_extraction_3(topology,command,remediation,from,to,anAttackerPath);
blockAttackerOnAllPath.add(remediation);
}
result.add(blockAttackerOnAllPath);
break;
}
}
}
return result;
}

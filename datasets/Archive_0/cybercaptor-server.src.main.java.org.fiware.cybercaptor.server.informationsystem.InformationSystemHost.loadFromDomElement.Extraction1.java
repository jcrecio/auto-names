/** 
 * Load the host from a DOM element (extracted from an XML file)
 * @param domElement the host root dom element
 * @param topology   the current network topology
 * @throws Exception
 */
public void loadFromDomElement(Element domElement,Topology topology,Database db) throws Exception {
  Element nameElement=domElement.getChild("name");
  if (nameElement != null)   this.setName(nameElement.getText());
  Element securityRequirementElement=domElement.getChild("security_requirement");
  if (securityRequirementElement != null) {
    double securityRequirementValue=Double.parseDouble(securityRequirementElement.getText());
    SecurityRequirement securityRequirement=new SecurityRequirement("sec-req-xml",securityRequirementValue);
    this.addSecurityRequirements(securityRequirement);
  }
  Element interfacesElement=domElement.getChild("interfaces");
  if (interfacesElement != null) {
    List<Element> interfaceListElement=interfacesElement.getChildren("interface");
    for (    Element interfaceElement : interfaceListElement) {
      Element interfaceNameElement=interfaceElement.getChild("name");
      Element interfaceAddressElement=interfaceElement.getChild("ipaddress");
      Element interfaceVlanElement=interfaceElement.getChild("vlan");
      if (interfaceNameElement != null && interfaceAddressElement != null) {
        String interfaceName=interfaceNameElement.getText();
        String interfaceAddress=interfaceAddressElement.getText();
        Interface inface;
        if (interfaceVlanElement != null && !interfaceVlanElement.getText().isEmpty()) {
          inface=new Interface(interfaceName,interfaceAddress,this,topology.getNewOrExistingVlan(interfaceVlanElement.getChildText("label")));
        }
 else {
          inface=new Interface(interfaceName,interfaceAddress,this);
        }
        this.getInterfaces().put(interfaceName,inface);
        Element networkElement=interfaceElement.getChild("network");
        Element maskElement=interfaceElement.getChild("mask");
        if (networkElement != null && maskElement != null) {
          inface.setNetwork(new Network(new IPAddress(networkElement.getText()),new IPAddress(maskElement.getText())));
        }
        Element directlyConnectedElement=interfaceElement.getChild("directly-connected");
        if (directlyConnectedElement != null) {
          Element connectedToInternetElement=directlyConnectedElement.getChild("internet");
          if (connectedToInternetElement != null) {
            inface.setConnectedToTheInternet(true);
          }
        }
      }
    }
  }
  Element servicesElement=domElement.getChild("services");
  if (servicesElement != null) {
    List<Element> servicesElementList=servicesElement.getChildren("service");
    for (    Element serviceElement : servicesElementList) {
      Element serviceNameElement=serviceElement.getChild("name");
      if (serviceNameElement != null) {
        String serviceName=serviceNameElement.getText();
        Service service;
        service=loadFromDomElement_extraction_3(serviceElement,serviceName);
        Element vulnerabilitiesElement=serviceElement.getChild("vulnerabilities");
        if (vulnerabilitiesElement != null) {
          List<Element> vulnsElements=vulnerabilitiesElement.getChildren("vulnerability");
          for (          Element vulnElement : vulnsElements) {
            Element typeElement=vulnElement.getChild("type");
            Element goalElement=vulnElement.getChild("goal");
            Element cveElement=vulnElement.getChild("cve");
            Vulnerability vuln=new Vulnerability(cveElement.getText());
            vuln.exploitGoal=goalElement.getText();
            vuln.exploitType=typeElement.getText();
            vuln.loadParametersFromDatabase(db.getConn());
            service.getVulnerabilities().put(vuln.cve,vuln);
          }
        }
        Element serviceDeployedPatchElement=serviceElement.getChild("deployed-patchs");
        if (serviceDeployedPatchElement != null) {
          List<Element> patchsElements=serviceDeployedPatchElement.getChildren("patch");
          for (          Element patchElement : patchsElements) {
            Patch patch=new Patch(patchElement.getText());
            service.getDeployedPatches().add(patch);
          }
        }
      }
    }
  }
  Element routesElement=domElement.getChild("routes");
  if (routesElement != null)   this.getRoutingTable().loadFromDomElement(routesElement,this);
  Element incommingFirewallElement=domElement.getChild("input-firewall");
  if (incommingFirewallElement != null)   this.getInputFirewallRulesTable().loadFromDomElement(incommingFirewallElement);
  Element outgoingFirewallElement=domElement.getChild("output-firewall");
  if (outgoingFirewallElement != null)   this.getOutputFirewallRulesTable().loadFromDomElement(outgoingFirewallElement);
}

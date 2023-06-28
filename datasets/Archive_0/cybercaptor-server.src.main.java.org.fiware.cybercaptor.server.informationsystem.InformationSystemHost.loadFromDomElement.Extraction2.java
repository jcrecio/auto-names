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
      loadFromDomElement_extraction_2(db,serviceElement,serviceNameElement);
    }
  }
  Element routesElement=domElement.getChild("routes");
  if (routesElement != null)   this.getRoutingTable().loadFromDomElement(routesElement,this);
  Element incommingFirewallElement=domElement.getChild("input-firewall");
  if (incommingFirewallElement != null)   this.getInputFirewallRulesTable().loadFromDomElement(incommingFirewallElement);
  Element outgoingFirewallElement=domElement.getChild("output-firewall");
  if (outgoingFirewallElement != null)   this.getOutputFirewallRulesTable().loadFromDomElement(outgoingFirewallElement);
}

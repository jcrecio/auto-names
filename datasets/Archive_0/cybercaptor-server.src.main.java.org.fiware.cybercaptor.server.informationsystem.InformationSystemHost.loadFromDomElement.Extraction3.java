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
        loadFromDomElement_extraction_1(topology,interfaceElement,interfaceAddressElement,interfaceVlanElement,interfaceName);
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

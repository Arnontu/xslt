package butters.rules2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Right side of two-nodes expr (map, match, ...)
 *
 */
public class RightNode extends NodeFact {
	
	static Logger logger = LoggerFactory.getLogger(RightNode.class);
	
	public RightNode(Node n) {
		super(n);
	}

	/**
	 * @return	XSL tag name (valueof, foreach, ...) or "false" if not an XML element
	 */
	public String getXslNodeTag() {
		Node node = getValue();
    	if ((node != null) && (Node.ELEMENT_NODE == node.getNodeType())) {	// element node
   			String ns = node.getNamespaceURI();
   			if (ns != null && ns.contains("/XSL/")) { 						// namespace contains "XSL"
   				String tag = node.getLocalName();
   				logger.debug(getValue() + ": xsl node tag: " + tag);
   				return tag;
   			}
    	}
    	return "false";
	}

	public boolean isXslNode() {
		String tag = getXslNodeTag();
		return ("false".equalsIgnoreCase(tag))? false : true;
	}

	/**
	 * @return child node of type text is exists. Otherwise returns null. 
	 */
	public Node getTextChild() {
		Node node = getValue();
		NodeList children = node.getChildNodes(); 
		for (int i=0; i<children.getLength(); i++) {
			Node ch = children.item(i);
			if (ch.getNodeType() == Node.TEXT_NODE) {
				logger.debug(getValue() + ": text child: " + ch);
				return ch;
			}
		}
		return null;
	}
	
	/**
	 * @return true is element has a child element.  Otherwise returns false. 
	 */
	public boolean hasChildElement() {
		Node node = getValue();
		NodeList children = node.getChildNodes(); 
		for (int i=0; i<children.getLength(); i++) {
			Node ch = children.item(i);
			if (ch.getNodeType() == Node.ELEMENT_NODE) {
				logger.debug(getValue() + ": first element child: " + ch);
				return true;
			}
		}
		logger.debug(getValue() + ": no element chidren");
		return false;
	}
	
	public boolean isLeafNode() {
		return !hasChildElement();
	}

}

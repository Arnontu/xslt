package butters.junk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import net.sf.saxon.s9api.XdmNode;

public class JSTreeBuilder {

	XdmNode rootNode;
	
	JsonNodeFactory factory = JsonNodeFactory.withExactBigDecimals(false);
	Logger logger = LoggerFactory.getLogger(JSTreeBuilder.class);
	
	public JSTreeBuilder withDocument(XdmNode root) {
		rootNode = root;
		return this;
	}

//	public JsonNode build() {
//		ArrayNode tree = factory.arrayNode();
//		JsonNode root = treeNodeBuilder(rootNode);
//		tree.add(root);
//		return tree;
//	}
	
//	protected JsonNode treeNodeBuilder(XdmNode xml) {
//		ObjectNode json = factory.objectNode();
//		json.put("value", xml.getNodeName());
//		json.put("xmlNodeType", xml.getNodeKind());
//		ArrayNode ch = factory.arrayNode();
//		json.set("children", ch);
//		logger.debug("node name: " + xml.getNodeName());
//		logger.debug("type: " + xml.getNodeKind());
//		switch(xml.getNodeKind()) {
//		case XdmNodeKind.ATTRIBUTE_NODE: break;
//		case XdmNodeKind.TEXT_NODE:
//			logger.debug("value: " + xml.getStringValue());
//			json.put("value", xml.getStringValue());
//			break;
//		case XdmNode.ELEMENT_NODE:
//			if (xml.hasChildNodes()) {
//				NodeList cl = xml.getChildNodes();
//				for (int i=0; i<cl.getLength(); i++) {
//					JsonNode j = treeNodeBuilder(cl.item(i));
//					ch.add(j);
//				}
//			}
//			break;
//		default:
//			json.put("rawXmlValue", xml.getStringValue());	// TODO serialize the XML to String
//			break;
//		}
//		
//		return json;
//	}
}

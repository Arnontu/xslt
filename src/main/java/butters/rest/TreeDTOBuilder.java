package butters.rest;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import butters.model.TreeDTO;
import butters.model.TreeNodeDTO;
import butters.xml.XMLDocumentDecorator;
import butters.xml.XSLTEditor;

public class TreeDTOBuilder {

	protected TreeDTO dto;
	private XSLTEditor editor = new XSLTEditor();
	private XMLDocumentDecorator doc;
	
	public static TreeDTOBuilder builder() {
		TreeDTOBuilder b = new TreeDTOBuilder();
		return b;
	}
	
	public TreeDTO build() {
		try {
			this.dto = walk(doc.getRoot());
		} catch (Exception e) {
			throw new RuntimeException("failed to build DTO from XML: " + e.getMessage(), e);
		}
		return dto;
	}
	
	public TreeDTOBuilder from(Node node) {
		return from(node.getOwnerDocument());
	}
	
	public TreeDTOBuilder from(Document doc) {
		try {
			this.doc = new XMLDocumentDecorator(doc);
		} catch (Exception e) {  // rethrow as runtime exception (with root cause)
			throw new RuntimeException("failed to load XML: " + e.getMessage(), e);
		}
		return this;
	}
	

	// ----------------------------------------------------------------------

	private TreeDTO walk(Node node) throws Exception {
		TreeDTO tree = new TreeDTO();
		if (doc.isEmptyText(node)) return null;  // TODO something more elegant? 
		tree.setNode(buildNode(node));

		if (node.hasAttributes()) {
			NamedNodeMap attrs = node.getAttributes();
			for (int i=0; i<attrs.getLength(); i++) {
				TreeDTO attch = buildNodesFromAttr(attrs.item(i), tree.getNode().getNodePath());
				tree.addChild(attch);
			}
		}

		Node ch = node.getFirstChild();
		while(ch != null) {
			TreeDTO dtoch = walk(ch); 
			if (dtoch != null) tree.addChild(dtoch);
			ch = ch.getNextSibling();
		}
		
		return tree;
	}
	
	
	// ----------------------------------------------------------------------

	// attribute nodes are converted into two parent-child node: 
	// - an element-like parent node (for the attr name) 
	// - a text-like child node (for the attr value)
	
	protected TreeDTO buildNodesFromAttr(Node node, String elmPath) throws Exception {
		TreeDTO t1 = new TreeDTO();	// name
		TreeNodeDTO n1 = new TreeNodeDTO();
		n1.setValue(node.getNodeName());
		n1.setType(TreeNodeDTO.NODETYPE.ELEMENT.toString());
		n1.setAttribute(true);
		n1.setNodePath(elmPath + "/@" + node.getNodeName()); 
		n1.setId(doc.getNodeOridinal(node));
		t1.setNode(n1);
		
		TreeDTO t2 = new TreeDTO(); // value
		TreeNodeDTO n2 = new TreeNodeDTO();
		n2.setValue(node.getNodeValue());
		n2.setType(TreeNodeDTO.NODETYPE.TEXT.toString());
		n2.setAttribute(true);
		n2.setNodePath(elmPath + "/@" + node.getNodeName()); 
		n2.setId(doc.getNodeOridinal(node));
		t2.setNode(n2);
		
		t1.addChild(t2);
		return t1;
	}
	
	protected TreeNodeDTO buildNode(Node node) throws Exception {
		TreeNodeDTO newnode = new TreeNodeDTO();
		newnode.setValue(getNodeValue(node));
		newnode.setType(getNodeType(node).toString());
		newnode.setAttribute((node.getNodeType() == Node.ATTRIBUTE_NODE)? true : false);
		newnode.setNodePath(editor.getNodePath(node)); 
		newnode.setId(doc.getNodeOridinal(node)); // TODO better node identifier 
		return newnode;
	}

	private TreeNodeDTO.NODETYPE getNodeType(Node node) {
		if (doc.isXSLNode(node)) {
			return TreeNodeDTO.NODETYPE.XSL;
		} else if (node.getNodeType() == Node.ELEMENT_NODE) {
			return TreeNodeDTO.NODETYPE.ELEMENT;
		} else if (node.getNodeType() == Node.TEXT_NODE) {
			return TreeNodeDTO.NODETYPE.TEXT;
		} else {
			return TreeNodeDTO.NODETYPE.DEFAULT;
		}

	}


	private String getNodeValue(Node node) {
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			return (node.getNodeName());
		} else if (node.getNodeType() == Node.TEXT_NODE) {
			return (node.getNodeValue());
		} else {
			return (node.getNodeValue());
		}
	}
		
}

package butters.jgrapht;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.w3c.dom.Node;

public class XMLImporter {

	protected Node root;
	
	public XMLImporter() {
		
	}
	
	public XMLImporter(Node root) {
		this.root = root;
	}
	
	public Graph<Node, XMLEdge> importxml() {
		if (root == null) return null;		// TODO organize constructors
		Graph<Node, XMLEdge> g = new DefaultDirectedGraph<Node, XMLEdge>(XMLEdge.class);
		walk(g, root, null);
		return g;
	}

	public Graph importxml(Node startNode) {
		Graph<Node, XMLEdge> g = new DefaultDirectedGraph<Node, XMLEdge>(XMLEdge.class);
		walk(g, startNode, null);
		return g;
	}

	protected void walk(Graph<Node, XMLEdge> g, Node n, Node parent) {

		switch (include(n)) {
		case INCLUDE:
			g.addVertex(n);
			if (parent != null)
				g.addEdge(n, parent, new XMLEdge("parent"));
			parent = n;
			break;
		case EXCLUDE_NODE:
			break;
		case EXCLUDE_DEEP:
			return;
		}

		Node ch = n.getFirstChild();
		while (ch != null) {
			walk(g,ch, parent);
			ch = ch.getNextSibling();
		}
	}
	
	
	protected enum Inclusion {INCLUDE, EXCLUDE_DEEP, EXCLUDE_NODE};
	
	protected Inclusion include(Node node) {
		Inclusion inc = Inclusion.EXCLUDE_DEEP;

		// element: exclude XSL elements
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			if (isXslNode(node)) inc = Inclusion.EXCLUDE_NODE; 
			else inc = Inclusion.INCLUDE;
			
		// value: exclude empty/WS 
		} else if (node.getNodeType() == Node.TEXT_NODE) {
			node.normalize();
			if (isEmptyText(node)) inc = Inclusion.EXCLUDE_DEEP;
			else inc = Inclusion.INCLUDE;

		} else 
			inc = Inclusion.EXCLUDE_DEEP;
		
		return inc;
	}
    
	// XSL? if namespace, prefix, or name contains "xsl" -- not the best test ...
	public boolean isXslNode(Node node) {
		boolean xsl = false;
		
		if (node.getNamespaceURI() != null && node.getNamespaceURI().toLowerCase().contains("xsl")) 
			xsl = true;
		else if (node.getPrefix() != null && node.getPrefix().contains("xsl")) 
			xsl = true;
		else if (node.getNodeName().contains("xsl")) 
			xsl = true;
		else 
			xsl = false;
		
		return xsl;
		
	}
	
	public boolean isEmptyText(Node node) {
		// ignore value nodes with all whitespaces
		if (node.getNodeType() == Node.TEXT_NODE) {
			String text = node.getNodeValue().replaceAll("\\s+","");
			if ("".equals(text)) return true;
		}
		return false;
	}


}

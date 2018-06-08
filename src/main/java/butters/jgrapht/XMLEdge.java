package butters.jgrapht;

import org.jgrapht.graph.DefaultEdge;
import org.w3c.dom.Node;

public class XMLEdge extends DefaultEdge {
	
	protected String label;
	
	public XMLEdge(String label) {
		super();
		this.label = label;
	}

	public String toString() {
		return ("edge: " + label);
	}
	
	public String getLabel() {
		return label;
	}
	
	public Node getSourceNode() {
		return (Node) getSource();
	}
	
	public Node getTargetNode() {
		return (Node) getTarget();
	}
}

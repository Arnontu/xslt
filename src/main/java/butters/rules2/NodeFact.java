package butters.rules2;

import org.w3c.dom.Node;

public class NodeFact {

	private long correlationId;
	private Node value;

	public NodeFact(long correlationId, Node value) {
		this.correlationId = correlationId;
		this.value = value;
	}

	public NodeFact(Node n) {
		this(0, n);
	}

	public Node getValue() {
		return value;
	}

	public void setValue(Node value) {
		this.value = value;
	}

	public boolean isElementNode() {
		return (value.getNodeType() == Node.ELEMENT_NODE);
	}

	public boolean isAttributeNode() {
		return (value.getNodeType() == Node.ATTRIBUTE_NODE);
	}

	public long getCorrelationId() {
		return correlationId;
	}
}

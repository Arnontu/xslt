package butters.rules2;

import org.w3c.dom.Node;

public class NodeFact {

	private Node value;

	public NodeFact(Node n) {
		this.value = n;
	}

	public Node getValue() {
		return value;
	}

	public void setValue(Node value) {
		this.value = value;
	}

}

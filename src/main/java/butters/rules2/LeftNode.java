package butters.rules2;

import org.w3c.dom.Node;

/**
 * Left side of two-nodes expr (map, match, ...)
 *
 */
public class LeftNode {
	private Node value;

	public LeftNode(Node n) {
		this.value = n;
	}
	
	public Node getValue() {
		return value;
	}

	public void setValue(Node value) {
		this.value = value;
	}
}

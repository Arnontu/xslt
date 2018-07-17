package butters.rules2;

import org.w3c.dom.Node;

/**
 * Right side of two-nodes expr (map, match, ...)
 *
 */
public class RightNode {
	private Node value;

	public RightNode(Node n) {
		this.value = n;
	}

	public Node getValue() {
		return value;
	}

	public void setValue(Node value) {
		this.value = value;
	}
}

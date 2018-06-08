package butters.jgrapht;

import java.util.Comparator;

import org.w3c.dom.Node;

public class NodeComperator implements Comparator<Node> {

	@Override
	public int compare(Node n1, Node n2) {
		int r = 0;
		r = n1.getNodeType() - n2.getNodeType();		// same type
		
		// elements' tags should match
		if (r==0 && n1.getNodeType() == Node.ELEMENT_NODE) {
			r = n1.getNodeName().compareToIgnoreCase(n2.getNodeName());
		}

		// for all other types we do not care about the node value
		//System.out.println("comparing: " + n1 + " to " + n2 + " = " + r);
		return r * (-1);
	}

}

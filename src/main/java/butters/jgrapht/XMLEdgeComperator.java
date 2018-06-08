package butters.jgrapht;

import java.util.Comparator;

import org.w3c.dom.Node;

public class XMLEdgeComperator implements Comparator<XMLEdge> {

	@Override
	public int compare(XMLEdge o1, XMLEdge o2) {
//		Comparator<Node> comperator = new NodeComperator();
		int r = 0;
		r = o1.label.compareToIgnoreCase(o2.label);
//		if (r==0) 
//			r = comperator.compare(o1.getSourceNode(), o2.getSourceNode());
//		if (r==0) 
//			r = comperator.compare(o1.getTargetNode(), o2.getTargetNode());
		return r;
	}

}

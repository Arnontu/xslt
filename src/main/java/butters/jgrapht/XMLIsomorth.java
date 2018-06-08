package butters.jgrapht;

import java.util.Comparator;
import java.util.Iterator;

import org.jgrapht.Graph;
import org.jgrapht.GraphMapping;
import org.jgrapht.alg.isomorphism.VF2SubgraphIsomorphismInspector;
import org.w3c.dom.Node;

public class XMLIsomorth {

	protected Graph<Node, XMLEdge> source, target;
	protected Comparator<Node> vertexComperator = new NodeComperator();
	protected Comparator<XMLEdge> edgeComperator = new XMLEdgeComperator();
	
	public XMLIsomorth(Graph<Node, XMLEdge> source, Graph<Node, XMLEdge> target) {
		this.source = source;
		this.target = target;
	}

	public Iterator<GraphMapping<Node, XMLEdge>> inspect() {
		
		VF2SubgraphIsomorphismInspector<Node, XMLEdge> inspector = 
				new VF2SubgraphIsomorphismInspector<>(source, target, vertexComperator, edgeComperator);
		Iterator<GraphMapping<Node, XMLEdge>> mapping = inspector.getMappings(); 
		return mapping;
	}

}

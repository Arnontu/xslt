package butters.jgrapht;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import org.jgrapht.Graph;
import org.jgrapht.GraphMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

public class RecursiveXmlIsomorth  {
	
	protected XMLImporter importer = new XMLImporter();  // TODO inject/factory
	protected Graph source;
	protected List mappings = new ArrayList();
	private Logger logger = LoggerFactory.getLogger(RecursiveXmlIsomorth.class);
	
	public RecursiveXmlIsomorth(Graph source) {
		this.source = source;
	}
	
	public void inspect(Node node) {
		logger.debug("inspecting: " + node);
		boolean found = false;
		
		if (!importer.isXslNode(node)) {
			Graph target = importer.importxml(node);
			XMLIsomorth morth = new XMLIsomorth(source, target); // TODO factory 
			Iterator<GraphMapping<Node, XMLEdge>> mapIt = morth.inspect();
			if (mapIt.hasNext()) found = true;
			mapIt.forEachRemaining(new Consumer<GraphMapping<Node, XMLEdge>>() {
				@Override
				public void accept(GraphMapping<Node, XMLEdge> t) {
//					Node x = t.getVertexCorrespondence(node, false);
					mappings.add(t);
				}
			});
		}
		
		// do not inspect children if current node is a subgraph.
		// we want to choose the "deepest" tree.
		if (!found) {
			Node ch = node.getFirstChild();
			while (ch != null) {
				if (ch.getNodeType() == Node.ELEMENT_NODE) {
					inspect(ch);
				}
				ch = ch.getNextSibling();
			} 
		}
	}
	
	public List<GraphMapping<Node, XMLEdge>> getMappings() {
		return mappings;
	}

}

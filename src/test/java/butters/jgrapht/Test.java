package butters.jgrapht;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.jgrapht.Graph;
import org.jgrapht.GraphMapping;
import org.jgrapht.alg.isomorphism.VF2SubgraphIsomorphismInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.w3c.dom.Node;

public class Test {

	@org.junit.Test
	public void test() {
		Graph<String, DefaultEdge> stringGraph = createStringGraph();
		System.out.println(stringGraph.toString());
	}

	private Graph<String, DefaultEdge> createStringGraph()
    {
        Graph<String, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);

        String v1 = "v1";
        String v2 = "v2";
        String v3 = "v3";
        String v4 = "v4";

        // add the vertices
        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);
        g.addVertex(v4);
        g.addVertex("v4");
        g.addVertex("v5");

        // add edges to create a circuit
        g.addEdge(v1, v2);
        g.addEdge(v2, v3);
        g.addEdge(v3, v4);
        g.addEdge(v4, v1);
        g.addEdge(v4, v1);

        return g;
    }
	
	
	@org.junit.Test
	public void testMapWeight() {
		Graph<String, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);
		Graph<String, DefaultEdge> h = new SimpleGraph<>(DefaultEdge.class);
		Map<GraphMapping, Integer> mappings = new TreeMap();
		
		g.addVertex("A");
		g.addVertex("a1");
		g.addEdge("A", "a1");
		System.out.println(g.toString());

		Graph<String, DefaultEdge> g1 = new SimpleGraph<>(DefaultEdge.class);
		g1.addVertex("A");
		System.out.println(g1.toString());

		Graph<String, DefaultEdge> g2 = new SimpleGraph<>(DefaultEdge.class);
		g2.addVertex("a1");
		System.out.println(g2.toString());
		
		h.addVertex("B");
		h.addVertex("b1");
		h.addEdge("B", "b1");
		h.addVertex("C");
		h.addVertex("c1");
		h.addEdge("C", "c1");
		h.addVertex("D");
		System.out.println(h.toString());

		Comparator<String> vcomp = new Comparator<String>() {
			@Override
			public int compare(String vh, String vg) {
				if ("A".equals(vg) && "B".equals(vh)) return 0;
				if ("a1".equals(vg) && "b1".equals(vh)) return 0;
				if ("A".equals(vg) && "D".equals(vh)) return 0;
				if ("a1".equals(vg) && "c1".equals(vh)) return 0;
				return vh.compareTo(vg);
			}
		};
				
		findMaps(g, h, mappings, vcomp);
		findMaps(g1, h, mappings, vcomp);
		findMaps(g2, h, mappings, vcomp);
		System.out.println(mappings);
		mappings.entrySet().stream().sorted(new Comparator(){
			@Override
			public int compare(Object o1, Object o2) {
				Entry<GraphMapping, Integer> e1 = (Entry<GraphMapping, Integer>) o1;
				Entry<GraphMapping, Integer> e2 = (Entry<GraphMapping, Integer>) o2;
				return e1.getValue().intValue() - e2.getValue().intValue(); 
			}}).toArray();
	}

	private void findMaps(Graph<String, DefaultEdge> g, Graph<String, DefaultEdge> h, Map mappings,
			Comparator<String> vcomp) {
		VF2SubgraphIsomorphismInspector<String, DefaultEdge> inspector = 
				new VF2SubgraphIsomorphismInspector<>(h, g, vcomp, null);
		Iterator<GraphMapping<String, DefaultEdge>> mapping = inspector.getMappings(); 
		while(mapping.hasNext()) {
			GraphMapping<String, DefaultEdge> m = mapping.next();
			System.out.println(m.toString());
			mappings.put(m, new Integer(g.vertexSet().size()));
		}
	}
	
	
}

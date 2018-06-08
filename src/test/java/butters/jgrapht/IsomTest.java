package butters.jgrapht;

import java.util.Iterator;

import org.jgrapht.Graph;
import org.jgrapht.GraphMapping;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import butters.BaseTest;
import butters.jgrapht.XMLEdge;
import butters.jgrapht.XMLImporter;
import butters.jgrapht.XMLIsomorth;

public class IsomTest extends BaseTest {
	
	@Test
	public void inspectSame() throws Exception {
		inspect("jgrapht/hello3.xml", "jgrapht/hello3.xml");
	}
	

	@Test
	public void inspectHello() throws Exception {
		inspect("jgrapht/hello2.xml", "jgrapht/hello3.xml");
	}

	@Test
	public void inspectHelloXslt() throws Exception {
		inspect("jgrapht/hello3.xml", "jgrapht/hello2.xsl");
	}

	@Test
	public void inspectBooks() throws Exception {
		inspect("jstree/books-in.xml", "jstree/books-out.xml");
	}
	
	private void inspect(String xml1, String xml2) throws Exception {
		
		Document d1 = loadDocument(xml1);
		XMLImporter imp = new XMLImporter(d1.getDocumentElement());
		Graph g1 = imp.importxml();
		System.out.println(g1);
		
		Document d2 = loadDocument(xml2);
		imp = new XMLImporter(d2.getDocumentElement());
		Graph g2 = imp.importxml();
		System.out.println(g2);
		
		XMLIsomorth iso = new XMLIsomorth(g1, g2);
		Iterator<GraphMapping<Node, XMLEdge>> mapIt = iso.inspect();
		while (mapIt.hasNext()) {
			GraphMapping<Node, XMLEdge> map = mapIt.next();
			System.out.println("---------------------------------------");
			printMapping(d1.getDocumentElement(), map);
		}
		System.out.println("---------------------------------------");
	}

	
	protected void printMapping(Node n1, GraphMapping<Node, XMLEdge> map) {
			Node n2 = map.getVertexCorrespondence(n1, true);
			if (n2 != null) {
				System.out.println(n1 + " -> " + n2);
			}
			
			Node ch = n1.getFirstChild();
			while (ch != null) {
				if (!isEmptyText(ch)) {
					printMapping(ch, map);
				}
				ch = ch.getNextSibling();
			}
	}
}

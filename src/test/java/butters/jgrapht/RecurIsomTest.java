package butters.jgrapht;

import static org.junit.Assert.*;

import org.jgrapht.Graph;
import org.jgrapht.GraphMapping;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import butters.jgrapht.RecursiveXmlIsomorth;
import butters.jgrapht.XMLEdge;
import butters.jgrapht.XMLImporter;

public class RecurIsomTest extends IsomTest {

	@Test
	public void test() throws Exception  {
		inspect("jstree/books-in.xml", "jstree/books-out.xml");
	}

	@Test
	public void testXslt() throws Exception  {
		inspect("jstree/books-in.xml", "jgrapht/books.xsl");
	}
	

	private void inspect(String xml1, String xml2) throws Exception {
		
		Document d1 = loadDocument(xml1);
		d1.normalizeDocument();
		XMLImporter imp = new XMLImporter(d1.getDocumentElement());
		Graph g1 = imp.importxml();
		System.out.println(g1);
		
		Document d2 = loadDocument(xml2);
		d2.normalizeDocument();
		RecursiveXmlIsomorth iso = new RecursiveXmlIsomorth(g1);
		iso.inspect(d2.getDocumentElement());
		for (GraphMapping<Node, XMLEdge> map : iso.getMappings()) { 
			System.out.println("---------------------------------------");
			printMapping(d1.getDocumentElement(), map);
		}
		System.out.println("---------------------------------------");
	}

}

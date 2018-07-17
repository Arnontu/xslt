package butters.rules2.test;

import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import butters.BaseTest;
import butters.xml.XMLDocumentDecorator;

public class MatchTest extends BaseTest {

	Logger logger = LoggerFactory.getLogger(MatchTest.class);
	
	@Test
	public void hello() throws Exception {
		test("jstree/hello.xml", "jstree/hello2.xml");
	}
	
	@Test
	public void hello3() throws Exception {
		test("jstree/hello3.xml", "jstree/hello2.xml");
	}

	@Test
	public void books() throws Exception {
		test("jstree/books-in.xml", "jstree/books-out.xml");
	}

	@Test
	public void booksnoRepeat() throws Exception {
		test("jstree/books-in-norepeat.xml", "jstree/books-out-norepeat.xml");
	}

	protected void test(String file1, String file2) throws Exception {
		Document d1 = loadDocument(file1);
		Document d2 = loadDocument(file2);
		MatchInfo info = new MatchInfo();
		find(new XMLDocumentDecorator(d1), new XMLDocumentDecorator(d2), info);
	}

	
	private class MatchInfo {
		int visited = 0;
		int match = 0;
		Node r1 = null;
		Node r2 = null;
		Map<Node, Node> map = new HashMap<Node, Node>();
		Map<Node, Node> tempMap = new HashMap<Node, Node>();
		
		public String toString() {
			return (new StringBuilder("find: ")
					.append("visited: " + visited)
					.append("   matching: " + match)
					.append("\n left at: " + r1)
					.append("\n right at: " + r2)
					.toString()
					);
		}
	}
	
	private boolean interesting(Node n) {
		if ( 
			(n.getNodeType() == Node.ELEMENT_NODE) 
			// || (n.getNodeType() == Node.TEXT_NODE && !"".equals(n.getNodeValue().replaceAll("\\s+","")))
			) return true;
			else return false;
	}
	
	private boolean equal(Node n1, Node n2) {
		if (n1.getNodeType() == n2.getNodeType() &&
			n1.getNodeName().equalsIgnoreCase(n2.getNodeName())
			// && n1.getNodeValue().equalsIgnoreCase(n2.getNodeValue())
			) return true;
		else
			return false;
	}
	
	private void map(Node n1, Node n2, MatchInfo info) {
		logger.info("map: left: " + n1 + "  right: " + n2);
		info.tempMap.put(n1,  n2);
	}
	
	/**
	 * map d1 and d2
	 * @param d1
	 * @param d2
	 * @param info
	 */
	public void find(XMLDocumentDecorator d1, XMLDocumentDecorator d2, MatchInfo info) {
		walkDoc1Outer(d1.getRoot(), d2.getRoot(), info);	
	}

	
	// walk doc1, for each node, match it against all nodes in doc2 
	private boolean walkDoc1Outer(Node r1, Node r2, MatchInfo info) {
		info.r1 = r1;
		info.r2 = r2;
		if (walkDoc2Inner(r1, r2, info)) {		// does r1 match any r2 subtreee ?? 
			logger.info("MATCH!!! " + " left: " + r1 + "  right: " + r2);
			logger.info(info.tempMap.toString());
			info.map.putAll(info.tempMap);
		}
		
		Node ch1 = r1.getFirstChild();
		while (ch1 != null) {
			if (interesting(ch1))
				if (walkDoc1Outer(ch1, r2, info)) {};	// does ch1 match any r2 subtreee ??
			ch1 = ch1.getNextSibling();
		}
		if (100* info.match / info.visited > 80) return true;  // FOUND !!!
		return false;
	}

	// walk doc2, for each node, match it against current r1 
	private boolean walkDoc2Inner(Node r1, Node r2, MatchInfo info) {
		info.visited = 1;
		info.match = 0;
		info.r1 = r1;
		info.r2 = r2;
		recursMatch(r1, r2, info);
		logger.info("inner: " + info.toString());
		if (100* info.match / info.visited > 80) return true;  // FOUND !!!

		Node ch2 = r2.getFirstChild();
		while (ch2 != null) {
			if (interesting(ch2))
				if (walkDoc2Inner(r1, ch2, info)) break;
			ch2 = ch2.getNextSibling();
		}
		if (100* info.match / info.visited > 80) return true;  // FOUND !!!
		return false;
	}
	
	// match between n1 and n2 trees  
	private boolean recursMatch(Node n1, Node n2, MatchInfo info) {
		if (equal(n1, n2)) {
			info.match++;
			map(n1, n2, info);
		}

		// even if nodes does not match, try children
		Node ch1 = n1.getFirstChild();
		while (ch1 != null) {
			if (interesting(ch1)) {
				info.visited++;
				Node ch2 = n2.getFirstChild();		// TODO do not visit mapped CH2 again
				while (ch2 != null) {
					if (interesting(ch2))
						if (recursMatch(ch1, ch2, info)) break;
					ch2 = ch2.getNextSibling();
				}
			}
			ch1 = ch1.getNextSibling();
		}
			
		if (100* info.match / info.visited > 80) return true;
		return false;
	}
}

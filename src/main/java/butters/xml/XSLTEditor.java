package butters.xml;

import static org.junit.Assert.assertNotNull;	

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@Service
public class XSLTEditor {
	
	Logger logger = LoggerFactory.getLogger(XSLTEditor.class);
	
	protected InputStream resource(String path) {
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(path);
		assertNotNull("resource as stream " + path, is);
		return is;
	}

	/** 
	 * Create an empty template 
	 * @param protoFileName
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public Document createEmptyTemplate(String protoFileName) throws ParserConfigurationException, SAXException, IOException {
		//InputStream is = resource("templatePrototype.xsl");
		InputStream is = new FileInputStream(new File(protoFileName));
		Document t = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
		return t;
	}

	/**
	 * Create template based on sample document.
	 * Start from an empty template.
	 * Reduce recurrent elements into a single xsl:for-each. 
	 * Replace value nodes with xsl:value-of.
	 *  
	 * @param sample
	 * @param empty
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public Document createFromSample(Document sample, Document empty) throws ParserConfigurationException, SAXException, IOException {
		
		// minimize the sample document 
		// replace elements' value with a const value-of selector
//		Node root = minimizeSample(sample.getDocumentElement(), new ValueOfConstReplacer());
		
		// create an empty template doc
		Document newtemplate = empty;
		NodeList nl = newtemplate.getElementsByTagName("xsl:template");
		if (nl.getLength() != 1) {
			throw new ParserConfigurationException("number of template elements should be exactly 1. found " + nl.getLength());
		}

		// import the sample doc into template
		Node t = nl.item(0);	// xls:template element
		Node root = sample.getDocumentElement();
		Node s = newtemplate.importNode(root , true);
		t.appendChild(s);
		return newtemplate;
	}
	
	/**
	 * minimize a sample document/tree
	 * @param sample	top-most node
	 * @return 	top-most node 
	 */
	public Node minimizeSample(Node sample, String repl) {
        return sample;
	}

	/**
	 * @param node
	 * @return
	 */
	
	public String getNodePath(Node node, Node root) {
		Stack<Node> st = new Stack<Node>();
		while (node != null && node.getNodeType() != Node.DOCUMENT_NODE && !node.isSameNode(root)) {
			st.push(node);
			node = node.getParentNode();
		}

		StringBuffer sb = new StringBuffer();
		while (!st.isEmpty()) {
			sb.append("/").append(st.pop().getNodeName());
		}
		return sb.toString();
	}
	
	public String getNodePath(Node node) {
		return getNodePath(node, node.getOwnerDocument());
	}
	
	/**
	 * Replace a node with <xsl:value-of> 
	 * The xquery selector is either 
	 *  - a constant equals to the origin value content.
	 *  - path to origin in sample document 
	 * 	@param origin	Node to replace 
	 * 	@param replaceToPath	set the replacing value-of selector to the path of origin node 
	 * 			(instead of keeping the origin content value). 
	 * @return
	 */
	public Node replaceWithvalueOf(Node origin, String selector) {
		Element newnode = origin.getOwnerDocument().createElement("xsl:value-of");
		Node parent = origin.getParentNode();
		newnode.setAttribute("select", selector); 
		
		parent.removeChild(origin);
		parent.appendChild(newnode);
		return newnode;
	}
	
	/**
	 * @param origin	node to delete
	 * @return			parent node
	 */
	public Node deleteNode(Node origin) {
		Node parent = origin.getParentNode();
		parent.removeChild(origin);
		return parent;
	}
	
	/**
	 * copy sample node into the template document
	 * @param sourceNode
	 * @param newParent
	 * @return new node
	 */
	public Node copyNode(Node sourceNode, Node newParent, String repl) {
		Document doc = newParent.getOwnerDocument();
		sourceNode = minimizeSample(sourceNode, repl);
		Node newNode = doc.importNode(sourceNode, true);
		newNode = newParent.appendChild(newNode);
		return newNode;
	}
}

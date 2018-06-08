package butters.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLDocumentDecorator {

	File file;
	Document doc;
	Map<Node, Long> nodeMap = new HashMap<Node, Long>();
	ArrayList<Node> nodes = new ArrayList<Node>();
	Logger logger = LoggerFactory.getLogger(XMLDocumentDecorator.class);
	
	public XMLDocumentDecorator(Document doc) throws Exception {
		super();
		this.doc = doc;
		load();
	}

	public XMLDocumentDecorator(File file) throws Exception {
		this(new FileInputStream(file));
		this.file = file;
	}
	
	public XMLDocumentDecorator(InputStream is) throws Exception {
		super();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		Document doc = dbf.newDocumentBuilder().parse(is);
		this.doc = doc;
		load();
	}
	
	public void load() throws Exception {
		Node root = doc.getDocumentElement();
		walk(root, 0, new XMLNodeVisitor() {
			@Override public void accept(long ord, Node node) {
				nodeMap.put(node, new Long(ord));
				ensureSize(nodes, (int) ord);
				nodes.add((int) ord, node);  // TODO (int) ord, 
			}
		});
	}

	private void ensureSize(List list, int i) {
		while (list.size() < i) {
			list.add(null);
		}
	}
	
	public void save() {
		try {
			if (this.file != null) {
				logger.debug("saving: " + file.getAbsolutePath());
				Transformer transformer = TransformerFactory.newInstance().newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
				StreamResult result = new StreamResult(file);
				DOMSource source = new DOMSource(doc);
				transformer.transform(source, result);
			}
		} catch (IllegalArgumentException | TransformerFactoryConfigurationError | TransformerException e) {
			logger.debug("failed to save document: " + e.getMessage());
			throw new RuntimeException("failed to save document: " + e.getMessage(), e);
		}
	}
	
	
	public long walk(Node node, long ordinal, XMLNodeVisitor visitor) {
		ordinal++;
		visitor.accept(ordinal, node);
		switch (node.getNodeType()) {
		case Node.ELEMENT_NODE:
			NamedNodeMap attrs = node.getAttributes();
			for (int i=0; i < attrs.getLength(); i++) {
				visitor.accept(++ordinal, attrs.item(i));
			}
			Node ch = node.getFirstChild();
			while(ch != null) {
				ordinal = walk(ch, ordinal, visitor);
				ch = ch.getNextSibling();
			}
			break;
		}
		return ordinal;
	}
	
	public Node getRoot() {
		return doc.getDocumentElement();
	}
	
	public boolean isXSLNode(Node node) {
    	if ((node != null) && (Node.ELEMENT_NODE == node.getNodeType())) {
   			String ns = node.getNamespaceURI();
   			if (ns != null && ns.contains("/XSL/")) { 
   				return true;
   			}
    	}
    	return false;
	}
	
	
	public long getNodeOridinal(Node node) {
		Long i = nodeMap.get(node);
		if (i == null) {
			throw new RuntimeException("node not found: " + node.toString());	// more explicit? return null?
		}
		return i.longValue();
	}
	
	public Node getNodeByOrdinal(int ord) {
		return nodes.get(ord);
	}
	
    // get node by path
    public Node getNodeByPath(String path) throws XPathExpressionException {
    	return getNodeByPath(doc.getDocumentElement(), path);
    }
    
    public Node getNodeByPath(Node context, String path) throws XPathExpressionException {
    	XPath xp =  XPathFactory.newInstance().newXPath();
    	xp.setNamespaceContext(new NamespaceResolver(doc));
    	Node n = (Node) xp.evaluate(path, context, XPathConstants.NODE);
    	return n;
    }
    
    // get node-list by xpath expr
    protected NodeList getNodeList(String expr) throws XPathExpressionException {
    	XPath xp =  XPathFactory.newInstance().newXPath();
    	xp.setNamespaceContext(new NamespaceResolver(doc));
    	NodeList list = (NodeList) xp.evaluate(expr, doc, XPathConstants.NODESET);
    	return list;
    }
    
	public boolean isEmptyText(Node node) {
		// ignore value nodes with all whitespaces
		if (node.getNodeType() == Node.TEXT_NODE) {
			String text = node.getNodeValue().replaceAll("\\s+","");
			if ("".equals(text)) return true;
		}
		return false;
	}

	protected String print() throws TransformerFactoryConfigurationError, TransformerException {
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		//initialize StreamResult with File object to save to file
		StreamResult result = new StreamResult(new StringWriter());
		DOMSource source = new DOMSource(doc);
		transformer.transform(source, result);
		String xmlString = result.getWriter().toString();
		return (xmlString);
	}

	public Node renameNode(Node node, String newName) {
		String ns = node.getNamespaceURI();
		Node renamed = doc.renameNode(node, ns, newName);
		return renamed;
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public Document getDocument() {
		return this.doc;
	}
	
}

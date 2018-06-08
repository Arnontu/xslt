package butters;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import butters.xml.NamespaceResolver;
import butters.xml.XSLTEditor;

public class BaseTest {

	protected Logger logger = LoggerFactory.getLogger("butters.rules.test");

	protected InputStream resource(String path) {
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(path);
		assertNotNull("resource as stream " + path, is);
		return is;
	}

//	protected void walk(RulesEngine engine, Rules butters.rules, Facts facts, Node node) {
//		logger.info("walking: " + node.getNodeName());
//		NodeList nl = node.getChildNodes();
//		for (int i=0; i < nl.getLength(); i++ ) {
//			walk(engine, butters.rules, facts, nl.item(i)); 
//		}
//
//		facts.put("current", node);
//		engine.fire(butters.rules, facts);
//	}

	
	protected Document loadDocument(String fileName) throws Exception {
		InputStream is = resource(fileName);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		Document doc = dbf.newDocumentBuilder().parse(is);
		return doc;
	}
	
    // get node by path
    protected Node getNodeByPath(Node context, String path) throws XPathExpressionException {
    	XPath xp =  XPathFactory.newInstance().newXPath();
    	xp.setNamespaceContext(new NamespaceResolver(context.getOwnerDocument()));
    	Node n = (Node) xp.evaluate(path, context.getOwnerDocument(), XPathConstants.NODE);
    	return n;
    }
    
    // get node-list by xpath expr
    protected NodeList getNodeList(Node context, String expr) throws XPathExpressionException {
    	XPath xp =  XPathFactory.newInstance().newXPath();
    	xp.setNamespaceContext(new NamespaceResolver(context.getOwnerDocument()));
    	NodeList list = (NodeList) xp.evaluate(expr, context.getOwnerDocument(), XPathConstants.NODESET);
    	return list;
    }
    
	protected boolean isEmptyText(Node node) {
		// ignore value nodes with all whitespaces
		if (node.getNodeType() == Node.TEXT_NODE) {
			String text = node.getNodeValue().replaceAll("\\s+","");
			if ("".equals(text)) return true;
		}
		return false;
	}

	protected String print(Node doc) throws TransformerFactoryConfigurationError, TransformerException {
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

}

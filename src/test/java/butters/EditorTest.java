package butters;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import butters.gui.EditController;
import butters.xml.XSLTEditor;

public class EditorTest {

	@Test
	public void createEmptytemplate() throws ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException {
		XSLTEditor edit = new XSLTEditor();
		Document t = edit.createEmptyTemplate("upload-dir/templatePrototype.xsl");
		assertNotNull("could not create an empty template", t);
		print(t);
	}

	
	@Test
	public void createFromSample() throws ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException {
		InputStream is = new FileInputStream(new File("upload-dir/books-out.xml"));
		Document sample = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
		XSLTEditor edit = new XSLTEditor();
		Document e = edit.createEmptyTemplate("upload-dir/templatePrototype.xsl");
		Document t = edit.createFromSample(sample, e);
		assertNotNull("could not create template", t);
		print(t);
	}

	@Test
	public void copyNodeFromSample() throws XPathExpressionException, ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException {
		EditController editor = new EditController();
		editor.setEditor(new XSLTEditor());
		Node xslt = editor.handleCopyNode("books-in.xml", "new2.xsl", "/BOOKLIST/BOOKS", "/xsl:stylesheet/xsl:template/root/copyhere[1]");
		print(xslt.getOwnerDocument());
	}
	
	protected void print(Document doc) throws TransformerFactoryConfigurationError, TransformerException {
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		//initialize StreamResult with File object to save to file
		StreamResult result = new StreamResult(new StringWriter());
		DOMSource source = new DOMSource(doc);
		transformer.transform(source, result);
		String xmlString = result.getWriter().toString();
		System.out.println(xmlString);
	}
	
	
	protected XPath getXpath() {
		XPath xpath = XPathFactory.newInstance().newXPath();
		return xpath;
	}

	protected void assertXpath(Document doc, XPath xpath, String expr, String expected)
			throws XPathExpressionException {
		String s = xpath.evaluate(expr, doc);
		assertEquals(expected, s);
	}

}

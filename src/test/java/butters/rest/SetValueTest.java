package butters.rest;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import javax.xml.xpath.XPathExpressionException;

import org.junit.Test;
import org.w3c.dom.Node;

import butters.model.TreeDTO;
import butters.model.TreeEditRequest;
import butters.model.TreeNodeDTO;
import butters.storage.FileSystemStorageService;
import butters.storage.StorageProperties;
import butters.storage.StorageService;
import butters.xml.XMLDocumentDecorator;

public class SetValueTest {

	StorageService storage = new FileSystemStorageService(new StorageProperties());
	
	@Test
	public void testSetTextValue() throws Exception {
		TreeDTO rsp = controllerSetValue("books-in2.xml", "/BOOKLIST/BOOKS/ITEM/TITLE/text()", "FOO", "TEXT");
	} 

	@Test
	public void testSetAttributeValue() throws Exception {
		TreeDTO rsp = controllerSetValue("books-in2.xml", "/BOOKLIST/BOOKS/ITEM[6]/@CAT", "FOO", "TEXT");
	}

	@Test
	public void testRenameAtt() throws Exception {
		TreeDTO rsp = controllerSetValue("books-in2.xml", "/BOOKLIST/BOOKS/ITEM[6]/@CAT", "ALPHA", "ELEMENT");
	}


	@Test
	public void testRenameElement() throws Exception {
		TreeDTO rsp = controllerSetValue("books-in2.xml", "/BOOKLIST/BOOKS/ITEM[3]/AUTHOR", "BETA", "ELEMENT");
	}

	@Test
	public void testRenameElementWithNS() throws Exception {
		TreeDTO rsp = controllerSetValue("books-in2.xml", "/BOOKLIST/BOOKS/ITEM/test:gama", "test:DELTA", "TEXT");
	}


	/**
	 * @param type TODO
	 * @throws Exception
	 * @throws XPathExpressionException
	 * @throws FileNotFoundException
	 */
	private TreeDTO controllerSetValue(String fileName, String path, String newValue, String type) throws Exception, XPathExpressionException, FileNotFoundException {
		TreeEditRequest rq = new TreeEditRequest();		// request body
		XMLDocumentDecorator doc = new XMLDocumentDecorator(storage.load(fileName).toFile());
		Node xn = doc.getNodeByPath(path);	// xml node
		
		TreeNodeDTO tn = new TreeNodeDTO();   // target node
		tn.setId(doc.getNodeOridinal(xn));
		tn.setNodePath(path);
		tn.setType(type);
		tn.setValue("some old value");
		rq.setTargetNode(tn);
		rq.setTargetTree("books-in2.xml");
		rq.setNewValue(newValue);

		RestController rc = new RestController(); 
		rc.storage = this.storage;	// this is not a Spring test ...
		TreeDTO rsp = rc.setvalue(rq);
		return rsp;
	}


}

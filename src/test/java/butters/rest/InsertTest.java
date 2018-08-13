package butters.rest;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import butters.model.TreeDTO;
import butters.model.TreeEditRequest;
import butters.model.TreeNodeDTO;
import butters.storage.StorageService;
import butters.xml.XMLDocumentDecorator;

@SuppressWarnings("unused")
@RunWith(MockitoJUnitRunner.class)
public class InsertTest {

	@Mock StorageService storage;
	Logger logger = LoggerFactory.getLogger(InsertTest.class);
	
	@Test
	public void insertElementTest() throws Exception {
		File tmp = createTmpFile("jstree/hello.xsl");
		TreeEditRequest rq = buildRequest("//xml/element", tmp.getName(), "//hello", "ELEMENT");
		TreeDTO rsp = insert(rq, tmp.getPath());
	}

	
	///////////////////////////
	
	
	private File createTmpFile(String targetFile) throws Exception {
		File tmp = File.createTempFile("insertTest", "xslt");
		//tmp.deleteOnExit();
		URL srcFile = this.getClass().getClassLoader().getResource(targetFile);
		FileUtils.copyURLToFile(srcFile, tmp);
		return tmp;
	}

	private XMLDocumentDecorator loadDocument(String path) throws Exception {
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(path);
		assertNotNull("resource as stream " + path, is);
		XMLDocumentDecorator doc = new XMLDocumentDecorator(is);
		return doc;
	}

	private XMLDocumentDecorator loadFragments() throws Exception {
		return loadDocument("/butters/prototypes/fragmentsPrototype.xsl");
	}
	
	private TreeEditRequest buildRequest(String fragPath, String targetFile, String targetPath, String fragType) throws Exception {
		TreeEditRequest rq = new TreeEditRequest();		// request body

		XMLDocumentDecorator fragdoc = loadFragments();
		Node xn = fragdoc.getNodeByPath(fragPath);	// fragment node

		TreeNodeDTO tn = new TreeNodeDTO();   // source fragment node
		tn.setId(fragdoc.getNodeOridinal(xn));
		tn.setNodePath(fragPath);
		tn.setType(fragType);
		rq.setFromNode(tn);
		rq.setFromTree("fragmentsPrototype.xsl");

		XMLDocumentDecorator doc = loadDocument(targetFile);
		Node targetNode = doc.getNodeByPath(targetPath);	

		TreeNodeDTO tn2 = new TreeNodeDTO(); 
		tn2.setId(doc.getNodeOridinal(targetNode));
		tn.setNodePath(fragPath);
		rq.setTargetNode(tn2);
		rq.setTargetTree("/jstree/hello.xslt");

		return rq;
	}
	
	private TreeDTO insert(TreeEditRequest rq, String targetFile) throws URISyntaxException {
		RestController rc = new RestController(); 
		Path path = Paths.get(targetFile);
		when(storage.load(targetFile)).thenReturn(path);
		rc.storage = this.storage;
		TreeDTO rsp = rc.insert(rq);
		return rsp;
	}

}

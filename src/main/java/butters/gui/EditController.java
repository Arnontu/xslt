package butters.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import butters.model.TreeDTO;
import butters.model.TreeNodeDTO;
import butters.rest.TreeDTOBuilder;
import butters.storage.StorageService;
import butters.xml.NamespaceResolver;
import butters.xml.XMLDocumentDecorator;
import butters.xml.XSLTEditor;
import butters.xml.XSLTTransform;
import net.sf.saxon.s9api.SaxonApiException;

//TODO use storage service (instead of upload-dir const)

@Controller
public class EditController {

	@Autowired	XSLTEditor editor;
	@Autowired XSLTTransform transformer;
	@Autowired StorageService storage;
	
	Logger logger = LoggerFactory.getLogger(EditController.class);
	
	// main editor screen (3 trees, etc.)
	// arguments: in, out, and xslt FILES
    @PostMapping("/edit")
    public String openTemplateEditor(
    		Model model, 
    		@RequestParam("sampleInputFile") String xmlFileName,
    		@RequestParam("templateFile") String xslFileName,
    		@RequestParam("sampleOutputFile") String outFileName,
    		@RequestParam(value="createMode", required=false) String createMode
    		
    		) throws ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException {
    	
    	Node xslDoc;
    	
//    	JsonNode xmlData = getJsonData(xmlFileName);
    	logger.info(String.format("enter template editor: %s, %s, %s, %s", xmlFileName, xslFileName, outFileName, createMode));
    	
    	// create new EMPY template 
    	if ("EMPTY".equalsIgnoreCase(createMode)) {
    		Document doc = editor.createEmptyTemplate(storage.load("templatePrototype.xsl").toString());
    		xslDoc = doc.getDocumentElement();
    		saveDocument(xslFileName, doc);
    		
    	// create from SAMPLE
    	} else if ("SAMPLE".equalsIgnoreCase(createMode)) {
    		InputStream is = new FileInputStream(storage.load(outFileName).toFile());
    		Document sample = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
    		Document empty = editor.createEmptyTemplate(storage.load("templatePrototype.xsl").toString());
    		Document doc = editor.createFromSample(sample, empty);
    		xslDoc = doc.getDocumentElement();
    		saveDocument(xslFileName, doc);
    	
    	// OPEN existing
    	} else {
        	xslDoc = loadDocument(xslFileName);
    	}
    	
    	Node xmlDoc;
		try {
			xmlDoc = loadDocument(xmlFileName);
		} catch (IOException e) {    // no such doc. create empty doc.
    		Document doc = editor.createEmptyTemplate(storage.load("xmlfilePrototype.xsl").toString());
    		xmlDoc = doc.getDocumentElement();
    		saveDocument(xmlFileName, doc);
		}
		
    	Node outputDoc;
		try {
			outputDoc = loadDocument(outFileName);
		} catch (IOException e) {
    		Document doc = editor.createEmptyTemplate(storage.load("xmlfilePrototype.xsl").toString());
    		outputDoc = doc.getDocumentElement();
    		saveDocument(outFileName, doc);
		}
		
    	TreeDTO xmlData = TreeDTOBuilder.builder().from(xmlDoc).build(); 
    	TreeDTO xslData = TreeDTOBuilder.builder().from(xslDoc).build();
    	TreeDTO outputData = TreeDTOBuilder.builder().from(outputDoc).build();
    	
    	model.addAttribute("xmlData", xmlData);
    	model.addAttribute("xsltData", xslData);
    	model.addAttribute("outputData", outputData);
    	model.addAttribute("xmlFileName", xmlFileName);
    	model.addAttribute("xslFileName", xslFileName);
    	model.addAttribute("outFileName", outFileName);
    	
        return "edit3";
    }

    /**
     * @param xmlFileName
     * @param xslFileName
     * @param spath
     * @param tpath
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws XPathExpressionException
     */
    @GetMapping("/copy")
    public @ResponseBody Node handleCopyNode(
	    		@RequestParam("sampleInputFile") String xmlFileName,
	    		@RequestParam("templateFile") String xslFileName,
	    		@RequestParam("sourcePath") String spath,
	    		@RequestParam("targetPath") String tpath
    		) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException
    {
    	logger.info(String.format("copy node: from %s(%s) to %s(%s) ", xmlFileName, spath, xslFileName, tpath));
		Node tdoc = loadDocument(xslFileName);
		Node sdoc = loadDocument(xmlFileName);
		Node snode = getNodeByPath(sdoc, spath);
		Node tnode = getNodeByPath(tdoc, tpath);
//    	editor.copyNode(snode, tnode, new ValueOfPathReplacer());
    	return tdoc;	// return updated template
    }
    
    
    // load document from file 
    // TODO use storage service
    protected Node loadDocument(String xmlFileName) throws ParserConfigurationException, SAXException, IOException {
    	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    	dbf.setNamespaceAware(true);
//		InputStream is = this.getClass().getClassLoader().getResourceAsStream(xmlFileName);
    	InputStream is = new FileInputStream(storage.load(xmlFileName).toFile());
    	assert(is != null);
		DocumentBuilder builder = dbf.newDocumentBuilder();
        Document doc = builder.parse(is);
        return doc.getDocumentElement();
    }

    // save document to files
    // TODO use storage service
    protected void saveDocument(String xmlFileName, Document doc) throws TransformerFactoryConfigurationError, TransformerException {
    	logger.debug("saving: " + xmlFileName);
    	Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		StreamResult result = new StreamResult(new File("upload-dir/" +xmlFileName));
		DOMSource source = new DOMSource(doc);
		transformer.transform(source, result);
    }
    
    // get node by path
    protected Node getNodeByPath(Node context, String path) throws XPathExpressionException {
    	XPath xp =  XPathFactory.newInstance().newXPath();
    	xp.setNamespaceContext(new NamespaceResolver(context.getOwnerDocument()));
    	Node n = (Node) xp.evaluate(path, context.getOwnerDocument(), XPathConstants.NODE);
    	return n;
    }
    
 
//    private TreeNodeDTO buildTreeDTO(Document doc) throws Exception {
//    	XMLDocumentDecorator xdd = new XMLDocumentDecorator(doc);
//    	Node docRoot = doc.getDocumentElement();
//    	TreeNodeDTO dtoRoot = walk(docRoot, doc);
//    	return dtoRoot;
//    }
//    
//    private TreeNodeDTO walk(Node node, Document doc) throws Exception {
//    	TreeNodeDTO dto = TreeDTOBuilder.builder().from(doc).from(node).build();
//    	return null;
//    }


	public void setEditor(XSLTEditor editor) {
		this.editor = editor;
	}
    
//    protected JsonNode getJsonData(String xmlFileName) throws ParserConfigurationException, SAXException, IOException {
//    	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//		InputStream is = this.getClass().getClassLoader().getResourceAsStream("jstree/butters.xml");
//		assert(is != null);
//		DocumentBuilder builder = dbf.newDocumentBuilder();
//        Document doc = builder.parse(is);
// 
//        JSTreeBuilder jsonBuilder = new JSTreeBuilder().withDocument(doc);
//        JsonNode root = jsonBuilder.build();
//        String jsonStr = root.toString();
//        logger.debug(jsonStr);
//    	return root;
//    }

}

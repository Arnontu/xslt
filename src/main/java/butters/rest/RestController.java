package butters.rest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Path;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Node;

import butters.model.TreeDTO;
import butters.model.TreeEditRequest;
import butters.model.TreeNodeDTO;
import butters.rules2.map.NodeMappingService;
import butters.storage.StorageService;
import butters.xml.XMLDocumentDecorator;
import butters.xml.XSLTEditor;
import butters.xml.XSLTTransform;

@Controller
@RequestMapping("/api/xslt")
public class RestController {
	@Autowired	XSLTEditor editor;
	@Autowired XSLTTransform transformer;
	@Autowired StorageService storage;
	@Autowired NodeMappingService mapper;
	
	Logger logger = LoggerFactory.getLogger(RestController.class);

	/**
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/load")
	public @ResponseBody TreeDTO load(@RequestBody TreeEditRequest req) throws Exception {
		logger.debug(req.toString());
		Path templFile = storage.load(req.getTargetTree());
		XMLDocumentDecorator tmdoc = new XMLDocumentDecorator(templFile.toFile());
		TreeDTO tree = TreeDTOBuilder.builder().from(tmdoc.getRoot()).build();
		return tree;
	}
	
	
	/**
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/map")
	public @ResponseBody TreeDTO map(@RequestBody TreeEditRequest req) throws Exception {
		logger.debug(req.toString());
		boolean dirty = false;

		// get template target node 
		Path templFile = storage.load(req.getTargetTree());
		XMLDocumentDecorator tmdoc = new XMLDocumentDecorator(templFile.toFile());
		Node templnode = tmdoc.getNodeByOrdinal((int) req.getTargetNode().getId());
		logger.debug("map: target node: " + templnode);
		
		// get input source node 
		Path inputFile = storage.load(req.getFromTree());
		XMLDocumentDecorator indoc = new XMLDocumentDecorator(inputFile.toFile());
		Node inputnode = indoc.getNodeByOrdinal((int) req.getFromNode().getId());
		logger.debug("map: source node: " + inputnode);

		// mapping service 
		mapper.map(inputnode, templnode);
		dirty = true;   // service method is void, but throws in case of errors
		// TODO cleaner dirty and error reporting 
		
		// save and return templ doc
		if (dirty) {
			logger.debug("map: saving updated template ... ");
			tmdoc.save();
		}
		TreeDTO tree = TreeDTOBuilder.builder().from(tmdoc.getRoot()).build();
		return tree;
	}
	
	/**
	 * @param req
	 * @return
	 * @throws FileNotFoundException
	 * @throws Exception
	 */
	@PostMapping("/setvalue")
	public @ResponseBody TreeDTO setvalue(@RequestBody TreeEditRequest req) throws FileNotFoundException, Exception {
		logger.debug(req.toString());
		boolean dirty = false;
		Path p = storage.load(req.getTargetTree());
		XMLDocumentDecorator doc = new XMLDocumentDecorator(p.toFile());
		Node node = doc.getNodeByOrdinal((int) req.getTargetNode().getId());
		
		// TODO move to @service
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			doc.renameNode(node, req.getNewValue());	// element: value is the element tag
			dirty = true;
		} else if (node.getNodeType() == Node.TEXT_NODE) {
			node.setNodeValue(req.getNewValue());
			dirty = true;
		} else if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
			// attribute XML nodes have two tree entries: one as element, and 2nd as text 
			if (req.getTargetNode().getType().equalsIgnoreCase(TreeNodeDTO.NODETYPE.ELEMENT.toString())) {
				// element part, therefore rename
				doc.renameNode(node, req.getNewValue());
				dirty = true;
			} else if (req.getTargetNode().getType().equalsIgnoreCase(TreeNodeDTO.NODETYPE.TEXT.toString())) {
				// text part, therefore set attr value
				node.setNodeValue(req.getNewValue());
				dirty = true;
			} else {
				logger.error(req.toString());
				throw new RuntimeException("invalid TYPE for an attribute node: " + req.getTargetNode().getType());
			}
		}
		
		if (dirty) {
			doc.save();
		}
		
		TreeDTO tree = TreeDTOBuilder.builder().from(doc.getRoot()).build();
		return tree;
	}
	
	
    /**
     * test template and save the result.
     * input & teamplate already saved.
     * @param req
     * @return
     * @throws Exception
     */
    @PostMapping("/run")
    public @ResponseBody TreeDTO handleXsltTest(@RequestBody TreeEditRequest req) throws Exception 
    {
    	logger.info("run xsl: " + req.getTemplateTree() +  " on  " + req.getFromTree() + "  -->  " + req.getTargetTree());
		Source t = new StreamSource(new FileInputStream(storage.load(req.getTemplateTree()).toFile()));
		Source d = new StreamSource(new FileInputStream(storage.load(req.getFromTree()).toFile())); // TODO do not overide samle outout
		OutputStream o = new FileOutputStream(storage.load(req.getTargetTree()).toFile());
		transformer.process(t, d, o);
		o.close();
		
		XMLDocumentDecorator doc = new XMLDocumentDecorator(storage.load(req.getTargetTree()).toFile());
		TreeDTO outputData = TreeDTOBuilder.builder().from(doc.getRoot()).build();
    	return outputData;
    }


    /**
     * @param rq
     * @return
     */
    @PostMapping("/insert")
	public TreeDTO insert(TreeEditRequest rq) {
		return null;
	}


}

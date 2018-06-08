package jstree;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.w3c.dom.Document;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import butters.BaseTest;
import butters.model.TreeDTO;
import butters.rest.TreeDTOBuilder;


public class XmlToTree extends BaseTest {
	

	@Test
	public void helloTest() throws Exception {
        Document doc = loadDocument("jstree/butters.xml");
        TreeDTO tree = TreeDTOBuilder.builder().from(doc).build();
        assertNotNull("builder returned null root", tree);
        print(tree);
	}


	@Test
	public void booksInTest() throws Exception {
        Document doc = loadDocument("jstree/books-in.xml");
        TreeDTO tree = TreeDTOBuilder.builder().from(doc).build();
        assertNotNull("builder returned null root", tree);
        print(tree);
	}

	@Test
	public void helloXsltTest() throws Exception {
        Document doc = loadDocument("jstree/butters.xslt");
        TreeDTO tree = TreeDTOBuilder.builder().from(doc).build();
        assertNotNull("builder returned null root", tree);
        print(tree);
	}

	private void print(TreeDTO tree) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(tree);
//		System.out.println(jsonString);
		System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(tree));
	}
}

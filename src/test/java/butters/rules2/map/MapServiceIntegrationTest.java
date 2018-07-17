package butters.rules2.map;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;
import org.w3c.dom.Node;

import butters.BaseTest;
import butters.xml.XMLDocumentDecorator;

@RunWith(SpringRunner.class)
@SpringBootTest
//	(classes={
//		butters.xml.XSLTEditor.class,
//		butters.rules2.RulesConfiguration.class, 
//		butters.rules2.map.NodeMappingService.class,
//		butters.rules2.map.RulesRunner.class,
//		butters.rules2.map.RulesContext.class
//		})
@AutoConfigureMockMvc
public class MapServiceIntegrationTest extends BaseTest {

	
	@Autowired NodeMappingService srv;
	
	@Test
	public void shouldMapElements() throws Exception {
		XMLDocumentDecorator doc1 = new XMLDocumentDecorator(resource("rules/hello2.xml"));
		Node src = doc1.getNodeByPath("/butters");
		XMLDocumentDecorator doc2 = new XMLDocumentDecorator(resource("rules/hello2.xslt"));
		Node trgt = doc2.getNodeByPath("//butters");
		srv.map(src, trgt);
		
		fail("Not yet implemented");
	}

}

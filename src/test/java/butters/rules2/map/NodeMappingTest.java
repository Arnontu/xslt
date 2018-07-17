package butters.rules2.map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import butters.rules2.LeftNode;
import butters.rules2.RightNode;
import butters.rules2.test.BaseRuleTest;
import butters.xml.XMLDocumentDecorator;

@SuppressWarnings("unused")
@RunWith(MockitoJUnitRunner.class)
public class NodeMappingTest extends BaseRuleTest{

	@Mock RulesContext ctx;
	Logger logger = LoggerFactory.getLogger(NodeMappingTest.class); 
	
	@Test
	public void elementToElement() throws Exception {
		XMLDocumentDecorator doc1 = new XMLDocumentDecorator(resource("rules/hello2.xml"));
		Node src = doc1.getNodeByPath("/butters");
		XMLDocumentDecorator doc2 = new XMLDocumentDecorator(resource("rules/hello2.xslt"));
		Node trgt = doc2.getNodeByPath("//butters");

		loadSession("MapKS"); 
		ksession.setGlobal("$app", ctx);
		ksession.insert(new LeftNode(src));
		ksession.insert(new RightNode(trgt));
		
		when(ctx.getLogger()).thenReturn(logger);
		fire();
		verify(ctx).replaceWithvalueOf(trgt, src); 
	}

}

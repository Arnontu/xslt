package butters.rules2.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.runtime.rule.QueryResults;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import butters.rules2.LeftNode;
import butters.rules2.ResultBean;
import butters.rules2.RightNode;
import butters.rules2.test.BaseRuleTest;
import butters.xml.XMLDocumentDecorator;
import butters.xml.XSLTEditor;

@SuppressWarnings("unused")
@RunWith(MockitoJUnitRunner.class)
public class NodeMappingTest extends BaseRuleTest{

	@Mock RulesContext ctx;
	Logger logger = LoggerFactory.getLogger(NodeMappingTest.class); 
	
	/**
	 * map element to leaf element. 
	 */
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

	/**
	 * map element to <xsl:value-of> element. rule should replace the value of @select attribute. 
	 */
	@Test
	public void udpateValueOf() throws Exception {
		XMLDocumentDecorator doc1 = new XMLDocumentDecorator(resource("rules/hello2.xml"));
		Node src = doc1.getNodeByPath("//butters");
		XMLDocumentDecorator doc2 = new XMLDocumentDecorator(resource("rules/valueof.xslt"));
		Node trgt = doc2.getNodeByPath("//butters/xsl:value-of");

		loadSession("MapKS"); 
		ksession.setGlobal("$app", ctx);
		ksession.insert(new LeftNode(src));
		ksession.insert(new RightNode(trgt));
		
		when(ctx.getLogger()).thenReturn(logger);
		fire();
		verify(ctx).setPathAttribute(trgt, "select", src); 
		QueryResults results = ksession.getQueryResults("result");
		assertEquals("single result from rule", 1, results.size());
		ResultBean r = (ResultBean) results.iterator().next().get("result");
		assertEquals("info level", "info", r.getLevel());
		assertTrue("update path", r.getMessage().contains("update value-of"));
	}

	/**
	 * map element to <xsl:fop-each> element. rule should replace the value of @select attribute. 
	 */
	@Test
	public void udpateForEach() throws Exception {
		XMLDocumentDecorator doc1 = new XMLDocumentDecorator(resource("rules/hello2.xml"));
		Node src = doc1.getNodeByPath("//butters");
		XMLDocumentDecorator doc2 = new XMLDocumentDecorator(resource("rules/foreach.xslt"));
		Node trgt = doc2.getNodeByPath("//butters/xsl:for-each");

		loadSession("MapKS"); 
		ksession.setGlobal("$app", ctx);
		ksession.insert(new LeftNode(src));
		ksession.insert(new RightNode(trgt));
		
		when(ctx.getLogger()).thenReturn(logger);
		fire();
		verify(ctx).setPathAttribute(trgt, "select", src); 

		QueryResults results = ksession.getQueryResults("result");
		assertEquals("single result from rule", 1, results.size());
		ResultBean r = (ResultBean) results.iterator().next().get("result");
		assertEquals("info level", "info", r.getLevel());
		assertTrue("update path", r.getMessage().contains("update for-each"));
	}

	/**
	 * map element to an attribute. rule should replace the value of parent element. 
	 */
	@Test
	public void mapElementToAttribute() throws Exception {
		XMLDocumentDecorator doc1 = new XMLDocumentDecorator(resource("rules/hello2.xml"));
		Node src = doc1.getNodeByPath("//butters");
		XMLDocumentDecorator doc2 = new XMLDocumentDecorator(resource("rules/attribute.xslt"));
		Node trgt = doc2.getNodeByPath("//butters/@hello");
		Node elm = doc2.getNodeByPath("//butters");

		loadSession("MapKS"); 
		ksession.setGlobal("$app", ctx);
		ksession.insert(new LeftNode(src));
		ksession.insert(new RightNode(trgt));
		
		when(ctx.getLogger()).thenReturn(logger);
		fire();
		verify(ctx).replaceWithvalueOf(elm, src); 
		
		QueryResults results = ksession.getQueryResults("result");
		assertEquals("single result from rule", 1, results.size());
		ResultBean r = (ResultBean) results.iterator().next().get("result");
		assertEquals("info level", "info", r.getLevel());
	}

	/**
	 * map element to a non-leaf element. Should do nothing. 
	 */
	@Test
	public void notLeafElement() throws Exception {
		XMLDocumentDecorator doc1 = new XMLDocumentDecorator(resource("rules/hello2.xml"));
		Node src = doc1.getNodeByPath("//butters");
		XMLDocumentDecorator doc2 = new XMLDocumentDecorator(resource("rules/valueof.xslt"));
		Node trgt = doc2.getNodeByPath("//butters");

		loadSession("MapKS"); 
		ksession.setGlobal("$app", ctx);
		ksession.insert(new LeftNode(src));
		ksession.insert(new RightNode(trgt));
		
		when(ctx.getLogger()).thenReturn(logger);
		fire();
		QueryResults results = ksession.getQueryResults("result");
		assertEquals("no results from rule", 0, results.size());
	}

	/**
	 * test rules callback/service method to replace a text with <xsl:value-of> 
	 */
	@Test
	public void replaceWithValueOf()  throws Exception {
		XMLDocumentDecorator doc2 = new XMLDocumentDecorator(resource("rules/hello2.xslt"));
		Node trgt = doc2.getNodeByPath("//butters");
		XSLTEditor editor = new XSLTEditor();
		editor.replaceWithvalueOf(trgt, "/path/to/heaven");
		Node after = doc2.getNodeByPath(trgt, "xsl:value-of");
		assertNotNull("xsl:value-of element", after);
		Node selector = doc2.getNodeByPath(after, "@select");
		assertNotNull("@select attribute", selector);
		assertEquals("@select attribute", "/path/to/heaven", selector.getNodeValue() );
	}


}

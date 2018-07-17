package butters.rules2.test;

import static org.junit.Assert.assertNotNull;

import java.io.InputStream;

import org.junit.Test;
import org.kie.api.runtime.KieSession;

import butters.xml.XMLDocumentDecorator;

public class Compare extends BaseRuleTest {

	@Test
	public void compareBooks() throws Exception {
		loadSession("compare"); 
		XMLDocumentDecorator doc1 = new XMLDocumentDecorator(resource("rules/books-in-norepeat.xml"));
		XMLDocumentDecorator doc2 = new XMLDocumentDecorator(resource("rules/books-out-norepeat.xml"));
		ksession.setGlobal("$leftDoc", doc1.getDocument());
		ksession.setGlobal("$rightDoc", doc2.getDocument());
		insertDocument(doc1);
		insertDocument(doc2);
		debug(false);
		fire();
	    dispose();
	}
}

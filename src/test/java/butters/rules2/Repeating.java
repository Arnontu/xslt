package butters.rules2;

import static org.junit.Assert.assertNotNull;

import java.io.InputStream;

import org.junit.Test;
import org.kie.api.runtime.KieSession;

public class Repeating extends BaseRuleTest {

	@Test
	public void testReapeatingBooks() throws Exception {
		loadSession("repeating");
		insertDocument("rules/books-in.xml");
	    fire();
		dispose(); 
	}
}

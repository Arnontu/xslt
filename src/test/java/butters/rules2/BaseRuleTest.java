package butters.rules2;

import org.kie.api.KieServices;
import org.kie.api.event.rule.DebugAgendaEventListener;
import org.kie.api.event.rule.DebugRuleRuntimeEventListener;
import org.kie.api.io.ResourceType;
import org.kie.api.logger.KieRuntimeLogger;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.logger.KnowledgeRuntimeLoggerFactory;
import org.w3c.dom.Node;

import butters.BaseTest;
import butters.xml.XMLDocumentDecorator;

public class BaseRuleTest extends BaseTest {

    protected KieContainer kc;
    protected KieSession ksession;
    KieRuntimeLogger logger;
    
	protected void loadSession(String sessionName) throws Exception {
	    kc = KieServices.Factory.get().getKieClasspathContainer();
	    ksession = kc.newKieSession(sessionName);
	}
	
	protected void loadRules(String[] rules) {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		for ( int i = 0; i < rules.length; i++ ) {
			String r = rules[i];
			kbuilder.add( ResourceFactory.newClassPathResource(r, BaseRuleTest.class ), ResourceType.DRL );
		}
		ksession = kbuilder.newKieBase().newKieSession();
	}
	
	protected void debug(boolean logging) {
		if (logging) {
			//ksession.setGlobal( "list", new ArrayList<Object>() );
		    ksession.addEventListener( new DebugAgendaEventListener() );
		    ksession.addEventListener( new DebugRuleRuntimeEventListener() );
		    logger = KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);
		} else {
			if (logger!=null) logger.close();
		}
	}
	
	protected void insertDocument(String xmlFileName) throws Exception {
		XMLDocumentDecorator doc = new XMLDocumentDecorator(resource(xmlFileName));
		insertDocument(doc);
	}
	
	protected void insertDocument(XMLDocumentDecorator doc) throws Exception {
		for (Node n : doc.getNodes()) {
			if (n!=null && !doc.isEmptyText(n)) ksession.insert(n);
		}
	}
	
	protected void fire() {
	    ksession.fireAllRules();
	}
	
	protected void dispose() {
		if (logger!=null) logger.close();
		ksession.dispose();
	}
}
package butters.rules2;

import org.kie.api.KieBase;
import org.kie.api.event.rule.DebugAgendaEventListener;
import org.kie.api.event.rule.DebugRuleRuntimeEventListener;
import org.kie.api.logger.KieRuntimeLogger;
import org.kie.api.runtime.Globals;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;
import org.kie.internal.logger.KnowledgeRuntimeLoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import butters.xml.XMLDocumentDecorator;

@SuppressWarnings("unchecked")
@Component
public abstract class AbstractRuleRunner<T> {

    @Autowired protected KieContainer container;
    protected KieBase kbase;
    protected KieSession session;
    KieRuntimeLogger kieLogger;
    private static Logger logger = LoggerFactory.getLogger("butters.rules2.RuleRunner"); 
    
	public T withKnowledgeBase(String kbaseName)  {
		KieBase kbase = container.getKieBase(kbaseName);
	    return (T) this;
	}
	
	public T withKnowledgeSesssion(String sessionName)  {
	    session = container.newKieSession(sessionName);
	    return (T) this;
	}

//	protected void withRule(String rule) {
//    KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
//		kbuilder.add( ResourceFactory.newClassPathResource(rule, BaseRuleTest.class ), ResourceType.DRL );
//		session = kbuilder.newKieBase().newKieSession();
//	}
	
	public T debug(boolean logging) {
		if (logging) {
			//ksession.setGlobal( "list", new ArrayList<Object>() );
		    session.addEventListener( new DebugAgendaEventListener() );
		    session.addEventListener( new DebugRuleRuntimeEventListener() );
		    kieLogger = KnowledgeRuntimeLoggerFactory.newConsoleLogger(session);
		    
		    Globals globals = session.getGlobals();
		    System.out.println( "Globals: " + globals.getGlobalKeys() );
		} else {
			if (kieLogger!=null) kieLogger.close();
		}
		
		return (T) this;
	}
	
	public T withDocument(XMLDocumentDecorator doc) throws Exception {
		for (Node n : doc.getNodes()) {
			if (n!=null && !doc.isEmptyText(n)) session.insert(n);
		}
		
		return (T) this;
	}
	
	public T insert(Object fact) {
		session.insert(fact);
		return (T) this;
	}

	public boolean failed() {
		int errors = 0;
		QueryResults results = session.getQueryResults("result");
		if (results.size() == 0) { // nothing done
			logger.error("rule ran successfully but with no actions");
			errors++;
		} else for ( QueryResultsRow row : results ) {
		    ResultBean bean = (ResultBean) row.get( "result" );
		    if ("error".equalsIgnoreCase(bean.level )) errors++;
		    logger.debug("rule run result: " + bean );
		}
		
		return (errors > 0);
	}
	
	public T withGlobal(String key, Object value) {
		session.setGlobal(key, value);
		return (T) this;
	}
	
	public T build() {
		return (T) this;
	}
	
	public int fire() {
	    int i = session.fireAllRules();
	    return i;
	}
	
	public void dispose() {
		if (kieLogger!=null) kieLogger.close();
		session.dispose();
	}
} 
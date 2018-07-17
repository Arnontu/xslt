package butters.rules2.map;

import javax.inject.Provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Node;

import butters.rules2.LeftNode;
import butters.rules2.RightNode;

@Service
public class NodeMappingService {
	
	@Autowired Provider<RulesRunner> runnerProvider;
	@Autowired Provider<RulesContext> contextProvider;
	
	public void map(Node src, Node trgt) {
		getRulesRunner()
			.withKnowledgeSesssion("MapKS")
			.debug(true)
			.withGlobal("$app", getRulesContext())
			.insert(new LeftNode(src))
			.insert(new RightNode(trgt))
			.fire();
	}
	
	
	private RulesRunner getRulesRunner() {
        return runnerProvider.get();
    }

	private RulesContext getRulesContext() {
        return contextProvider.get();
    }
}

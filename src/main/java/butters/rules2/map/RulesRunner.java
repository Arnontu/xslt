package butters.rules2.map;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import butters.rules2.AbstractRuleRunner;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RulesRunner extends AbstractRuleRunner<RulesRunner> {

}

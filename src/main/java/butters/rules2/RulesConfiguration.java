package butters.rules2;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RulesConfiguration {

	@Bean
    public KieContainer kieContainer() {
    	return KieServices.Factory.get().getKieClasspathContainer();
    }

}

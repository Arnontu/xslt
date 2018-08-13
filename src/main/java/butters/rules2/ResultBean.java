package butters.rules2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Dummy result object to return results, errors and Warnings raised by rules.
 * Use when there are no "natural" results and exceptions. 
 * severity: info, warn, error
 */
public class ResultBean {
	
	String level; 
	String message;

	static Logger logger = LoggerFactory.getLogger(ResultBean.class);
	
	public ResultBean(String severity, String message) {
		super();
		this.level = severity;
		this.message = message;
		logger.info("result: [" + severity + "] " + message);
	}

	public String getMessage() {
		return message;
	}

	public String getLevel() {
		return level;
	}

	@Override
	public String toString() {
		return ("{ level: " + level + ", message: " + message + "}");
	}

}

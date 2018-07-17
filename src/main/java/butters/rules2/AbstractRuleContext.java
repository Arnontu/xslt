package butters.rules2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import butters.xml.XMLDocumentDecorator;
import butters.xml.XSLTEditor;

public abstract class AbstractRuleContext {
	
	@Autowired XSLTEditor editor; 
	protected Logger logger = LoggerFactory.getLogger("butters.rules2.run");

	public void replaceWithvalueOf(Node target, Node source) {
		editor.replaceWithValueof(target, source);
	}
	
//	public void setAttributeValue(Node parentElmNode, String attr,  String value) {
//		
//	}

	public Logger getLogger() {
		return logger;
	}

}

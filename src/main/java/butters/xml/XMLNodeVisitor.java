package butters.xml;

import org.w3c.dom.Node;

public interface XMLNodeVisitor {
	public void accept(long ordinal, Node node);
}

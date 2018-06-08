package butters.xml;

import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

import org.w3c.dom.Document;

public class NamespaceResolver implements NamespaceContext {
	
	private Document doc;

	public NamespaceResolver(Document document) {
		this.doc = document;
	}

	public String getNamespaceURI(String prefix) {
		if (prefix.equals(XMLConstants.DEFAULT_NS_PREFIX)) {
			return doc.lookupNamespaceURI(null);
		} else {
			return doc.lookupNamespaceURI(prefix);
		}
	}

	public String getPrefix(String namespaceURI) {
		return doc.lookupPrefix(namespaceURI);
	}

	@SuppressWarnings("rawtypes")
	public Iterator getPrefixes(String namespaceURI) {
		return null;
	}
}

package butters.xml;

import java.io.OutputStream;

import javax.xml.transform.Source;

import org.springframework.stereotype.Service;
import org.w3c.dom.Node;

import net.sf.saxon.s9api.DOMDestination;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.XdmDestination;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;
import net.sf.saxon.s9api.XsltTransformer;
import net.sf.saxon.trace.XQueryTraceListener;

@Service
public class XSLTTransform {

	public void process(Source tmplsource, Source docsource, OutputStream out) throws SaxonApiException {
        Processor proc = new Processor(false);
        XsltCompiler comp = proc.newXsltCompiler();
        XsltExecutable exp = comp.compile(tmplsource);
        XsltTransformer trans = exp.load();
        trans.setSource(docsource);
        XdmDestination dest = new XdmDestination(); // TODO streaming?
        trans.setDestination(dest);
        trans.transform();
        print(out, dest.getXdmNode(), proc);
	}


	protected void print(OutputStream out, XdmNode root, Processor proc) throws SaxonApiException {
		Serializer ser = proc.newSerializer(out);
        ser.setOutputProperty(Serializer.Property.METHOD, "xml");
        ser.setOutputProperty(Serializer.Property.INDENT, "yes");
        ser.serializeNode(root);
	}

}

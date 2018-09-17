package com.bar1g16;

import com.bar1g16.interfaces.*;
import net.sf.saxon.s9api.*;
import org.eclipse.rdf4j.query.algebra.Str;
import org.w3c.dom.Document;
import sun.tools.tree.NewArrayExpression;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

public class SaxonTransformer implements ITransformer {
    IDataLoader dataLoader;
    IDataStore dataStorage;

    public SaxonTransformer(IDataLoader IDataLoader, IDataStore dataStorage) {
        this.dataLoader = IDataLoader;
        this.dataStorage = dataStorage;
    }

    public void transform() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Processor processor = new Processor(false);
        XsltTransformer transformer = getTransformer(processor);
        Serializer result = processor.newSerializer(out);
        transformer.setDestination(result);
        try {
            transformer.transform();
        } catch (SaxonApiException e) {
            e.printStackTrace();
        }
        save(out);
    }

    private XsltTransformer getTransformer(Processor processor) {
        XsltCompiler styleSheetCompiler = processor.newXsltCompiler();
        XsltExecutable stylesheet = null;
        XdmNode src = null;
        try {
            stylesheet = styleSheetCompiler.compile(dataLoader.getStyleSheet());
            src = processor.newDocumentBuilder().build(dataLoader.getXMLData());
        } catch (SaxonApiException e) {
            e.printStackTrace();
        }
        XsltTransformer transformer = stylesheet.load();
        transformer.setInitialContextNode(src);
        return transformer;

    }

    private void save(ByteArrayOutputStream out) {
        dataStorage.save(out);
    }

}

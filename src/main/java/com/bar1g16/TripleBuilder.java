package com.bar1g16;
import com.bar1g16.interfaces.IDataLoader;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;


/***
 * Injests plain XML data using an IDataLoader - a strategy for allowing us to use XML files or input streams,
 * The
 *
 */
public class TripleBuilder {
    // a representation of an xml document (org.w3c.dom.Document)
    private Document document;
    private File stylesheet;
    // private File datafile;
    // xml parser to create Document objects from files
    DocumentBuilderFactory docFactory;
    IDataLoader dataLoader;


    /***
     * constructor
     */
    public TripleBuilder(IDataLoader dataloader) {

        //I don't know why I am doing this yet, but it may come in handy...
        //using java.nio  code gives us other options for file manipulation later...
        // @ TODO: 23/01/2018 switch this to just use File if we don't need it als ship this out to fileIO
        //Path stylesheet_Nio_p = Paths.get("xslt/" + styleSheet);
        //stylesheet = stylesheet_Nio_p.toFile();
        //we are a triplebuilder - we don't give a shit about files anymore!
        this.dataLoader = dataloader;


    }

    public void convert() {
        try {
            document = dataLoader.getXMLDocument();
        } catch (IOException e) {

            //TODO: get shot of this stacktrace
            if (Prefs.printStackTraces) e.printStackTrace();
        }
    }

    public void getModel() {

        Transformer transformer = dataLoader.getStyleSheet();
        Document xmlDoc = null;

        try {
            xmlDoc = dataLoader.getXMLDocument();
        } catch (IOException e) {
            if (Prefs.printStackTraces) e.printStackTrace();
        }
        DOMSource source = new DOMSource(xmlDoc);
        StreamResult result = new StreamResult(new File("data-out/result.rdf"));
       // DOMResult result=new DOMResult();
        //SAXResult result=new SAXResult();
        try {

            transformer.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        Model m = ModelFactory.createDefaultModel();
        m.read("data-out/result.rdf");
        m.write(System.out);

    }

}

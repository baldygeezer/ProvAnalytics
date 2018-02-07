package com.bar1g16;

import com.bar1g16.interfaces.IDataLoader;
import com.sun.xml.internal.stream.buffer.XMLStreamBufferResult;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import java.nio.file.*;

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
    IDataLoader fileLoader;


    /***
     * constructor
     * @param datapath      A string representing the path to the data directory
     * @param styleSheet    A string representing the name of the stylesheet file in the data directory
     */
    public TripleBuilder(String datapath, String styleSheet) throws FileNotFoundException {

        //I don't know why I am doing this yet, but it may come in handy...
        //using java.nio  code gives us other options for file manipulation later...
        // @ TODO: 23/01/2018 switch this to just use File if we don't need it als ship this out to fileIO
        //Path stylesheet_Nio_p = Paths.get("xslt/" + styleSheet);
        //stylesheet = stylesheet_Nio_p.toFile();
        this.fileLoader = new FileLoader(datapath, styleSheet);


    }

    public void convert(String dataFile) {
        try {
            document = fileLoader.getXMLDocument(dataFile);
        } catch (IOException e) {
            System.err.println("The input xml file could not be loaded. We were looking in " + fileLoader.getDataSourceInfo());
            //TODO: get shot of this stacktrace
            if (Prefs.printStackTraces) e.printStackTrace();
        }
    }

    public void getModel() {


        Transformer transformer = fileLoader.getStylesheet();
        Document xmlDoc = null;

        try {
            xmlDoc = fileLoader.getXMLDocument("testfixture.osm");
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
        m.write(System.out, "TURTLE");

    }

}

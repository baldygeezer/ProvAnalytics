package com.bar1g16;

import com.bar1g16.interfaces.IDataLoader;
import com.bar1g16.interfaces.IDataStore;
import com.bar1g16.interfaces.ITransformer;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;


/***
 * Ingests plain XML data using an IDataLoader - a strategy for allowing us to use XML files or input streams,
 * The
 *
 */
public class TripleBuilder implements ITransformer {
    // a representation of an xml document (org.w3c.dom.Document)
    private Document document;
    private File stylesheet;
    // private File datafile;
    // xml parser to create Document objects from files
    DocumentBuilderFactory docFactory;
    IDataLoader dataLoader;
    IDataStore dataStorage;

    /***
     * constructor
     */
    public TripleBuilder(IDataLoader dataLoader, IDataStore dataStorage) {

        //I don't know why I am doing this yet, but it may come in handy...
        //using java.nio  code gives us other options for file manipulation later...
        // @ TODO: 23/01/2018 switch this to just use File if we don't need it als ship this out to fileIO
        //Path stylesheet_Nio_p = Paths.get("xslt/" + styleSheet);
        //stylesheet = stylesheet_Nio_p.toFile();
        //we are a triplebuilder - we don't care about files any more!
        this.dataLoader = dataLoader;
        //we also don't care about data persistence
        this.dataStorage = dataStorage;


    }

    public void convert() {
        try {
            document = dataLoader.getXMLDocument();
        } catch (IOException e) {

            //TODO: get shot of this stacktrace
            if (Prefs.printStackTraces) e.printStackTrace();
        }
    }
    @Override
    public void transform() {

        Transformer transformer = dataLoader.getStyleSheet();
        Document xmlDoc = null;

        try {
            xmlDoc = dataLoader.getXMLDocument();
        } catch (IOException e) {
            if (Prefs.printStackTraces) e.printStackTrace();
        }
        DOMSource source = new DOMSource(xmlDoc);

         /* @TODO this should not be here
         write the resylt to file. This needs to be replaced with an object that persists the data
          */
       // Result result = new StreamResult(new File("data-out/result.rdf"));
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
       // BufferedOutputStream o = new BufferedOutputStream(byteArrayOutputStream);
        Result result = new StreamResult(byteArrayOutputStream);

       // StringWriter stringWriter=new StringWriter();
       // Result result = new StreamResult(stringWriter);
       // Result result = dataStorage.getResult();
        // DOMResult result1=new DOMResult();
        //SAXResult result=new SAXResult();
        try {

            transformer.transform(source,result);


        } catch (TransformerException e) {
            e.printStackTrace();
        }

        /**
         * We wrote out our data to a byte array output stream
         * We will pass this to the IDataStores, which no longer care about transformer results
         * ...or we will find something better than OutputBuffers
         */


        //s=stringWriter.toString();
        dataStorage.save(byteArrayOutputStream);

      ///  Model m = ModelFactory.createDefaultModel();
      //  m.read("data-out/result.rdf");
      //  m.write(System.out);

    }

}

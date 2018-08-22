package com.bar1g16;


import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        boolean printStacktraces = false;
        // Wibble wobble = new Wibble();
        // wobble.doTheWibblyThing();
//        FileLoader f = null;
//        try {
//            f = new FileLoader("xslt/", "xmlToRDF.xsl");
//        } catch (FileNotFoundException e) {
//            System.out.println("whoopsy we cant file a file: "+e.getMessage());
//            if (printStacktraces) e.printStackTrace();
//        }
//        Transformer t = f.getStylesheet();
//        Document d = null;
//
//        try {
//            d = f.getXMLDocument("testfixture.osm");
//        } catch (IOException e) {
//            if (printStacktraces) e.printStackTrace();
//        }
//        DOMSource source = new DOMSource(d);

        //        StreamResult result = new StreamResult(System.out);
//        try {
//            t.transform(source, result);
//        } catch (TransformerException e) {
//            e.printStackTrace();
//        }

FileLoader loader=null;
        try {
             loader = new FileLoader("testfixture.osm", "OshToOwl.xsl");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        TripleBuilder t = null;
            t = new TripleBuilder(loader);

        t.getModel();
    }


}

package com.bar1g16;


import org.w3c.dom.Document;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
       // Wibble wobble = new Wibble();

        //wobble.doTheWibblyThing();
        FileLoader f = null;
        try {
            f = new FileLoader("xslt/", "xmlToRDF.xsl");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Transformer t = f.getStylesheet();
        Document d = null;

        try {
            d = f.getXMLDocument("testfixture.osm");
        } catch (IOException e) {
            e.printStackTrace();
        }
        DOMSource source = new DOMSource(d);
        StreamResult result = new StreamResult(System.out);
        try {
            t.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }


}

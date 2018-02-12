package com.bar1g16;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class FileLoader extends FileIO {
    private DocumentBuilderFactory docBuilderFactory;
    private DocumentBuilder docBuilder;
    private final File xsl;
    private StreamSource xslt;
    //private static Document document;
    private Transformer transformer;


    /***
     *
     * @param dataPath path to a directory containing the xsl and xml files
     * @param xslt name of the xsl file
     */
    public FileLoader(String dataPath, String xslt) throws FileNotFoundException {
        super(dataPath, xslt);
        //get the XSD transformation as a Transformer object, store it, throwing exception if we can't
        //find the xsl file
        docBuilderFactory = DocumentBuilderFactory.newInstance();
        xsl = new File(dataPath + xslt);
        setStyleSheet();

        try {
            docBuilder = docBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
           // e.printStackTrace();
        }
    }

    /**
     * ingests an XML file. returns w3 xml document object
     *
     * @param filename
     * @return
     */
    @Override
    public Document getXMLDocument(String filename) throws IOException {
        //make a document builder object to parse the xml source
        File file = new File(super.getDataPath() + filename);

        Document document = null;
        try {
            document = docBuilder.parse(file);
        } catch (SAXException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        return document;
    }

    /***
     * todo cleanup debug code
     * Provides a Transformer loaded with a stylesheet
     * @return
     */
    @Override
    public Transformer getStylesheet() {


        //Object o = transformerFactory.getAttribute("match");
        return transformer;


    }

    private boolean setStyleSheet() throws FileNotFoundException {
        StreamSource xslSource = new StreamSource(xsl);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        boolean result = true;
        try {
            transformer = transformerFactory.newTransformer(xslSource);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
            throw new FileNotFoundException("We were unable to locate a valid stylesheet. Please ensure there is a valid xsl file " +
                    "named: "
                    + xsl.getName() + " located in the directory: " + super.getDataPath());
        } finally {
            result= false;

        }
        return result;

    }

}

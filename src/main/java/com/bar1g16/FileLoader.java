package com.bar1g16;

import com.bar1g16.interfaces.IDataLoader;
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


public class FileLoader implements IDataLoader {
    private DocumentBuilderFactory docBuilderFactory;
    private DocumentBuilder docBuilder;
    // private final File xsl;
    //private StreamSource xslt;
    //private static Document document;
    private Transformer transformer;
    private Document xmlDocument;
    public static final String DATAPATH = "data-in/";
    public static final String XSDPATH = "xslt/";


    /***
     * @param xsltFilename name of the xsl file
     * @param dataFileName name of the data file
     */
    public FileLoader(String dataFileName, String xsltFilename) throws IOException, SAXException {

        //get the XSD transformation as a Transformer object, store it, throwing exception if we can't
        //find the xsl file
        docBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            docBuilder = docBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            // e.printStackTrace();
        }

        setStyleSheet(xsltFilename);
        setXMLDocument(dataFileName);

    }


    /**
     * get a w3 xml document object
     *
     * @return
     */
    @Override
    public Document getXMLDocument() {
        return xmlDocument;
    }

    /***
     * todo cleanup debug code
     * Provides a Transformer loaded with a stylesheet
     * @return
     */
    @Override
    public Transformer getStyleSheet() {
        //Object o = transformerFactory.getAttribute("match");
        return transformer;
    }

    /**
     * ingests an XML file (geographic data version history). initialises a w3 xml document object
     */
    private Document setXMLDocument(String XmlFileName) throws IOException, SAXException {

        File file = new File(DATAPATH + XmlFileName);
        Document document = null;
        document = docBuilder.parse(file);


        return document;
    }

    private boolean setStyleSheet(String xsltFileName) throws FileNotFoundException {
        File xsl = new File(XSDPATH + xsltFileName);
        StreamSource xslSource = new StreamSource(xsl);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        boolean result = true;
        try {
            transformer = transformerFactory.newTransformer(xslSource);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
            throw new FileNotFoundException("We were unable to locate a valid stylesheet. Please ensure there is a valid xsl file " +
                    "named: "
                    + xsl.getName() + " located in the directory: " + XSDPATH);
        } finally {
            result = false;

        }
        return result;

    }


}

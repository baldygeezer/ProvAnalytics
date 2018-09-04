package com.bar1g16;

import com.bar1g16.interfaces.IDataLoader;
import com.bar1g16.interfaces.IFileIO;
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
    private Transformer transformer;
    private Document xmlDocument;
    private IFileIO files;


    /***
     * @param files an object that gets the required files
     */
    public FileLoader( IFileIO files) throws IOException, SAXException {
this.files=files;
        //get the XSD transformation as a Transformer object, store it, throwing exception if we can't
        //find the xsl file
        docBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            docBuilder = docBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
             e.printStackTrace();
        }

        setStyleSheet();
       xmlDocument=setXMLDocument();

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
    private Document setXMLDocument() throws IOException, SAXException {
        File file = files.getDataFile();
        Document document = null;
        document = docBuilder.parse(file);

        return document;
    }

    private boolean setStyleSheet() throws FileNotFoundException {
        //File xsl = new File(XSDPATH + xsltFileName);
        File xsl = files.getStyleSheet();
        StreamSource xslSource = new StreamSource(xsl);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        boolean result = true;
        try {
            transformer = transformerFactory.newTransformer(xslSource);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
            throw new FileNotFoundException("We were unable to locate a valid stylesheet. Please ensure there is a valid xsl file " +
                    "named: "
                    + xsl.getName() + " located in the directory: " + files.getStyleSheetPath());
        } finally {
            result = false;

        }
        return result;

    }


}

package com.bar1g16;

import com.bar1g16.interfaces.IDataLoader;
import com.bar1g16.interfaces.IFileIO;
import com.bar1g16.interfaces.IDataLoader;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;

/**
 * class to load osm data from file
 */
public class FileLoader implements IDataLoader {
    private DocumentBuilderFactory docBuilderFactory;
    private DocumentBuilder docBuilder;
    private Transformer transformer;
    private Document xmlDocument;
    private IFileIO files;



    public FileLoader(IFileIO files)  {
        this.files = files;
        //get the XSD transformation as a Transformer object, store it, throwing exception if we can't
        //find the xsl file
//        docBuilderFactory = DocumentBuilderFactory.newInstance();
//        try {
//            docBuilder = docBuilderFactory.newDocumentBuilder();
//        } catch (ParserConfigurationException e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public StreamSource getStyleSheet() {
        return new StreamSource(files.getStyleSheet());
    }

    @Override
    public StreamSource getXMLData() {
        return new StreamSource(files.getDataFile());
    }
}

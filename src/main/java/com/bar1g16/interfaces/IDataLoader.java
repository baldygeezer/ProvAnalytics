package com.bar1g16.interfaces;
import org.w3c.dom.Document;
import javax.xml.transform.Transformer;
import java.io.IOException;

/**
 * Interface for loading data to carry out the processing
 * The implementing classes should be able to ingest XML data and an XSLT stylesheet
 * and supply javax xml Transformer object and w3c xml document object that can be
 * used to create RDF Triples. details about how the data is ingested are left to concrete classes
 */
public interface IDataLoader {
    /**
     * @return w3c xml document object with the data to be analysed
     * @throws IOException
     */
    Document getXMLDocument() throws IOException;

    /**
     * @return string - mesage with information about the data source
     * e.g. a file location or filestream info
     */
    //String getDataSourceInfo();

    /**
     * @return a Transformer object containing the stylesheet information
     */
    Transformer getStyleSheet();

}

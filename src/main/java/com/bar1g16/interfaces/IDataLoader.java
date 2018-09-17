package com.bar1g16.interfaces;
import org.w3c.dom.Document;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;

/**
 * Interface for loading data to carry out the processing
 * The implementing classes should be able to ingest XML data and an XSLT stylesheet
 * and supply javax xml StreamSource objects that can be used to create RDF Triples.
 * details about how the data is ingested are left to concrete classes
 */
public interface IDataLoader {
    /**
     * @return StreamSource object holding the data to be analysed
     * @throws IOException
     */
    StreamSource getXMLData();

    /**
     * @return string - mesage with information about the data source
     * e.g. a file location or filestream info
     */
    //String getDataSourceInfo();

    /**
     * @return a Streamsource object containing the stylesheet information
     */
    StreamSource getStyleSheet();

}

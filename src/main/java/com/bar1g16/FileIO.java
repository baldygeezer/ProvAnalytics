package com.bar1g16;
import com.bar1g16.interfaces.IDataLoader;
import org.w3c.dom.Document;

import java.io.IOException;

/***
 * Handles file IO. Ingests an xslt stylesheet and xml file. Outputs objects required to apply the xslt to the xml
 */
public abstract class FileIO implements IDataLoader {
    private String dataPath;

    @Override
    public abstract Document getXMLDocument(String Filename) throws IOException;

    public FileIO(String dataPath, String xsltDocument) {
        this.dataPath = dataPath;
    }

    public String getDataPath() {
        return dataPath;
    }
    @Override
    public String getDataSourceInfo() {

        return dataPath;
    }
}

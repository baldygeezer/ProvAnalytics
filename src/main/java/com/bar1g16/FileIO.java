package com.bar1g16;
import com.bar1g16.interfaces.IDataLoader;
import org.w3c.dom.Document;

import java.io.IOException;

/***
 * Handles ingests information using file IO. Ingests an xslt stylesheet and xml file. Outputs objects required to apply the xslt transformation to the xml
 * Abstract so that specifics about how the files are handled, and where they come from can be delegated to a concrete class
 */
public abstract class FileIO implements IDataLoader {
    private String dataPath;
    private String  fileName;

    @Override
    public abstract Document getXMLDocument() throws IOException;

    public FileIO(String dataPath, String fileName) {
        this.dataPath = dataPath;
        this.fileName = fileName;
    }

    public String getDataPath() {
        return dataPath;
    }

    //@Override
    public String getDataSourceInfo() {

        return dataPath+fileName;
    }


}

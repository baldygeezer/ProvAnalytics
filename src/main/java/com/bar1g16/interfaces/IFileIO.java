package com.bar1g16.interfaces;

import java.io.File;

public interface IFileIO  {

    File getStyleSheet(String xsdFileName);
    File getDataFile(String dataFileName);
    File getStyleSheet();
    File getDataFile();

    boolean saveRDF(File rdfFile);
    String getDataPath();
    String getStyleSheetPath();
}

package com.bar1g16;

import com.bar1g16.interfaces.IDataStore;
import com.bar1g16.interfaces.IFileIO;

import javax.xml.transform.Result;
import java.io.File;
import java.util.Scanner;

/***
 * Handles/ingests information using file IO. Ingests an xslt stylesheet and xml file. Outputs objects required to apply the xslt transformation to the xml
 * Abstract so that specifics about how the files are handled, and where they come from can be delegated to a concrete class
 * saves results to file
 */
public class FileIO implements IFileIO {
    private String dataFileName;
    private String styleSheetName;
    private String dataPath = "data-in/";
    private String xslPath = "xslt/";
    private String savePath = "data-out/";
    private File dataFile;
    private File styleSheet;

    /**
     * constuctor for default file locations. Filenames will need to be specified when getting files.
     */
//    public FileIO() {
//    }

    /**
     * Constructor to use custom file locations
     *
     * @param dataPath path to the data file location
     * @param xslPath  path to the xslt stylesheet file
     * @param savePath path to save rdf files to
     */
    public FileIO(String dataPath, String xslPath, String savePath, String dataFileName, String styleSheetName) {
        this(dataFileName, styleSheetName);
        this.dataPath = dataPath;
        this.xslPath = xslPath;
        this.savePath = savePath;
    }

    /**
     * constructor specifying the name of files. Use no arguments when getting files
     * NB we change this to not append the path to the input folder, so that we can use multi file loaders
     * @toDo look at better way of doing file locations
     * @param dataFileName path to  the data file
     * @param styleSheetName name of the stylesheet
     */
    public FileIO(String dataFileName, String styleSheetName) {
        this.dataFileName = dataFileName;
        this.styleSheetName = styleSheetName;
        dataFile = getFile( this.dataFileName);
        styleSheet = getFile(xslPath + this.styleSheetName);
    }

    @Override
    public File getStyleSheet(String xslFileName) {
        return getFile(xslPath + xslFileName);
    }

    @Override
    public File getDataFile(String dataFileName) {
        return getFile(dataPath + dataFileName);
    }

    @Override
    public File getStyleSheet() {
        return styleSheet;
    }

    @Override
    public File getDataFile() {
        return dataFile;
    }

    @Override
    public boolean saveRDF(File rdfFile) {
        return false;
    }

    @Override
    public String getDataPath() {
        return dataPath;
    }

    @Override
    public String getStyleSheetPath() {
        return xslPath;
    }

    private File getFile(String path) {
        File file = new File(path);
        checkfile(file);
        return file;
    }


    private void checkfile(File file) {
        if (!file.isFile() && !file.canRead() && !file.exists()) {
            System.err.println("The file: \"" + file.getAbsolutePath() + "\" is not useable by the system. It either does not exist, or is otherwise unreadable. ");
            System.err.println("press \"q\" and then \"enter\" to take this program outside and shoot it");
            System.err.println("press \"r\" and then \"enter\" to run it again");

            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                switch(scanner.nextLine()){
                    case"q": System.exit(1);
                        break;
                    case "r": Main.start(1);
                }
            }
        } else if (file.length() < 100) {
            System.err.println("warning: The file " + file.getAbsolutePath() + " does not contain much content and may produce unexpected results");
        }

    }


}

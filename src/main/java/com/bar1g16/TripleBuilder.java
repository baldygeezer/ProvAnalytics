package com.bar1g16;

import com.bar1g16.interfaces.IDataLoader;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.nio.file.*;

import java.io.File;

/***
 * Injests plain XML data using an IDataLoader - a strategy for allowing us to use XML files or input streams,
 * The
 *
 */
public class TripleBuilder {
    // a representation of an xml document (org.w3c.dom.Document)
    private Document document;
    private File stylesheet;
    // private File datafile;
    // xml parser to create Document objects from files
    DocumentBuilderFactory docFactory;
    IDataLoader fileLoader;


    /***
     * constructor
     * @param fileLoader     A string representing the name of an input file in the data directory
     * @param inputFile    A string representing the name of the outPut file in the data directory
     * @param styleSheet    A string representing the name of the stylesheet file in the data directory
     */
    public TripleBuilder(IDataLoader fileLoader, String inputFile, String styleSheet) {

        //I don't know why I am doing this yet, but it may come in handy...
        //using java.nio  code gives us other options for file manipulation later...
        // @ TODO: 23/01/2018 switch this to just use File if we don't need it als ship this out to fileIO
        Path stylesheet_Nio_p = Paths.get("xslt/" + styleSheet);
        stylesheet = stylesheet_Nio_p.toFile();
        this.fileLoader = fileLoader;


    }

    public void convert(String dataFile) {
        try {
            document = fileLoader.getXMLDocument(dataFile);
        } catch (IOException e) {
            System.err.println("The input xml file could not be loaded. We were looking in " + fileLoader.getDataSourceInfo());
            //TODO: get shot of this stacktrace
            e.printStackTrace();
        }
    }


}

package com.bar1g16;

import com.bar1g16.interfaces.IDataLoader;
import com.bar1g16.interfaces.IFileIO;

import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.bar1g16.interfaces.ITransformer;
import net.sf.saxon.s9api.SaxonApiException;
import org.apache.jena.dboe.migrate.L;
import org.eclipse.rdf4j.query.algebra.Str;


public class MultiFileLoader {
    private Logster log;

    public static void main(String[] args) {

        MultiFileLoader m = new MultiFileLoader();
        //m.getFiles("data-in/cleanCset");
        m.start();


//        for (int ctr = 1; ctr < 4; ctr++) {
//
//            m.log.log(Level.INFO, ctr + " potato ");
//        }
    }

    public MultiFileLoader() {
        this.log = Logster.getInstance();
    }

    public void start() {
        //ArrayList<String> fileList = getFiles();
    //  processFiles("/sotonLatest/7", "toRDF.xsl", "sotonLatest", "history");
        processFiles("/sotonLatestCleanCset", "changesets2rdf.xsl", "sotonLatest", "changesets");
   //     processFiles("/cleanCset", "changesets2rdf.xsl", "sotonrdfs", "changesets");
        // processFiles("/cleanSotonHist", "toRDF.xsl", "sotonData_RL-OPT", "history");
       // processFiles("/cleanCset/fails1", "changesets2rdf.xsl", "test", "changesets");


////        print out the list of failures and copy the failed files to a subdir
//        int ctr = 0;
//        ArrayList<String> fails = getFailedFiles();
//        for (String s : fails) {
//            System.out.println(s);
//            ctr++;
//        }
//        System.out.println(ctr);
//
   }


    /**
     * //     * get a list of all the files in a directory
     * //     * @param dirLoc the directory to look in
     * //     * @return
     * //
     */
    private ArrayList<String> getFiles(String dirLoc) {
        File dir = new File("data-in" + dirLoc);
        ArrayList<String> files = new ArrayList<>();
        File[] fileList = dir.listFiles();

        for (File file : fileList) {
            if (!file.isHidden()) {
                files.add("data-in" + dirLoc + "/" + file.getName());
            }
        }
        return files;
    }


    /**
     *      get a list of all the failed files from the log;
     *      copy them into a 'fail' folder
     *
     * //     * @return
     * //
     */
    private ArrayList<String> getFailedFiles() {
        ArrayList<String> files = new ArrayList<>();
        File logFile = new File("fileLog.txt");
        String line;
        String failDirLoc;
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(logFile));
            while ((line = fileReader.readLine()) != null) {
                //split each line on the colon
                String[] lineList = line.split(":");
                //if the line is logging a failed file...
                if (lineList[1].equals(" transforming file failed")) {
                    //...strip trailing whitespace and add it to the list
                    String filePath = lineList[2].trim();
                    files.add(filePath);

                    //break up the uri into a list by separating on the "\"
                    String[] fileLocParts = filePath.split("/");

                    //get the filename (string after the last /)
                    String fileName = fileLocParts[fileLocParts.length - 1];

                    //replace the filename with the name of a subdirectory that we copy the files to
                    fileLocParts[fileLocParts.length - 1] = "fails";
                    failDirLoc = "";

                    //make it back into a string uri pointing at the subdirectory
                    for (String s : fileLocParts) {
                        failDirLoc += s + "/";
                    }
                    //get an nio path with it
                    Path failDirPath = Paths.get(failDirLoc);

                    //if we didn't already make a subfolder, make one
                    if (!Files.isDirectory(failDirPath)) {
                        Files.createDirectory(failDirPath);
                    }

                    //copy the file into it- the second arg is making the path to for the
                    // file by adding the filename to the directory url
                    Files.copy(Paths.get(filePath), failDirPath.resolve(fileName));

                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return files;
    }


    /**
     * This method processes the files in a directory and sends them to the triplestore
     *
     * @param dirLoc         path to the folder containing the files
     * @param styleSheetName name of the stylesheet file (must be in the xslt directory)
     * @param repoName       name of the repo in GraphDB
     * @param graphSuffix    the last part of the named graph which the data will be added to
     *                       (this is appended to 'http://osm.osmd.org/')
     */

    private void processFiles(String dirLoc, String styleSheetName, String repoName, String graphSuffix) {
        ITransformer t = null;
        ArrayList<String> fileLocations = new ArrayList<>();
        //if we are importing files that have failed on the first run...
//        if (failedFiles) {
//            //...call the no arg version of get files which scrapes the log for file failures
//            fileLocations = getFiles();
//        } else {
//            //if this is a first run - just scour the directory and import
        fileLocations = getFiles(dirLoc);
//        }

        int ctr = 1;
        for (String fileLocation : fileLocations) {
            IDataLoader loader = new FileLoader(new FileIO(fileLocation, styleSheetName));
            t = new SaxonTransformer(loader, new GraphdbStore(repoName, "http://osm.osmd.org/" + graphSuffix));
            try {
                log.log(Level.INFO, "transforming file: " + fileLocation + " (file #" + ctr + " of " + fileLocations.size() + ")");
                t.transform();
                ctr++;

            } catch (SaxonApiException e) {
                log.log(Level.INFO, "transforming file failed: " + fileLocation);
                //e.printStackTrace();
            } catch (Exception e) {
                ctr++;
                log.log(Level.INFO, "transforming file failed: " + fileLocation);
                e.printStackTrace();
            }
        }


    }


}

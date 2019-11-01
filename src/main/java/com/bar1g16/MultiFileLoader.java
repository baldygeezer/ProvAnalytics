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
        // processFiles("/cleanCset", "changesets2rdf.xsl", "sotonData_RL-OPT", "changesets");
        processFiles("/cleanHampCset", "changesets2rdf.xsl", "sotonData_RL-OPT", "changesets", true);
        // processFiles("/cleanSotonHist", "toRDF.xsl", "sotonData_RL-OPT", "history");
    }

//    /**
//     * get a list of all the files in a directory
//     * @param dirLoc the directory to look in
//     * @return
//     */
//    private ArrayList<String> getFiles(String dirLoc) {
//
//        Path dirPath = Paths.get("data-in" + dirLoc);
//        ArrayList<String> fileLocList = new ArrayList<>();
//        try (Stream<Path> walk = Files.walk(dirPath)) {
//            fileLocList = walk.filter(Files::isRegularFile).map(x -> x.toFile().toString()).collect(Collectors.toCollection(ArrayList::new));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        //get rid of Mac OS filesystem crap
//        // @todo Windows?
//        for (String str : fileLocList) {
//            if (str.contains(".DS_Store")) {
//               fileLocList.remove(str);
//            }
//        }
//
//        return fileLocList;
//
//    }

    /**
     * //     * get a list of all the files in a directory
     * //     * @param dirLoc the directory to look in
     * //     * @return
     * //
     */
    private ArrayList<String> getFiles(String dirLoc) {
        File dir = new File("data-in" + dirLoc);
        // dir.setReadable(true);
        // System.out.println(dir.length());
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
     * //     * get a list of all the failed files from the log
     * //     * @param dirLoc the directory to look in
     * //     * @return
     * //
     */
    private ArrayList<String> getFiles() {

        ArrayList<String> files = new ArrayList<>();
       File logFile=new File ("fileLog.txt") ;
       String line;
        try {
            BufferedReader fileReader=new BufferedReader(new FileReader(logFile));
            while ((line = fileReader.readLine()) != null){
                //do your shit wit da string...
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }


        return files;
    }

    /**
     * This method processes the files in a directory and sends them to the triplestore
     *
     * @param failedFiles    whether we are reprocessing files wot failed
     * @param dirLoc         path to the folder containing the files
     * @param styleSheetName name of the stylesheet file (must be in the xslt directory)
     * @param repoName       name of the repo in GraphDB
     * @param graphSuffix    the last part of the named graph which the data will be added to
     *                       (this is appended to 'http://osm.osmd.org/')
     */

    private void processFiles(String dirLoc, String styleSheetName, String repoName, String graphSuffix, boolean failedFiles) {
        ITransformer t = null;
        ArrayList<String> fileLocations=new ArrayList<>();
        //if we are importing files that have failed on the first run...
        if (failedFiles) {
            //...call the no arg version of get files which scrapes the log for file failures
            fileLocations = getFiles();
        } else {
            //if this is a first run - just scoure the directory and import
            fileLocations = getFiles(dirLoc);
        }

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
                log.log(Level.INFO, "transforming file failed: " + fileLocation);
            }
        }


    }



}

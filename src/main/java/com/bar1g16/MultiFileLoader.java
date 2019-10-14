package com.bar1g16;

import com.bar1g16.interfaces.IDataLoader;
import com.bar1g16.interfaces.IFileIO;

import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
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
private Logster log ;

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
        processFiles("/cleanCset", "changesets2rdf.xsl", "sotonData_RL-OPT", "changesets");
    }


    private ArrayList<String> getFiles(String dirLoc) {

        Path dirPath = Paths.get("data-in" + dirLoc);
        ArrayList<String> fileLocList = new ArrayList<>();
        try (Stream<Path> walk = Files.walk(dirPath)) {
            fileLocList = walk.filter(Files::isRegularFile).map(x -> x.toFile().toString()).collect(Collectors.toCollection(ArrayList::new));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileLocList;

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
        ArrayList<String> fileLocations = getFiles(dirLoc);

        for (String fileLocation : fileLocations) {
            IDataLoader loader = new FileLoader(new FileIO(fileLocation, styleSheetName));
            t = new SaxonTransformer(loader, new GraphdbStore(repoName, "http://osm.osmd.org/" + graphSuffix));
            try {
                log.log(Level.INFO,"transforming file: "+fileLocation);
                t.transform();

            } catch (SaxonApiException e) {
                log.log(Level.INFO,"transforming file failed: "+fileLocation);
                //e.printStackTrace();
            }

        }


        //  SaxonTransformer t = new SaxonTransformer()


    }
//        private void processHistory(String dirLoc) {
//            ITransformer t = null;
//            ArrayList<String> fileLocations = getFiles(dirLoc);
//
//            for (String fileLocation : fileLocations) {
//                IDataLoader changeSetLoader = new FileLoader(new FileIO(fileLocation, "changesets2rdf.xsl"));
//                t = new SaxonTransformer(changeSetLoader, new GraphdbStore("sotonData_RL-OPT", "http://osm.osmd.org/changesets"));
//                try {
//                    t.transform();
//                } catch (SaxonApiException e) {
//                    e.printStackTrace();
//                }
//
//            }

}

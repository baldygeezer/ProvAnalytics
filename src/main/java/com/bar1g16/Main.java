package com.bar1g16;

import com.bar1g16.interfaces.ITransformer;
import net.sf.saxon.s9api.SaxonApiException;

public class Main {

    public static void main(String[] args) {

       // start("csetsData.osm","osmHistData.osh","FullOSM","http://www.osm.org");
        start(7);
        //messing about with some rdf data to see what it looks like in graph db
        //  DeleteMeEventually pw = new DeleteMeEventually();
        // pw.print();
    }

    /**
     * graphDB must be running
     *
     * @param loadnum select an option for the analyser :
     *                1 to write result to file
     *                2 to send to graphDB's default repo
     *                3 to send to named repo
     *                4 to send changesets to repo
     *                any other number writes to file
     * @TODO fix catch block, exception handle for no comms with db
     */
    public static void start(int loadnum) {

        FileLoader loader = null;
        FileLoader sLoader = null;
        FileLoader changeSetLoader = null;


        loader = new FileLoader(new FileIO("data-in/osmHistData.osh", "toRDF.xsl"));
        changeSetLoader = new FileLoader(new FileIO("data-in/MiniCsets.osm", "changesets2rdf.xsl"));
        // sLoader=new FileLoader(new FileIO("testfixture.osm", "toRDF.xsl"));

        ITransformer t = null;
        // configurations which can be selected using ints in the start method
        switch (loadnum) {
            case 1:
                //send the result to a file
                t = new SaxonTransformer(loader, new FileStore("data-out/result.rdf"));
                break;
            case 2:
                //send the result to graph db using the default repo
                t = new SaxonTransformer(loader, new GraphdbStore());
                break;
            case 3:
                //send the result to graphDB using an unfortunately named repo
                t = new SaxonTransformer(loader, new GraphdbStore("testicle"));
                break;
            case 4:
                //send the result to graphDB using realOSM_RL-OPT repo and placing triples in a named graph
                t = new SaxonTransformer(loader, new GraphdbStore("realOSM_RL-OPT", "http://osm.osmd.org/dtatata"));
                break;
            case 5:
                //send the result (changesets) to file
                t = new SaxonTransformer(changeSetLoader, new FileStore("changesetResult.rdf"));
                break;
            case 6:
                //send the result to graphDB using realOSM_RL-OPT repo and placing triples in a named graph
                t = new SaxonTransformer(changeSetLoader, new GraphdbStore("realOSM_RL-OPT", "http://osm.osmd.org/changesets"));
                break;
           case 7:
//                //test a cleaned changeset
                t = new SaxonTransformer(new FileLoader(new FileIO("data-in/cleanHampCset/fails/chunk_1774.xml", "changesets2rdf.xsl")), new FileStore("changesetResult.rdf"));
                break;

            default:
                //same as 1
                t = new SaxonTransformer(loader, new FileStore("data-out/result.rdf"));
        }


        try {
            t.transform();

        } catch (SaxonApiException e) {
            e.printStackTrace();
        }
    }

    public static void start(String csetSourcFile, String historySourceFile, String repo, String graphname) {
        ITransformer historyTransformer = null;
        ITransformer csetTransformer = null;

        FileLoader cSetLoader = new FileLoader(new FileIO(csetSourcFile, "changesets2rdf.xsl"));
        FileLoader historyLoader = new FileLoader(new FileIO(historySourceFile, "toRDF.xsl"));

        GraphdbStore gdb = new GraphdbStore(repo, graphname);

        csetTransformer = new SaxonTransformer(cSetLoader, gdb);
        historyTransformer = new SaxonTransformer(historyLoader,gdb);
        try {
            csetTransformer.transform();
            historyTransformer.transform();

        } catch (SaxonApiException e) {
            e.printStackTrace();
        }
    }


}

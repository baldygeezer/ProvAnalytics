package com.bar1g16;
import com.bar1g16.interfaces.ITransformer;
import net.sf.saxon.s9api.SaxonApiException;

public class Main {

    public static void main(String[] args) {

        start(4);
        //messing about with some rdf data to see what it looks like in graph db
        DeleteMeEventually pw = new DeleteMeEventually();
       // pw.print();
    }

    /**
     * graphDB must be running
     * @TODO  fix catch block, exception handle for no comms with db
     * @param loadnum select an option for the analyser :
     *                1 to write result to file
     *                2 to send to graphDB's default repo
     *                3 to send to named repo
     *                any other number writes to file
     */
    public static void start(int loadnum) {

        FileLoader loader = null;
        FileLoader sLoader=null;

            loader = new FileLoader(new FileIO("osmHistData.osh", "toRDF.xsl"));
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
                //send the result to graphDB using an unfortunately named repo
                t = new SaxonTransformer(loader, new GraphdbStore("realOSM","http://osm.osmd.org/dtatata"));
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






    }

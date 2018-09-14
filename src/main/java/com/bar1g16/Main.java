package com.bar1g16;
import com.bar1g16.interfaces.ITransformer;
import net.sf.saxon.s9api.SaxonApiException;
import org.xml.sax.SAXException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        start(3);

    }

    public static void start(int loadnum) {

        FileLoader loader = null;
        S9FileLoader sLoader=null;
        try {
            loader = new FileLoader(new FileIO("testfixture.osm", "toRDF.xsl"));
            sLoader=new S9FileLoader(new FileIO("testfixture.osm", "toRDF.xsl"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        ITransformer t = null;
        switch (loadnum) {
            case 1:
                t = new TripleBuilder(loader, new FileStore("data-out/result.rdf"));
                break;
            case 2:
                t = new TripleBuilder(loader, new GraphdbStore());
                break;
            case 3:
                t = new SaxonTransformer(sLoader, new GraphdbStore());
                break;
            default:
                t = new TripleBuilder(loader, new FileStore("data-out/result.rdf"));
        }


        try {

            t.transform();

        } catch (SaxonApiException e) {
            e.printStackTrace();
        }
    }

    }

package com.bar1g16;
import com.bar1g16.interfaces.ITransformer;
import net.sf.saxon.s9api.SaxonApiException;
import org.xml.sax.SAXException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        start(1);

    }

    public static void start(int loadnum) {

        FileLoader loader = null;
        FileLoader sLoader=null;

            loader = new FileLoader(new FileIO("testfixture.osm", "toRDF.xsl"));
            // sLoader=new FileLoader(new FileIO("testfixture.osm", "toRDF.xsl"));


        ITransformer t = null;
        switch (loadnum) {
            case 1:
                t = new SaxonTransformer(loader, new FileStore("data-out/result.rdf"));
                break;
            case 2:
                t = new SaxonTransformer(loader, new GraphdbStore());
                break;
            default:
                t = new SaxonTransformer(loader, new FileStore("data-out/result.rdf"));
        }


        try {

            t.transform();

        } catch (SaxonApiException e) {
            e.printStackTrace();
        }
    }

    }

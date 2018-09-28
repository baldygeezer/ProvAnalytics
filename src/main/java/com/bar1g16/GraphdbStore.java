package com.bar1g16;
import com.bar1g16.interfaces.IDataStore;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.query.algebra.In;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.config.RepositoryConfig;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.repository.manager.LocalRepositoryManager;
import org.eclipse.rdf4j.repository.manager.RepositoryManager;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParser;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;

public class GraphdbStore implements IDataStore {

    private StringWriter stringWriter = new StringWriter();
    private StreamResult result;
    private Writer writer;
    private ByteArrayOutputStream out;
    private String db;
    private String repoName;

    /**
     * @return a reference to a sax result object that an XSLT Transformer writes data to
     */
    @Override
    public Result getResult() {
        if (result == null) {
            result = new StreamResult(stringWriter);
        }
        return result;
    }

//    public boolean save(ByteArrayOutputStream data,String db, String repoName) {
//        ByteArrayInputStream in = new ByteArrayInputStream(data.toByteArray());
//        RepositoryConnection conn = getConnection(db, repoName);
//        conn.begin();
////        URL url = null;
////        try {
////            url = new URL("https://www.ecs.soton.ac.uk/people/bar1g16/OSMProv#");
////        } catch (MalformedURLException e) {
////            e.printStackTrace();
////        }
//        try {
//            conn.add(in, "", RDFFormat.RDFXML);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        conn.commit();
//        return true;
//    }

    public boolean save(ByteArrayOutputStream data) {
        ByteArrayInputStream in = new ByteArrayInputStream(data.toByteArray());
        RepositoryConnection conn;
        if (repoName == null || db == null) {
            conn = getConnection();
        } else {
            conn = getConnection(repoName, db);
        }


        conn.begin();
//        URL url = null;
//        try {
//            url = new URL("https://www.ecs.soton.ac.uk/people/bar1g16/OSMProv#");
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
        try {
            conn.add(in, "", RDFFormat.RDFXML);
        } catch (IOException e) {
            e.printStackTrace();
        }
        conn.commit();
        return true;
    }

    /**
     * default constructor
     */
    public GraphdbStore() {
    }

    /**
     * constructor to specify the database and repo
     *
     * @param db       url of database server
     * @param repoName name of the GraphDB repo
     */
    public GraphdbStore(String db, String repoName) {
        this.db = db;
        this.repoName = repoName;
    }

    /**
     * constructor to specify just repo, using local database.
     *
     * @param repoName name of the GraphDB repo
     */
    public GraphdbStore(String repoName) {
        this.repoName = repoName;
        this.db = "http://localhost:7200/";
    }

    /**
     * get a connection to the repository
     *
     * @return an initialised rdf4j RepositoryConnection object, using default values
     */
    private RepositoryConnection getConnection() {
        RepositoryManager repoManager = new LocalRepositoryManager(new File("."));
        repoManager.initialize();
        Repository repo = new HTTPRepository("http://localhost:7200/", "test");
        repo.initialize();
        RepositoryConnection conn = repo.getConnection();
        return conn;
    }

    /**
     * get a connection to the repository
     *
     * @param db
     * @param repoName
     * @return an initialised rdf4j RepositoryConnection object, spefying the database and repository
     */
    private RepositoryConnection getConnection(String db, String repoName) {
        RepositoryManager repoManager = new LocalRepositoryManager(new File("."));
        repoManager.initialize();
        Repository repo = new HTTPRepository( repoName, db);
        repo.initialize();
        RepositoryConnection conn = repo.getConnection();
        return conn;
    }

    /**
     * experimental method
     *
     * @return
     */
    public OutputStream getOutputStream() {
        out = new ByteArrayOutputStream();

        return out;
    }

}

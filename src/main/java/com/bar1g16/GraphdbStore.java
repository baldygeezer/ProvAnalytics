package com.bar1g16;

import com.bar1g16.interfaces.IDataStore;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.config.RepositoryConfig;
import org.eclipse.rdf4j.repository.manager.LocalRepositoryManager;
import org.eclipse.rdf4j.repository.manager.RepositoryManager;

import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

public class GraphdbStore implements IDataStore {


    /**
     *
      * @return a reference to a sax result object that an XSLT Transformer writes data to
     */
    @Override
    public Result getResult() {
        RepositoryManager repoManager = new LocalRepositoryManager(new File("data-in/test-config.ttl"));
        Repository repo = repoManager.getRepository("test");
        repo.initialize();
      RepositoryConnection conn = repo.getConnection();
        StreamResult r=new StreamResult();


        return null;
    }





}

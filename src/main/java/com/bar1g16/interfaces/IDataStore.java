package com.bar1g16.interfaces;

import javax.xml.transform.Result;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;


public interface IDataStore  {

    /**
     * classes implement this to provide a specific result object for an XSLT transformer. the concrete implementation
     * can then decide whether the result should write to various database platforms or files etc.
     * This isn't complicated enough for factories yet...
     * @return a reference to a sax result object that an XSLT Transformer writes data to
     */
    Result getResult();
    boolean save(ByteArrayOutputStream b);
}

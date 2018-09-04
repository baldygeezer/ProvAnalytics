package com.bar1g16;
import com.bar1g16.interfaces.IDataStore;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.Writer;

public class FileStore implements IDataStore {

private StreamResult result;


    public FileStore(String filePath) {
        result = new StreamResult(new File(filePath));

    }


    @Override
    public Result getResult() {
        return result;
    }
}

package com.bar1g16;
import com.bar1g16.interfaces.IDataStore;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

public class FileStore implements IDataStore {

private StreamResult result;
private String filePath;

    public FileStore(String filePath) {
       // result = new StreamResult(new File(filePath));
        this.filePath=filePath;
    }


    @Override
    public Result getResult() {
        return result;
    }

    @Override
    public boolean save(ByteArrayOutputStream b) {
File file=new File(filePath);
        try {
            FileOutputStream out = new FileOutputStream(file);
            b.writeTo(out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }




}

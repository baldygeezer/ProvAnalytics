package com.bar1g16;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.*;

public class Logster {
    private static Logster logster = null;
    private File logFile;
    private FileWriter logWriter;
    private PrintWriter logPrinter;
    private Logger logger;



private  Logster(){
    this.logger = Logger.getLogger("com.bar1g16");

    FileHandler fileHandler=null;
    try {
        fileHandler= new FileHandler("fileLog.txt",true);
    } catch (IOException e) {
        e.printStackTrace();
    }

    fileHandler.setFormatter(new SimpleFormatter());
    logger.addHandler(fileHandler);
}



    public static Logster getInstance() {

        if (logster == null) {
            logster = new Logster();
        }

        return logster;
    }

    public void setWarningLevel(Level level) {
        logger.setLevel(level);
    }

    public void log(Level level, String eventMessage) {
        logger.log(level, eventMessage);
        //logger.info(eventMessage);
    }


}

package com.bar1g16;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.jena.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;

import java.io.InputStream;

public class Wibble {
    private String dPath = "data-in/";

    public void doTheWibblyThing() {
        Model model_from_file = ModelFactory.createDefaultModel();
        InputStream rdf_input = FileManager.get().open(dPath + "eswc-2006-09-21.rdf");

        model_from_file.read(rdf_input, null);
        //model_from_file.write(System.out);

        Model home_made_model = ModelFactory.createDefaultModel();


    }


}

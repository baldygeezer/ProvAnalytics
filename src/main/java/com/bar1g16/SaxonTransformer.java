package com.bar1g16;

import com.bar1g16.interfaces.*;

public class SaxonTransformer implements ITransformer {
    IDataLoader dataLoader;
    IDataStore dataStorage;

    public SaxonTransformer(IDataLoader dataLoader, IDataStore dataStorage) {
        this.dataLoader = dataLoader;
        this.dataStorage = dataStorage;
    }

    @Override
    public void transform() {

    }
}

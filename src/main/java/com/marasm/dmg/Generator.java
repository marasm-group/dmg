package com.marasm.dmg;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by vhq473 on 23.08.2016.
 */
public interface Generator
{
    void generate(ArrayList<DMGObject> objectList);
    void enableFeatures(Collection<Feature> features);
    void disableFeatures(Collection<Feature> features);

    void beginGeneration();
    void endGeneration(OutputStream stream) throws IOException;
}

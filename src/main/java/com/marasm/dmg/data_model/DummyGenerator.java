package com.marasm.dmg.data_model;

import com.marasm.dmg.DMGObject;
import com.marasm.dmg.Feature;
import com.marasm.dmg.Generator;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by vhq473 on 26.08.2016.
 */
class DummyGenerator implements Generator
{
    public void generate(ArrayList<DMGObject> objectList)
    {
        System.out.println("Parsed data:");
        for (DMGObject obj : objectList)
        {
            System.out.println(obj.name+":\n"+obj.toString(2));
        }
    }
    @Override
    public void enableFeatures(Collection<Feature> features) {}
    @Override
    public void disableFeatures(Collection<Feature> features){}
    @Override
    public void beginGeneration() {}
    @Override
    public void endGeneration(OutputStream stream) throws IOException {}
}
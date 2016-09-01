package com.marasm.dmg;

import org.json.JSONArray;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by vhq473 on 01.09.2016.
 */
public class DMGStructureGenerator implements Generator
{
    String s = "";
    @Override
    public void generate(ArrayList<DMGObject> objectList)
    {
        JSONArray arr = new JSONArray();
        for (DMGObject o : objectList)
        {
            Field f = new Field(o.name,Type.Object,o);
            arr.put(f.toJSON());
        }
        s += arr.toString(2);
    }
    @Override
    public void enableFeatures(Collection<Feature> features) {}

    @Override
    public void disableFeatures(Collection<Feature> features) {}

    @Override
    public void beginGeneration()
    {
        s += "// DMG Structure: \n\n";
    }

    @Override
    public void endGeneration(OutputStream stream) throws IOException {
        stream.write(s.getBytes());
    }
}

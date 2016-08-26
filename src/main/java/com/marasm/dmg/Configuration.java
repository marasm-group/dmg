package com.marasm.dmg;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.IllegalFormatException;

/**
 * Created by vhq473 on 26.08.2016.
 */
public class Configuration
{
    public static String file = null;
    public static String json = null;
    public static Collection<Feature> features = new ArrayList<>();

    public static String featuresDescription()
    {
        String out = "Available options:\n";
        for (Feature f : Feature.values())
        {
            out += "\t"+f.description()+"\n";
        }
        return out;
    }

    public static void setFeatures(String jsonStr)
    {
        if (jsonStr == null)
        {
            Log.e(new Configuration(),"string expected!");
            return;
        }
        try {
            JSONArray arr = new JSONArray(jsonStr);
            for (Object o : arr)
            {
                if (o instanceof String)
                {
                    String str = (String) o;
                    Feature f = Feature.fromString(str);
                    if(f == Feature.ERROR_FEATURE)
                    {
                        Log.e(new Configuration(),"Option '"+str+"' is not available!");
                        throw new Exception("Option '"+str+"' is not available!");
                    }
                    else
                    {
                        features.add(f);
                    }
                }else{ throw new Exception("Only strings are expected!");}
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            Log.e(new Configuration(),"illegal option string '"+jsonStr+"'");
            System.exit(-1);
        }
    }
}

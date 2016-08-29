package com.marasm.dmg;

import com.marasm.dmg.generators.csharp.CSharpGenerator;
import com.marasm.dmg.generators.java.JavaGenerator;
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
    static String[] langs = {"java","c#"};
    private static String lang = "java";
    public static String language(){return lang;}
    public static void setLanguage(String _lang)
    {
        boolean unknown = true;
        for (String l : langs)
        {
            if(l.equals(_lang))
            {
                unknown = false;
                lang = _lang;
            }
        }
        if (unknown)
        {
            Log.e(new Configuration(),"Unknown language '"+_lang+"'");
            System.exit(-1);
        }
    }
    public static String languagesDescription()
    {
        String res = "";
        for (String l : langs)
        {
            res += l+", ";
        }
        if (res.endsWith(", "))
        {
            res = res.substring(0,res.length()-2);
        }
        return res;
    }
    public static Generator generator()
    {
        switch (lang)
        {
            case "java":
                return new JavaGenerator();
            case "c#":
                return new CSharpGenerator();
            default:
                Log.e(new Configuration(),"Unknown language '"+lang+"'");
                return null;
        }
    }
}

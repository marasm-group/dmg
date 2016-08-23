package com.marasm.dmg;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by vhq473 on 23.08.2016.
 */
public class DMG_JsonParser
{
    ArrayList<DMGObject> objects = new ArrayList<>();
    public DMG_JsonParser(String data)
    {
        ParseJson(data);
    }
    void ParseJson(String data)
    {
        if(data.startsWith("{")) {
            JSONObject json = new JSONObject(data);
            objects.add(parseObject(json,"DMG_JsonObject"));
        }else
        {
            if(data.startsWith("["))
            {
                JSONArray json = new JSONArray(data);
                parseObjectArray(json,"DMG_JsonObject");
            }
        }

    }
    DMGObject parseObject(JSONObject json,String name)
    {
        Set<String> keys = json.keySet();
        DMGObject obj = new DMGObject();
        obj.name = name;
        for(String key : keys)
        {
            Object o = json.get(key);
            Field f = objectToDMG(o,key);
            obj.fields.add(f);
        }
        return obj;
    }
    Field parseArray(JSONArray json,String name)
    {
        for (Object obj : json)
        {
            Field f = objectToDMG(obj,name);
            f.isArray = true;
            return f;
        }
        Log.e(this,"Empty array '"+name+"' in json!");
        return null;
    }
    Field objectToDMG(Object o, String name )
    {
        if (o instanceof JSONObject)
        {
            DMGObject obj = parseObject((JSONObject) o,name);
            return new Field(name,Type.Object,false,obj);
        }
        if (o instanceof  JSONArray)
        {
            return parseArray((JSONArray) o,name);
        }
        if (o instanceof  String)
        {
            return new Field(name, Type.String,false);
        }
        if (o instanceof  Integer)
        {
            return new Field(name, Type.Int,false);
        }
        if (o instanceof  Double)
        {
            return new Field(name, Type.Real,false);
        }
        if (o instanceof Boolean)
        {
            return new Field(name, Type.Bool,false);
        }
        Log.e(this,"Failed to parse type of '"+name+"' <=> "+o);
        return null;
    }



    void parseObjectArray(JSONArray json, String name)
    {
        int i = 0;
        for (Object o : json)
        {
            Field f = objectToDMG(o,"name["+i+"]");
            if(f.type == Type.Object)
            {
                objects.add(f.object);
            }
            else
            {
                Log.e(this,"Object expected, but found "+f.type);
            }
        }
    }
}

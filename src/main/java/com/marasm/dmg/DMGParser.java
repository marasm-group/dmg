package com.marasm.dmg;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Sergey Rump on 19.08.2016.
 */
public class DMGParser
{
    ArrayList<DMGObject> objects = new ArrayList<>();
    public void init(String data,DataType dataType)
    {
        switch (dataType)
        {
            case JSON:
                objects = new DMG_JsonParser(data).objects;
                break;
            default:
                System.out.println("ERROR: Unknown data type '"+dataType+"'");
                break;
        }
    }
    public DMGParser(String data,DataType dataType)
    {
        this.init(data,dataType);
    }
    public DMGParser(String data)
    {
        if (data.startsWith("{") || data.startsWith("["))
        {
            init(data,DataType.JSON);
            return;
        }
        if (data.startsWith("<"))
        {
            init(data,DataType.XML);
            return;
        }
        Log.e(this,"Failed to determine data type automatically!");
    }
}

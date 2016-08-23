package com.marasm.dmg;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sergey Rump on 19.08.2016.
 */
public class DMGObject
{
    public ArrayList<Field> fields = new ArrayList<>();
    public String name = "DMGObject";

    public JSONObject toJSON()
    {
        JSONObject obj = new JSONObject();
        for (Field f : fields)
        {
            JSONObject field = f.toJSON();
            obj.put(f.name,field);
        }
        return obj;
    }
    public String toString()
    {
        return toJSON().toString();
    }
    public String toString(int indentFactor)
    {
        return toJSON().toString(indentFactor);
    }
}

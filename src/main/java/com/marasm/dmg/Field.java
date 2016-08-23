package com.marasm.dmg;

import org.json.JSONObject;

/**
 * Created by Sergey Rump on 19.08.2016.
 */
public class Field
{
    public Type type;
    public String name;
    public boolean isArray = false;
    public DMGObject object = null;

    public Field(String name, Type type)
    {
        this(name, type, false, null);
    }
    public Field(String name, Type type, boolean array)
    {
        this(name, type, array, null);
    }
    public Field(String name, Type type, DMGObject object)
    {
        this(name, type, false, object);
    }
    public Field(String name, Type type, boolean array, DMGObject object)
    {
        this.name = ""+name;
        this.type = type;
        this.isArray = array;
        this.object = object;
    }

    public JSONObject toJSON()
    {
        JSONObject o = new JSONObject();
        String typeKey = "type";
        String classKey = "class";
        String objectKey = "object";
        String isArrayKey = "array";
        switch (type)
        {
            case Object:
                o.put(typeKey,"Object");
                o.put(objectKey,object.toJSON());
                o.put(classKey,object.name);
                break;
            case String:
                o.put(typeKey,"string");
                break;
            case Int:
                o.put(typeKey,"int");
                break;
            case Real:
                o.put(typeKey,"real");
                break;
            case Number:
                o.put(typeKey,"Number");
                break;
            case Bool:
                o.put(typeKey,"bool");
                break;
            default:
                o.put(typeKey,"ERROR_TYPE");
                break;
        }
        o.put(isArrayKey,isArray);
        return o;
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

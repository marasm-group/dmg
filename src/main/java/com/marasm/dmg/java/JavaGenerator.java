package com.marasm.dmg.java;

import com.marasm.dmg.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by vhq473 on 23.08.2016.
 */
public class JavaGenerator implements Generator
{
    String result = "";
    void append(String str)
    {
        result += str+"\n";
        System.out.println(str);
    }
    public void generate(DMGObject object)
    {
        append("class "+capitalizeFirst(object.name)+"\n{");
        for (Field f: object.fields)
        {
            if (f.isArray)
            {
                append("ArrayList<"+type(f)+"> "+f.name+";");
            }
            else
            {
                append(type(f)+" "+f.name+";");
            }
        }

        append("void fillWithJSON(String jsonStr)\n" +
                "{\n" +
                "JSONObject json = new JSONObject(jsonStr);\n" +
                "fillWithJSON(json);\n" +
                "}\n" +
                "void fillWithJSON(JSONObject json)\n{");
        for (Field f: object.fields)
        {
            if (f.isArray)
            {
                processFieldArray(f);
            }
            else
            {
                processField(f);
            }
        }
        append("\n}//fillWithJSON");
        append("\n}//"+object.name);
        for (Field f: object.fields)
        {
            if (f.type ==  Type.Object)
            {
                generate(f.object);
            }
        }
    }
    void processFieldArray(Field f)
    {
        append(f.name+" = new ArrayList<"+type(f)+">();");
        append("try{");
        JSONArray arr;
        append("for (int i = 0; i< json.getJSONArray(\""+f.name+"\").length();i++){\n");
        switch (f.type)
        {
            case Object:
                append(type(f)+" "+"TMP"+" = new "+type(f)+"();");
                append("TMP"+".fillWithJSON(json.getJSONArray(\"" + f.name + "\").getJSONObject(i));");
                append(f.name+".add("+"TMP"+");");
                break;
            case String:
                append("this."+f.name+".add(json.getJSONArray(\"" + f.name + "\").getString(i));");
                break;
            case Int:
                append("this."+f.name+".add(json.getJSONArray(\"" + f.name + "\").getInt(i));");
                break;
            case Real:
                append("this."+f.name+".add(json.getJSONArray(\"" + f.name + "\").getDouble(i));");
                break;
            case Bool:
                append("this."+f.name+".add(json.getJSONArray(\"" + f.name + "\").getBoolean(i));");
                break;
            case Number:
                append("this."+f.name+".add(json.getJSONArray(\"" + f.name + "\").getBigDecimal(i));");
                break;
            default:
                Log.e(this,"invalid type '"+f.type+"'");
                break;
        }
        append("}");
        append("}catch (Exception t) {t.printStackTrace();}");
    }
    void processField(Field f)
    {
        append("try{");
        switch (f.type)
        {
            case Object:
                append("this."+f.name+" = new "+type(f)+"();");
                append("this."+f.name+".fillWithJSON(json.getJSONObject(\""+f.name+"\"));");
                break;
            case String:
                append("this."+f.name+" = json.getString(\""+f.name+"\");");
                break;
            case Int:
                append("this."+f.name+" = json.getInt(\""+f.name+"\");");
                break;
            case Real:
                append("this."+f.name+" = json.getDouble(\""+f.name+"\");");
                break;
            case Bool:
                append("this."+f.name+" = json.getBoolean(\""+f.name+"\");");
                break;
            case Number:
                append("this."+f.name+" = json.getBigDecimal(\""+f.name+"\");");
                break;
            default:
                Log.e(this,"invalid type '"+f.type+"'");
                break;
        }
        append("}catch (Exception t) {t.printStackTrace();}");
    }
    public void generate(ArrayList<DMGObject> objects)
    {
        for (DMGObject obj : objects)
        {
            generate(obj);
        }
    }


    public String type(Field f)
    {
        switch (f.type)
        {
            case Object:
                return capitalizeFirst(f.object.name);
            case String:
                return "String";
            case Int:
                return "int";
            case Real:
                return "double";
            case Number:
                return "BigDecimal";
            case Bool:
                return "boolean";
            default:
                return "ERROR_TYPE";
        }
    }

    public String capitalizeFirst(String str)
    {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

}

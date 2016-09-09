package com.marasm.dmg.generators;

import com.marasm.dmg.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by vhq473 on 09.09.2016.
 */
public class SQL implements Generator
{
    String result = "";
    String ID = "id";

    String MAX_STRING_LENGTH = "1024";

    void append(String str)
    {
        result += str+"\n";
    }

    void generate(DMGObject o, DMGObject parent)
    {
        append(createTable(o,parent));
        append(select(o));
        for (Field f : o.fields)
        {
            if (f.object != null)
            {
                generate(f.object,o);
            }
        }
    }

    public String select(DMGObject o)
    {
        String res = "SELECT "+fields(o);
        res = res.substring(0,res.length()-2);
        res += " FROM "+join(o);
        return res;
    }

    private String fields(DMGObject o)
    {
        String res = "";
        for (Field f: o.fields)
        {
            if((f.object == null)&&(!f.isArray))
            {
                res += table(o.name)+"."+f.name + ", ";
            }
            else
            {
                if(f.object == null) {
                    res += f.name + "." + f.name + ", ";
                }
                else
                {
                    res += fields(f.object);
                }
            }
        }
        return res;
    }

    private String table(String s)
    {
        return Configuration.classname(s);
    }

    private String join(DMGObject o)
    {
        boolean nojoin = true;
        String res = o.name;
        for (Field f : o.fields)
        {
            if(f.isArray || (f.object != null))
            {
                nojoin = false;
                String name = f.name;
                if (f.object != null)
                {
                    name = f.object.name;
                }
                res += " LEFT OUTER JOIN "+name+" ON "+name+"."+o.name+" = "+o.name+"."+ID;
            }
        }
        if (nojoin) {return o.name;}


        return res;
    }

    public String createTable(DMGObject o, DMGObject parent)
    {
        String res = "CREATE TABLE "+table(o.name)+"("+ID+" INTEGER NOT NULL";

        for (Field f: o.fields)
        {
            if((f.object == null)&&(!f.isArray))
            {
                res += ", " + f.name + " " + type(f);
            }
        }
        if (parent != null)
        {
            res += ", " + parent.name + " " + "INTEGER";
            res += ", "+"FOREIGN KEY ("+parent.name+") REFERENCES "+table(parent.name)+"("+ID+")";
        }
        res += ", PRIMARY KEY ("+ID+"));";

        for (Field f: o.fields)
        {
            if(f.isArray && f.object == null)
            {
                res +=  "\nCREATE TABLE "+f.name+"("+ID+" INTEGER NOT NULL";
                res += ", " + f.name + " " + type(f);
                res += ", " + o.name + " " + "INTEGER";
                res += ", "+"FOREIGN KEY ("+o.name+") REFERENCES "+table(o.name)+"("+ID+")";
                res += ", PRIMARY KEY ("+ID+"));";
            }
        }
        return res;
    }

    private String type(Field f)
    {
        switch (f.type)
        {
            case Int: return "INTEGER";
            case String: return "VARCHAR("+MAX_STRING_LENGTH+")";
            case Real: return "DOUBLE";
            case Bool: return "bit";
        }
        return "ERROR_TYPE";
    }


    @Override
    public void generate(ArrayList<DMGObject> objectList)
    {
        for (DMGObject o:objectList)
        {
            generate(o,null);
        }
    }

    @Override
    public void enableFeatures(Collection<Feature> features) {}
    @Override
    public void disableFeatures(Collection<Feature> features) {}

    @Override
    public void beginGeneration() {

    }

    @Override
    public void endGeneration(OutputStream stream) throws IOException {
        stream.write(result.getBytes());
    }
}

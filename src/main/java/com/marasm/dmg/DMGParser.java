package com.marasm.dmg;

import org.apache.commons.io.IOUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

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
            case XML:
                objects = new DMG_XMLParser(data).objects;
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
    public DMGParser(InputStream stream) throws IOException
    {
        this(IOUtils.toString(stream, Utils.getEncoding()));
    }
    public DMGParser(String data)
    {
        data = data.trim();
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
    public void generate(Generator gen)
    {
        gen.beginGeneration();
        gen.generate(objects);
        try {
            OutputStream stream = System.out;
            if (Utils.outFile != null)
            {
                stream = new FileOutputStream(Utils.outFile);
                System.out.println("Writing output to "+Utils.outFile);
            }
            gen.endGeneration(stream);
        } catch (IOException e)
        {
            Log.e(this,"Failed to write result");
            e.printStackTrace();
            System.exit(-1);
        }
    }
}

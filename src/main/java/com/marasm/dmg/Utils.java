package com.marasm.dmg;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by vhq473 on 23.08.2016.
 */
public class Utils
{
    public static boolean forceNumber = false;
    public static String outFile = null;

    public static String getVersion()
    {
        DMGObject object = new DMGObject();
        Package objPackage = object.getClass().getPackage();
        //examine the package object
        String version = objPackage.getImplementationVersion();
        //some jars may use 'Implementation Version' entries in the manifest instead
        return version;
    }
    private static Charset encoding = StandardCharsets.UTF_8;
    public static Charset getEncoding()
    {
        return encoding;
    }
    public static void setEncoding(Charset v)
    {
        encoding = v;
    }

    public static Type numericType(Type t)
    {
        if (Utils.forceNumber)
        {
            switch (t) {
                case Int:
                case Real:
                case Number:
                    return Type.Number;
                default:
                    Log.e(new Utils(),"unexpected type '"+t+"'");
            }
        }
        else
        {
            switch (t) {
                case Int:
                case Real:
                case Number:
                    return t;
                default:
                    Log.e(new Utils(),"unexpected type '"+t+"'");
            }
        }
        return t;
    }
    public static boolean isInteger(String s) {
        return isInteger(s,10);
    }
    public static boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }
    public static boolean isNumeric(String s)
    {
        try
        {
            NumberFormat.getInstance().parse(s);
        }
        catch(ParseException e)
        {
            return false;
        }
        return true;
    }
    public static boolean isBoolean(String value)
    {
        return value.equalsIgnoreCase("false") || value.equalsIgnoreCase("true");
    }

    public static boolean isNullOrEmpty(String value)
    {
        if(value == null){return true;}
        return value.length() > 0;
    }

    public static ArrayList<Field> processDuplicates(ArrayList<Field> fields)
    {
        ArrayList<Field> result = new ArrayList<>();
        for (Field f : fields)
        {
            boolean unique = true;
            for (int i = 0; i < result.size(); i++)
            {
                Field rf = result.get(i);
                if (f.nameAndTypeEquals(rf))
                {
                    rf.isArray = true;
                    unique = false;
                    break;
                }
            }
            if (unique)
            {
                result.add(f);
            }
        }
        return result;
    }

}

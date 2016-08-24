package com.marasm.dmg;

/**
 * Created by vhq473 on 23.08.2016.
 */
public class Utils
{
    public static boolean forceNumber = false;
    public static String getVersion()
    {
        DMGObject object = new DMGObject();
        Package objPackage = object.getClass().getPackage();
        //examine the package object
        String version = objPackage.getImplementationVersion();
        //some jars may use 'Implementation Version' entries in the manifest instead
        return version;
    }
    private static String encoding = "UTF-8";
    public static String getEncoding()
    {
        return encoding;
    }
    public static void setEncoding(String v)
    {
        encoding = v;
    }
}

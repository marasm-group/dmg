package com.marasm.dmg;

/**
 * Created by Sergey Rump on 23.08.2016.
 */
public class Log
{
    static public void e(Object sender, Object msg)
    {
        log("ERROR", sender, msg);
    }
    static public void w(Object sender, Object msg)
    {
        log("WARN", sender, msg);
    }

    static public void i(Object sender, Object msg)
    {
        log("INFO", sender, msg);
    }
    static public void d(Object sender, Object msg)
    {
        log("DEBUG", sender, msg);
    }
    static public void v(Object sender, Object msg)
    {
        log("VERB", sender, msg);
    }
    static public void fixme(Object sender, Object msg)
    {
        log("FIXME", sender, msg);
    }

    static private void log(String tag, Object type, Object msg)
    {
        System.out.println(tag+": "+type.getClass().getName()+": "+msg);
    }
}

/**
 * Created by Sergey Rump on 19.08.2016.
 */
package com.marasm.dmg;

import com.marasm.dmg.generators.SQL;
import com.marasm.dmg.generators.java.JavaGenerator;
import org.apache.commons.cli.*;
import java.io.FileInputStream;
import java.io.IOException;

public class DMG_Main
{
    public static void main(String[] args)
    {
        Options options=new Options();
        options.addOption("h","help",false,"print help");
        options.addOption("version",false,"print version number and exit");
        options.addOption("f","file",true,"file to process");
        options.addOption("json",true,"single-line json string to process");
        options.addOption("forceNumber",false,"use 'Number' data type instead of 'Int' or 'Real'");
        options.addOption("out",true,"output file");
        options.addOption("opt",true,"options (json array of strings)\n"+Configuration.featuresDescription());
        options.addOption("to_json",false,"add 'to_json' option");
        options.addOption("from_json",false,"add 'from_json' option");
        options.addOption("couchbase",false,"add 'couchbase' option");
        options.addOption("classSuffix",true,"suffix to append to all class types");
        options.addOption("rootClass",true,"root class name");
        options.addOption("classSuffix",true,"suffix to append to all class types");
        options.addOption("l","lang",true,"output language (one of following:)\n"+Configuration.languagesDescription()+"\ndefault: java");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse( options, args);
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("dmg", options);
            System.exit(127);
        }
        if(cmd.hasOption("help") || cmd.hasOption("h") || cmd.getOptions().length == 0)
        {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("dmg", options);
            System.exit(0);
        }
        if(cmd.hasOption("version"))
        {
            System.out.println("dmg version: "+Utils.getVersion());
            System.exit(0);
        }
        if(cmd.hasOption("opt"))
        {
            String s = cmd.getOptionValue("opt");
            Configuration.setFeatures(s);
            if(s == null){System.exit(0);}
        }
        if(cmd.hasOption("file") || cmd.hasOption("f"))
        {
            String newfile = cmd.getOptionValue("file");
            if (newfile == null){newfile = cmd.getOptionValue("f");}
            Configuration.file = newfile;
        }
        if(cmd.hasOption("out"))
        {
            Utils.outFile = cmd.getOptionValue("out");
        }
        if(cmd.hasOption("json"))
        {
            Configuration.json = cmd.getOptionValue("json");
        }
        if(cmd.hasOption("rootClass"))
        {
            Utils.rootClass = cmd.getOptionValue("rootClass");
        }
        if(cmd.hasOption("forceNumber"))
        {
            Utils.forceNumber = true;
        }
        if(cmd.hasOption("classSuffix"))
        {
            Configuration.classSuffix = cmd.getOptionValue("classSuffix");
        }
        if(cmd.hasOption("lang") || cmd.hasOption("l"))
        {
            String l = cmd.getOptionValue("lang");
            if(l == null)
            {
                l = cmd.getOptionValue("l");
            }
            Configuration.setLanguage(l);
        }
        if (cmd.hasOption("couchbase"))
        {
            Configuration.setFeatures("[couchbase]");
        }
        if (cmd.hasOption("to_json"))
        {
            Configuration.setFeatures("[to_json]");
        }
        if (cmd.hasOption("from_json"))
        {
            Configuration.setFeatures("[from_json]");
        }
        if (Configuration.classSuffix  == null)
        {
            Configuration.classSuffix = "";
        }
        parse();
    }
    static void parse()
    {
        if (Configuration.file == null && Configuration.json == null)
        {
            Log.e(new DMG_Main(),"No file or data specified");
            System.exit(-1);
        }

        DMGParser parser = null;
        try {
            if (Configuration.file != null)
            {
                parser = new DMGParser(new FileInputStream(Configuration.file));
                Generator gen = new SQL();//Configuration.generator();
                gen.enableFeatures(Configuration.features);
                parser.generate(gen);
            }
            if (Configuration.json != null)
            {
                parser = new DMGParser(Configuration.json, DataType.JSON);
                Generator gen = new SQL();//Configuration.generator();
                gen.enableFeatures(Configuration.features);
                parser.generate(gen);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(new DMG_Main(),"failed to open '"+Configuration.file+"'");
        }

    }

}

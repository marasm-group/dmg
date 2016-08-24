/**
 * Created by Sergey Rump on 19.08.2016.
 */
package com.marasm.dmg;

import com.marasm.dmg.java.JavaGenerator;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;

public class DMG_Main
{
    static String file = null;
    static Collection<Feature> features = new ArrayList<>();
    public static void main(String[] args)
    {
        Options options=new Options();
        options.addOption("h","help",false,"print help");
        options.addOption("version",false,"print version number and exit");
        options.addOption("f","file",true,"file to process");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse( options, args);
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("dmg", options);
            System.exit(127);
        }
        if(cmd.hasOption("version"))
        {
            System.out.println("mvm version: "+Utils.getVersion());
            System.exit(0);
        }
        if(cmd.hasOption("file") || cmd.hasOption("f"))
        {
            String newfile = cmd.getOptionValue("file");
            if (newfile == null){newfile = cmd.getOptionValue("f");}
            file = newfile;
        }
        features.add(Feature.from_json);
        features.add(Feature.to_json);
        parse();
    }
    static class DummyGenerator implements Generator
    {
        public void generate(ArrayList<DMGObject> objectList)
        {
            System.out.println("Parsed data:");
            for (DMGObject obj : objectList)
            {
                System.out.println(obj.name+":\n"+obj.toString(2));
            }
        }
        @Override
        public void enableFeatures(Collection<Feature> features) {}
        @Override
        public void disableFeatures(Collection<Feature> features){}
        @Override
        public void beginGeneration() {}
        @Override
        public void endGeneration(OutputStream stream) throws IOException {}
    }
    static void parse()
    {
        if (file == null){
            Log.e(new DMG_Main(),"No file specified");
        }

        DMGParser parser = null;
        try {
            parser = new DMGParser(new FileInputStream(file));
            Generator gen = new JavaGenerator();
            gen.enableFeatures(features);
            parser.generate(gen);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(new DMG_Main(),"failed to open '"+file+"'");
        }

    }

}

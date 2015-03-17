package com.xiaer;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by kimseongrim.
 * @author Kim
 * @link https://github.com/kimseongrim/html9patch
 */

public class Main {

    // 9-Patch source file directory.
    @Option(name = "-s", usage = "Image or image directory URL\r\n directory is batch processing directory All 9-Patch PNG file.", required = true, metaVar="Source directory")
    private File src = new File(".");

    public static void main(String[] args) throws IOException {
        new Main().doMain(args);
    }

    public void doMain(String[] args) throws IOException {

        // argument
        CmdLineParser parser = new CmdLineParser(this);
        try {
            // parse the arguments.
            parser.parseArgument(args);

        } catch( CmdLineException e ) {
            System.err.println(e.getMessage());
            System.err.println("java SampleMain [options...] arguments...");
            parser.printUsage(System.err);
            System.err.println();

            return;
        }

        // main
        ArrayList<String> htmls = new ArrayList<String>();
        ArrayList<String> ids = new ArrayList<String>();
        String html = "";
        String srcDirectory ="";

        if(src.isDirectory()){

            File[] fa = src.listFiles();
            srcDirectory = src.getCanonicalFile().toString();
            NinePatch np;


            for(int i=0; i< fa.length; i++){
                if(fa[i].getName().substring(fa[i].getName().length() - 6).equalsIgnoreCase(".9.png")){

                    np = new NinePatch(fa[i].getCanonicalFile());
                    np.slice(srcDirectory + System.getProperty("file.separator") + "images");

                    // get html code
                    html = np.getHTML(np.srcName);
                    htmls.add(html);
                    ids.add(np.srcName);
                    // init Array data
                    np.clear();
                }
            }

            // Check directory has 9patch images?
            if(htmls.isEmpty() ){
                System.err.println("Directory No 9patch images.");
                System.exit(0);
            }

        }else{
            System.err.println("Directory does not exist");
            System.exit(0);
        }

        // Create JS File
        UtilTools.createJS(htmls, ids, srcDirectory);

    }

}

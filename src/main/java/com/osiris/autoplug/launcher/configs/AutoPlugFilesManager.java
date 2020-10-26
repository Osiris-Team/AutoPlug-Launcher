/*
 * Copyright Osiris Team
 * All rights reserved.
 *
 * This software is copyrighted work licensed under the terms of the
 * AutoPlug License.  Please consult the file "LICENSE" for details.
 */

package com.osiris.autoplug.launcher.configs;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Responsible for creating all needed files for AutoPlug to run correctly.
 */
public class AutoPlugFilesManager {

    public static File WORKING_DIR = new File(System.getProperty("user.dir"));

    //Directories
    public File plugins = new File(WORKING_DIR+"/plugins");
    public File autoplug_system = new File(WORKING_DIR+"/autoplug-system");

    //YamlFiles
    private final File autopluglauncher_config_yml = new File(WORKING_DIR+"/autoplug-launcher-config.yml");

    //Jars
    private File AutoPlugLauncher = new File(autoplug_system+"/AutoPlugLauncher.jar");
    private File AutoPlug = new File(autoplug_system+"/AutoPlug.jar");
    private File AutoPlugPlugin = new File(autoplug_system+"/AutoPlugPlugin.jar");


    private List<File> directories;
    private List<File> yamlFiles;
    private List<File> jarFiles;

    private static int missing;
    private static int total;

    public void check(){

        missing=0;
        total=0;

        try {
            directories = Arrays.asList(
                    plugins, autoplug_system);
            checkFiles("dir", directories);

            yamlFiles = Arrays.asList(autopluglauncher_config_yml);
            checkFiles("xml", yamlFiles);

            jarFiles = Arrays.asList(AutoPlugLauncher, AutoPlug, AutoPlugPlugin);
            checkFiles("jar", jarFiles);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public boolean isFirstRun(){
        //If of the files is missing and had to be generated, the user is considered as a new user!
        if (missing==total){
            return true;
        }
        return false;
    }

    /**
     * Iterates through all directories and creates missing files
     * @param file_type Enter 'dir' if its a directory
     * @param files The file we check for its existence
     * @throws IOException
     */
    private void checkFiles(String file_type, List<File> files) throws IOException {

        total++;

        //Iterate through all directories and create missing ones
        System.out.println("Checking "+file_type+"s...");
        for (int i = 0; i < files.size(); i++) {

            if (!files.get(i).exists()) {
                System.out.println(" - Generating: " + files.get(i).getName());

                if (file_type.equals("dir")){
                    files.get(i).mkdirs();
                    missing++;
                } else{
                    files.get(i).createNewFile();
                    missing++;
                }

                }

        }
        System.out.println("All "+file_type+"s ok!");

    }

}

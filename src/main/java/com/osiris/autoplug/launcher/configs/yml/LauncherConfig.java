/*
 * Copyright (c) 2020 [Osiris Team](https://github.com/Osiris-Team)
 *  All rights reserved.
 *  
 *  This software is copyrighted work licensed under the terms of the
 *  AutoPlug License.  Please consult the file "LICENSE" for details.
 */

package com.osiris.autoplug.launcher.configs.yml;

import org.simpleyaml.configuration.file.YamlFile;

import java.io.IOException;

public class LauncherConfig {

    private final YamlFile config = new YamlFile("autoplug-launcher-config.yml");

    public void load(){

        // Load the YAML file if is already created or create new one otherwise
        try {
            if (!config.exists()) {
                System.out.println(" - autoplug-launcher-config.yml not found! Creating new one...");
                config.createNewFile(true);
            }
            else {
                System.out.println(" - Loading autoplug-launcher-config.yml...");
            }
            config.load(); // Loads the entire file
        } catch (Exception e) {
            System.out.println("Failed to load autoplug-launcher-config.yml...");
            e.printStackTrace();
        }

        // Insert defaults
        insertDefaults();

        // Makes settings globally accessible
        setUserOptions();

        // Validates options
        validateOptions();

        //Finally save the file
        save();

    }

    private void insertDefaults(){

        config.addDefault("autoplug-launcher-config.update-check.enable", true);
        config.addDefault("autoplug-launcher-config.update-check.mode", "stable-builds");

    }

    //User configuration
    public static boolean update_check_enabled;
    public static String update_check_mode;


    private void setUserOptions(){

        //UPDATE CHECK
        update_check_enabled = config.getBoolean("autoplug-launcher-config.update-check.enable");
        update_check_mode = config.getString("autoplug-launcher-config.update-check.mode");

    }

    private void validateOptions() {

        if (update_check_mode.equals("stable-builds") || update_check_mode.equals("beta-builds")){
        }else{
            System.out.println("Config error! mode must be stable-builds or beta-builds and nothing else!");
            System.out.println("Setting default: stable-builds");
            update_check_mode = "stable-builds";
        }

    }


    private void save() {

        // Finally, save changes!
        try {
            config.saveWithComments();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Issues while saving config.yml");
        }

        System.out.println(" - Configuration file loaded!");

    }

}

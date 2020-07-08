/*
 * Copyright (c) 2020 [Osiris Team](https://github.com/Osiris-Team)
 *  All rights reserved.
 *
 *  This software is copyrighted work licensed under the terms of the
 *  AutoPlug License.  Please consult the file "LICENSE" for details.
 */

package com.osiris.autoplug.launcher;

import com.osiris.autoplug.launcher.configs.AutoPlugFilesManager;
import com.osiris.autoplug.launcher.configs.xml.UpdateConfig;
import com.osiris.autoplug.launcher.configs.yml.LauncherConfig;
import org.update4j.Configuration;
import org.update4j.FileMetadata;
import org.update4j.LaunchContext;
import org.update4j.Property;
import org.update4j.inject.Injectable;
import org.update4j.service.Launcher;
import org.update4j.service.UpdateHandler;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Main implements UpdateHandler, Injectable {

    public static void main(String[] args) {

        try {
            /*
            Workflow: First we check if the launcher needs to get updated. If so we launch it.
            Then we check the other files for updates and launch AutoPlug.jar
             */

            AutoPlugFilesManager files = new AutoPlugFilesManager();
            files.check();

            //Loads and validates the yml config file
            LauncherConfig yml = new LauncherConfig();
            yml.load();

            if (LauncherConfig.update_check_enabled){

                //This will fetch the latest update.xml online
                UpdateConfig xml = new UpdateConfig();
                List<Configuration> allConfigs = xml.fetch();

                for (int i = 0; i < allConfigs.size(); i++) {

                    Configuration config = allConfigs.get(i);

                    //Compare update.xml with the local file and check for updates
                    CompletableFuture<Boolean> searchForUpdates = CompletableFuture.supplyAsync(() -> {
                        try { return config.requiresUpdate(); }
                        catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Error! Skipping update search...");
                            return false;
                        }
                    });

                    while (!searchForUpdates.isDone()) {
                        waitingForTask("Searching for updates");
                    }
                    boolean result = searchForUpdates.get();

                    if (result){
                        System.out.println("Update found! - Downloading from: "+ config.getBaseUri());

                        //Compare update.xml with the local file
                        CompletableFuture<Boolean> performUpdate = CompletableFuture.supplyAsync(() -> config.update());

                        while (!performUpdate.isDone()) {
                            //downloadProcess();
                        }
                        boolean update = performUpdate.get();

                        if (update){
                            System.out.println("Downloaded update successfully!");

                            //If i is 0 that means that its the launchers update config xml file. For details see UpdateConfig.class
                            if (i==0){
                                //TODO UPDATE ITSELF
                            }
                            else if (i==2){
                                try{
                                    //This will throw nullpoiterexception if there is no launcher property given
                                    System.out.println("LAUNCHER: "+config.getLauncher());
                                    config.launch();
                                } catch (Exception e) {
                                    System.out.println("No launcher found, skipping launch...");
                                }
                            }

                        }
                        else{
                            System.out.println("Failed to download update!");
                        }


                    }
                    else{
                        System.out.print("No updates found!");

                        //If i is 0 that means that its the launchers update config xml file. For details see UpdateConfig.class
                        if (i==0){
                            //TODO UPDATE ITSELF
                        }
                        else if (i==2){
                            try{
                                //This will throw nullpoiterexception if there is no launcher property given
                                System.out.println("LAUNCHER: "+config.getLauncher());
                                config.launch();
                            } catch (Exception e) {
                                System.out.println("No launcher found, skipping launch...");
                            }
                        }

                    }

                } //End of for loop

            } //End of if
            else{
                System.out.println("Skipping update-check!");
            }


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error initialising AutoPlugLauncher!");
            System.out.println("Please notify the developers!");
        }

    }

    private static void waitingForTask(String message) throws InterruptedException {
        System.out.println(" ");
        for (int i = 0; i < 2; i++) {
            if (i==0){
                System.out.print(message+".     \r");
                Thread.sleep(500);
            }
            else if (i==1){
                System.out.print(message+"..    \r");
                Thread.sleep(500);
            }
            else{
                System.out.print(message+"...   \r");
                Thread.sleep(500);
            }
        }
    }


    @Override
    public void updateDownloadFileProgress(FileMetadata file, float frac) {
        System.out.print("Downloading " + file.getPath().getFileName() + " (" + ((int) (100 * frac)) + "%)");
    }

}

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
import org.update4j.inject.Injectable;
import org.update4j.service.UpdateHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Main implements UpdateHandler, Injectable {

    public static void main(String[] args) {

        try {

            Thread.sleep(3000);

            DuplicateCheck dc = new DuplicateCheck();
            dc.isRunning();

            AutoPlugFilesManager files = new AutoPlugFilesManager();
            files.check();

            //Loads and validates the yml config file
            LauncherConfig yml = new LauncherConfig();
            yml.load();

            //Check if we are at the updates directory /autoplug-system. If we aren`t then it means we are not an update file.
            String dir = System.getProperty("user.dir");
            if (dir.contains("autoplug-system")){

                //This means that we are an update file and we need to copy us into /
                String main = dir.substring(0, dir.length() - 16); //This removes the last 16 chars(.../autoplug-system) so we can go 1dir back into the main dir

                try {
                    //Move the file to main dir
                    Path target = Paths.get(main + "/AutoPlugLauncher.jar");
                    Files.copy(Paths.get(System.getProperty("user.dir") + "/AutoPlugLauncher.jar"), target, StandardCopyOption.REPLACE_EXISTING);

                    //Start the updated file
                    List<String> commands = new ArrayList<>();
                    commands.add("java");
                    commands.add("-jar");
                    commands.add(target.toString());
                    ProcessBuilder processBuilder = new ProcessBuilder(commands);
                    processBuilder.directory();
                    processBuilder.redirectErrorStream(true);
                    processBuilder.start();

                    System.exit(0);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            else{
                //This mean we are not in the autoplug-system, but in the main folder so we can perform and download updates

                if (LauncherConfig.update_check_enabled){

                    //This will fetch the latest update.xml online
                    UpdateConfig xml = new UpdateConfig();
                    List<Configuration> allConfigs = xml.fetch();

                    for (int i = 0; i < allConfigs.size(); i++) {

                        downloadUpdate(allConfigs.get(i), i);

                    } //End of for loop

                } //End of if
                else{
                    System.out.println("Skipping update-check!");
                }

            }




        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error initialising AutoPlugLauncher!");
            System.out.println("Please notify the developers!");
        }

    }

    private static void downloadUpdate(Configuration config, int i) throws Exception{

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

                //If i is 0 that means that its the launchers update config xml file and it was updated
                if (i==0){

                    System.out.println("There is an update available for AutoPlugLauncher.jar.");
                    System.out.println("In order to update it we have to restart it");
                    System.out.println("That means that this console will be closed.");
                    System.out.println("Please switch over to the online console at https://autoplug.ddns.net to follow the update progress.");
                    Thread.sleep(5000);

                    //Start the downloaded updated file
                    List<String> commands = new ArrayList<>();
                    commands.add("java");
                    commands.add("-jar");
                    commands.add(System.getProperty("user.dir")+"/autoplug-system/AutoPlugLauncher.jar");
                    ProcessBuilder processBuilder = new ProcessBuilder(commands);
                    processBuilder.directory();
                    processBuilder.redirectErrorStream(true);
                    processBuilder.start();

                    System.exit(0);
                }
                else if (i==2){
                    try{
                        //This will throw nullpoiterexception if there is no launcher property given
                        System.out.println("Starting AutoPlug from AutoPlugLauncher at class: "+config.getLauncher());
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

            if (i==2){
                try{
                    //This will throw nullpoiterexception if there is no launcher property given
                    System.out.println("LAUNCHER: "+config.getLauncher());
                    config.launch();
                } catch (Exception e) {
                    System.out.println("No launcher found, skipping launch...");
                }
            }

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

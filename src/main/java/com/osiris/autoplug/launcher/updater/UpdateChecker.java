/*
 * Copyright Osiris Team
 * All rights reserved.
 *
 * This software is copyrighted work licensed under the terms of the
 * AutoPlug License.  Please consult the file "LICENSE" for details.
 */

package com.osiris.autoplug.launcher.updater;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.osiris.autoplug.launcher.LauncherProperties;
import com.osiris.autoplug.launcher.configs.yml.LauncherConfig;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class UpdateChecker {
    
    private JsonTools jTools = new JsonTools();
    private List<JsonObject> jarsToCheckList = new ArrayList<>();

    private List<String> launchPathsList = new ArrayList<>();
    private List<String> launchClassesList = new ArrayList<>();

    /**
     * Returns null if there is no update available.
     * If there is a update available it will return a JsonObject containing all information needed to update.
     */
    public void checkAndUpdate() throws Exception{

        String jsonUrl;

        if (LauncherConfig.update_check_mode.equals("stable-builds")){
            System.out.println("Searching for new stable builds...");
            jsonUrl = "https://raw.githubusercontent.com/Osiris-Team/AutoPlug-Releases/master/stable-builds/update.json";

        }
        else {
            System.out.println("Searching for new beta builds...");
            jsonUrl = "https://raw.githubusercontent.com/Osiris-Team/AutoPlug-Releases/master/beta-builds/update.json";
        }

        //Get the update.json file from this static url and convert the array inside it into a list
        jarsToCheckList = jTools.getJsonArrayAsList(jsonUrl);

        //Go through each JsonObject in the list (Each jsonObject represents a jar file)
        for (int i = 0; i < jarsToCheckList.size(); i++) {

            //Get the JsonObject
            JsonObject jar = jarsToCheckList.get(i);

            //Get details about the latest jar
            int id = jar.get("id").getAsInt();
            String path = jar.get("path").getAsString();
            double version = jar.get("version").getAsDouble();
            String url = jar.get("url").getAsString();
            String checksum = jar.get("checksum").getAsString();
            long size = jar.get("size").getAsLong();
            String launchClass = jar.get("class").getAsString();

            //Check if the jar exists at the path
            if (new File(path).exists()){

                //Get the autoplug.properties file of the old/local/already installed jar
                Properties p = getProperties(path);

                //The values for localId and localVersion are created at the jars compilation by maven
                int localId = Integer.parseInt(p.getProperty("id"));
                double localVersion = Double.parseDouble(p.getProperty("version"));

                //Compare the ids to be sure, that the right jar is installed at that path
                if (id == localId){

                    //Check if the version number is greater than the localVersion number
                    if (version>localVersion){
                        //This means that we have an update available

                        //TODO download latest jar from url to cache, create a checksum when finished and compare them
                        if (checksum.equals(getChecksum(path))){
                            //This means that the downloaded was successfull and we now can replace/update the actual file
                            //TODO check if the jar is still running
                            //Delete the old version
                            new File(path).delete();
                            //TODO copy the update from cache to the actual installation path

                            if (!launchClass.equals("none")){
                                //This means that we must start the jar after finishing updating
                                //For that we add its path and launchClass to theirs lists
                                launchPathsList.add(path);
                                launchClassesList.add(launchClass);
                                //TODO implement functionality for launching jars
                            }
                            else{
                                //This means that the jar must NOT be started after finishing updating
                            }

                        }
                        else{
                            //This means that the download failed and we need to restart it
                            //TODO implement functionality of retrying 3 times
                        }//End of if 4

                    }
                    else{
                        //This mean that there is no update available and we can skip to the next jar
                        //TODO notify that there is NO update available
                    } //End of if 3

                }
                else{
                    //This means that the jars name/path doesn't match to its id so it must be deleted and reinstalled
                    new File(path).delete();
                    //TODO download process

                } //End of if 2

            } //End of if 1
            else{
                //This means that the jar does not exist at its installation path so we download it directly via its download link
                jar.get("url").getAsString();
                jar.get("version").getAsString();
                jar.get("size").getAsString();
                jar.get("checksum").getAsString();
                //TODO downloading and process showing
            }

        }

    }

    /**
     * This creates an URLClassLoader so we can access the autoplug.properties file inside the jar and then returns the properties file.
     * @param path The jars path
     * @return autoplug.properties
     * @throws Exception
     */
    private Properties getProperties(String path) throws Exception{

        //The properties file
        File file = new File(path);

        if (file.exists()){
            try{

                Collection<URL> urls = new ArrayList<URL>();
                urls.add(file.toURI().toURL());
                URLClassLoader fileClassLoader = new URLClassLoader(urls.toArray(new URL[urls.size()]));

                java.io.InputStream is = fileClassLoader.getResourceAsStream("autoplug.properties");
                java.util.Properties p = new java.util.Properties();
                p.load(is);
                return p;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }
        else{
            throw new Exception("Couldn't find the file at: " + path);
        }
    }

}

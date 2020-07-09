/*
 * Copyright (c) 2020 [Osiris Team](https://github.com/Osiris-Team)
 *  All rights reserved.
 *
 *  This software is copyrighted work licensed under the terms of the
 *  AutoPlug License.  Please consult the file "LICENSE" for details.
 */

package com.osiris.autoplug.launcher.configs.xml;

import com.osiris.autoplug.launcher.configs.yml.LauncherConfig;
import org.update4j.Configuration;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class UpdateConfig {

        public List<Configuration> fetch(){
                try {
                        List<Configuration> allConfigs = new ArrayList<>();

                        if (LauncherConfig.update_check_mode.equals("stable-builds")) {

                                //The order of these files is important! See Main.class for details
                                allConfigs.add(fetchOnlineUpdatesConfig("AutoPlugLauncher", "stable-builds"));
                                allConfigs.add(fetchOnlineUpdatesConfig("AutoPlugPlugin", "stable-builds"));
                                allConfigs.add(fetchOnlineUpdatesConfig("AutoPlug", "stable-builds"));
                        } else {
                                allConfigs.add(fetchOnlineUpdatesConfig("AutoPlugLauncher", "beta-builds"));
                                allConfigs.add(fetchOnlineUpdatesConfig("AutoPlugPlugin", "beta-builds"));
                                allConfigs.add(fetchOnlineUpdatesConfig("AutoPlug", "beta-builds"));
                        }

                        return  allConfigs;
                } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                }
        }

        private Configuration fetchOnlineUpdatesConfig(String name, String builds) throws Exception {
                final String CONFIG_FILENAME = "update-"+name+".xml";
                final String CONFIG_URL = "https://github.com/Osiris-Team/AutoPlug-Releases/raw/master/"+builds+"/"+CONFIG_FILENAME;

                System.out.println("Fetching latest "+CONFIG_FILENAME+"...");

                //Fetches the online updates xml config
                Configuration config;
                URL configUrl = new URL(CONFIG_URL);
                try (Reader in = new InputStreamReader(configUrl.openStream(), StandardCharsets.UTF_8)) {
                        config = Configuration.read(in);
                } catch (Exception e) {
                        System.err.println("Failed to get update xml file! Make sure you have and internet connection!");
                        return null;
                }
                return config;
        }

}

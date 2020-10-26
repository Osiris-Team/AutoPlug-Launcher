/*
 * Copyright Osiris Team
 * All rights reserved.
 *
 * This software is copyrighted work licensed under the terms of the
 * AutoPlug License.  Please consult the file "LICENSE" for details.
 */

package com.osiris.autoplug.launcher.updater;

import com.google.gson.JsonObject;

import java.util.List;

public class ApiAutoPlugUpdater {

    private final JsonTools jsonTools = new JsonTools();

    /**
     * Returns a list of JsonObjects.
     * It should contain a name and a url.
     */
    public List<JsonObject> getStableUpdates(){
        return
    }

    /**
     * Returns a list of JsonObjects.
     * It should contain a name and a url.
     */
    public List<JsonObject> getBetaUpdates(){
        return jsonTools.getJsonArrayAsList("https://raw.githubusercontent.com/Osiris-Team/AutoPlug-Releases/master/beta-builds/update.json");
    }

    /**
     * Get a json object
     * @param url
     * @return
     */
    public JsonObject getJsonObject(String url){
        return jsonTools.getJsonObject(url);
    }

}

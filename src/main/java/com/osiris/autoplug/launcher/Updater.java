/*
 * Copyright Osiris Team
 * All rights reserved.
 *
 * This software is copyrighted work licensed under the terms of the
 * AutoPlug License.  Please consult the file "LICENSE" for details.
 */

package com.osiris.autoplug.launcher;

import com.google.gson.*;

public class Updater {

    public static void main(String[] args) {

        Gson gson = new Gson();
        JsonObject object = new JsonObject();
        object.addProperty("path", 1);
        object.addProperty("property2", 2);
        object.addProperty("property3", 3);

        JsonArray array = new JsonArray();
        array.add(object);
        array.add(object2);

        String json = gson.toJson(array);

        System.out.println(json);

    }


    public void check(){
        //TODO 0. Which update.yml files should be fetched? - See this in master-update.yml
        // Master.yml contains a list with links to the other update.yml files
        //TODO 1. Where are the update.yml files? - In /autoplug-system
        //TODO 2. What do they contain? UPDATE.YML-DOWNLOAD-LINK - DOWNLOAD-LINK, NAME, VERSION, DOWNLOAD-PATH
        // (yml-link ; file-link ; name ; version ; download-path
        //TODO 3. From where do I get the LATEST update.yml file? - link is inside old update.yml and must be constant
        //TODO 4.
    }

    public void download(){

    }

    public void update(){


    }


}

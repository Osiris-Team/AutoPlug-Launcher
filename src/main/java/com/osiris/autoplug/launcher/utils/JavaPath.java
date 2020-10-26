/*
 * Copyright Osiris Team
 * All rights reserved.
 *
 * This software is copyrighted work licensed under the terms of the
 * AutoPlug License.  Please consult the file "LICENSE" for details.
 */

package com.osiris.autoplug.launcher.utils;

public class JavaPath {

    public String getPath(){

        String path = "ERROR";
        //Check which system we are running on
        String os = System.getProperty("os.name");
        if (os.contains(" ")){
            String[] s = os.split(" ");
            os = s[0];
        }

        if (os.contains("Windows") || os.contains("windows")){
            path = System.getProperty("java.home")+"\\bin\\java.exe";
        }
        else if (os.contains("Linux") || os.contains("linux")){
            path = System.getProperty("java.home")+"/bin/java";
        }
        else{
            path = System.getProperty("java.home")+"/bin/java";
        }

        return path;

    }

}

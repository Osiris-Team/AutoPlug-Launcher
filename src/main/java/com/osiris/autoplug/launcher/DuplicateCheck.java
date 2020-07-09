/*
 * Copyright (c) 2020 [Osiris Team](https://github.com/Osiris-Team)
 *  All rights reserved.
 *
 *  This software is copyrighted work licensed under the terms of the
 *  AutoPlug License.  Please consult the file "LICENSE" for details.
 */

package com.osiris.autoplug.launcher;

import org.gridkit.lab.jvm.attach.AttachManager;
import org.gridkit.lab.jvm.attach.JavaProcessId;

import java.util.ArrayList;
import java.util.List;

public class DuplicateCheck {

    /**
     * Searches for java processes with similar name and if there are more than 2 in total it will shut itself down
     * @return
     */
    public boolean isRunning(){

        int running = 0;
        List<String> processes = new ArrayList<>();

        for (JavaProcessId id :
                AttachManager.listJavaProcesses()) {
            if (id.getDescription().contains("AutoPlugLauncher.jar") || id.getDescription().contains("autoplug.launcher")){
                running++;
                processes.add(" Instance -> "+id.getDescription()+" with PID:"+id.getPID());
            }
        }
        AttachManager.listJavaProcesses();

        if (running>=2){
            System.out.println("Found multiple instances of AutoPlugLauncher!!!");
            for (String instance :
                    processes) {
                System.out.println(instance);
            }

            System.out.println("Shutting down...");
            System.exit(0);
            return true;
        }
        return false;

    }

}

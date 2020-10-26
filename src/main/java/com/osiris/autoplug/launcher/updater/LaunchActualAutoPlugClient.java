/*
 * Copyright Osiris Team
 * All rights reserved.
 *
 * This software is copyrighted work licensed under the terms of the
 * AutoPlug License.  Please consult the file "LICENSE" for details.
 */

package com.osiris.autoplug.launcher.updater;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class LaunchActualAutoPlugClient {

    public void launch() throws Exception{

        URLClassLoader child = new URLClassLoader(
                new URL[] {myJar.toURI().toURL()},
                this.getClass().getClassLoader()
        );
        Class classToLoad = Class.forName("com.MyClass", true, child);
        Method method = classToLoad.getDeclaredMethod("myMethod");
        Object instance = classToLoad.newInstance();
        Object result = method.invoke(instance);

    }

}

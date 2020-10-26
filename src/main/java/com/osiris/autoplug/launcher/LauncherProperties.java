/*
 * Copyright Osiris Team
 * All rights reserved.
 *
 * This software is copyrighted work licensed under the terms of the
 * AutoPlug License.  Please consult the file "LICENSE" for details.
 */

package com.osiris.autoplug.launcher;

import java.io.IOException;

public class LauncherProperties {

    private String id;
    private String name;
    private double version;

    public LauncherProperties() throws IOException {
        java.io.InputStream is = this.getClass().getResourceAsStream("launcher-properties");
        java.util.Properties p = new java.util.Properties();
        p.load(is);
        id = p.getProperty("id");
        name = p.getProperty("name");
        version = Double.parseDouble(p.getProperty("version"));
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getVersion() {
        return version;
    }
}

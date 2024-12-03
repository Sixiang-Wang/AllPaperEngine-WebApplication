package com.example.scholar.config;

public class PathConfig {

    public static final String os = System.getProperty("os.name").toLowerCase();
    public static String path;
    public static final String pathLinux = "/www/data/Scholar/store";
    public static final String pathWindow = System.getProperty("user.dir")+"\\store";

    static {
        if (os.contains("win")) {
            path = pathWindow;
        } else {
            path = pathLinux;
        }
    }
}

package ru.dkx86.boorudownloader;

public final class Herald {

    public static void info(String msg) {
        System.out.println(" -INFO- " + msg);
    }

    public static void err(String msg) {
        System.err.println(" !! ERROR !! " + msg);
    }

    public static void say(String msg) {
        System.out.println(msg);
    }

    public static void err(String msg, Exception e) {
        err(msg + ": " + e.getMessage());
    }
}

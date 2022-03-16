package ru.dkx86.boorudownloader;

import ru.dkx86.boorudownloader.dl.Downloader;
import ru.dkx86.boorudownloader.dl.QueueFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {


    public static void main(String[] args) {
        if (args.length == 0 || args[0].equals("--help") || args[0].equals("-h")) {
            printHelp();
            return;
        }

        if (args[0].equals("--list")) {
            printAvailableSites();
            return;
        }

        String source = null;
        List<String> tags = null;
        if (args[0].equals("-s"))
            source = args[1];

        if (args[2].equals("-t"))
            tags = new ArrayList<>(Arrays.asList(args).subList(3, args.length));

        if (source == null || tags == null) {
            printHelp();
            return;
        }
        var queueFile = new QueueFile();
        var src = AvailableSites.valueOf(source);
        new Downloader(src, tags, queueFile).start();

    }

    private static void printAvailableSites() {
        for (AvailableSites value : AvailableSites.values()) {
            System.out.println(value + " (" + value.getName() + ")");
        }
    }


    private static void printHelp() {
        System.out.println("booru_downloader v1.0");
        System.out.println("PARAMS:");

        System.out.println("--list - list supported websites.");
        System.out.println("-s <SITE_NAME> -t tag1 tag2 tagN - download all images by tags. Use <SITE_NAME> from the '--list' command output.");
        System.out.println("--help -h - pring this help.");
    }
}

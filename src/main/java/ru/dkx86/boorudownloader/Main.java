package ru.dkx86.boorudownloader;

import ru.dkx86.boorudownloader.dl.Downloader;
import ru.dkx86.boorudownloader.dl.QueueFile;

public class Main {


    public static void main(String[] args) {
        if (args.length == 0 || args[0].equals("--help")) {
            printHelp();
            return;
        }

        if (args[0].equals("--list")) {
            printAvailableSites();
            return;
        }


        String source = null;
        String tags = null;
        String ratingStr = null;
        for (var i = 0; i < args.length; i++) {
            if (args[i].equals("--src"))
                source = args[i + 1];

            if (args[i].equals("--tags"))
                tags = args[i + 1];

            if (args[i].equals("--rating"))
                ratingStr = args[i + 1];
        }


        if (source == null || tags == null) {
            printHelp();
            return;
        }
        final RatingFilter ratingFilter = new RatingFilter(ratingStr);
        var queueFile = new QueueFile();
        var src = AvailableSites.valueOf(source);
        new Downloader(src, tags, queueFile, ratingFilter).start();

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
        System.out.println("--src <SITE_NAME> - source site name. Use <SITE_NAME> from the '--list' command output.");
        System.out.println("--tags \"tag1 tag2 tagN\" - tag list to download");
        System.out.println("--rating s|q|e - rating: safe | questionable | explicit. Will download all if not present.");
        System.out.println("--help - print this help.");
    }
}

package ru.dkx86.boorudownloader.dl;

import ru.dkx86.boorudownloader.Herald;

import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Queue;

public class QueueFile {
    private static final String DIR_NAME = "images";

    private final Queue<Entry> links = new ArrayDeque<>();

    public QueueFile() {
        createOutputDir();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void createOutputDir() {
        var dir = Paths.get(DIR_NAME).toFile();
        if (!dir.exists())
            dir.mkdir();
    }

    public synchronized Entry getNextLink() {
        if (links.isEmpty())
            return null;

        return links.poll();
    }

    public void addLink(String link) {
        try {
            var url = new URI(link).toURL();
            var path = Paths.get(DIR_NAME).resolve(getFileName(link));
            links.add(new Entry(url, path));
        } catch (Exception e) {
            Herald.err("Malformed link: " + link + " ERROR:" + e.getMessage());
        }
    }

    private String validateLink(String link) {
        return link.startsWith("//konachan.net") ? "http:" + link : link;
    }

    private String getFileName(String url) {
        final String[] parts = url.split("/");
        var fileName = URLDecoder.decode(parts[parts.length - 1], StandardCharsets.UTF_8);
        return fileName.replaceAll("\\*", "_")
                .replaceAll("\\?", "_")
                .replaceAll("<", "_")
                .replaceAll(">", "_")
                .replaceAll("%", "_")
                .replaceAll("\"", "_")
                .replaceAll(":", "_");
    }

    public int size() {
        return links.size();
    }

    public static class Entry {
        private final URL url;
        private final Path path;

        public Entry(URL url, Path path) {
            this.url = url;
            this.path = path;
        }

        public URL getUrl() {
            return url;
        }

        public Path getPath() {
            return path;
        }
    }

}

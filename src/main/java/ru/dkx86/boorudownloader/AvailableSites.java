package ru.dkx86.boorudownloader;

public enum AvailableSites {
    KONACHAN("konachan.net", "https://konachan.net/post.json");
    //YANDERE("yande.re", "https://yande.re/post.json"),
    //DANBOORU("danbooru.donmai.us","https://danbooru.donmai.us/posts.json");

    private final String url;
    private final String name;

    AvailableSites(String name, String url) {
        this.url = url;
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }
}

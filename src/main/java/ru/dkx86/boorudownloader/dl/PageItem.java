package ru.dkx86.boorudownloader.dl;

import com.google.gson.annotations.SerializedName;

public class PageItem {
    @SerializedName("id")
    private Long id;

    @SerializedName("created_at")
    private Long createdAt;

    @SerializedName("rating")
    private String rating;

    @SerializedName("source")
    private String source;

    @SerializedName("file_url")
    private String fileUrl;

    @SerializedName("jpeg_url")
    private String jpegUrl;

    public Long getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    Rating getRating() {
        return Rating.getByValue(rating);
    }

    public String getSource() {
        return source;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public String getJpegUrl() {
        return jpegUrl;
    }
}

package ru.dkx86.boorudownloader.dl;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import ru.dkx86.boorudownloader.AvailableSites;
import ru.dkx86.boorudownloader.Herald;
import ru.dkx86.boorudownloader.RatingFilter;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class Downloader {

    private final static int LIMIT = 100;
    private static final int REPEAT_TIMEOUT = 20000; // 20 sec

    private final AvailableSites site;
    private final String tags;
    private final QueueFile queueFile;
    private final RatingFilter ratingFilter;
    private final Gson gson;

    public Downloader(AvailableSites site, String tags, QueueFile queueFile, RatingFilter ratingFilter) {
        this.site = site;
        this.tags = tags;
        this.queueFile = queueFile;
        this.ratingFilter = ratingFilter;
        this.gson = new Gson();
    }

    private String makeUrl(int page) {
        StringBuilder url = new StringBuilder();
        url.append(site.getUrl()).append('?');
        url.append("page=").append(page);
        url.append("&limit=").append(LIMIT);

        if (tags != null && !tags.isEmpty()) {
            url.append("&tags=").append(URLEncoder.encode(tags.trim(), StandardCharsets.UTF_8));
        }

        return url.toString().trim();
    }

    public void start() {

        Herald.info("Start collecting links...");
        try {
            collectingLinks();
        } catch (IOException e) {
            Herald.err("Cannot collect links", e);
            return;
        }
        Herald.info("Collected " + queueFile.size() + " links.");
        Herald.info("Start downloading in threads");
        int cores = Runtime.getRuntime().availableProcessors();
        Herald.info("Your PC has " + cores + " CPU cores.");
        ExecutorService executor = Executors.newFixedThreadPool(cores);
        for (int i = 0; i < cores; i++) {
            executor.execute(new ImageThread("it_" + i, queueFile));
        }
        executor.shutdown();
        try {
            //noinspection ResultOfMethodCallIgnored
            executor.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
            final var end = System.currentTimeMillis();
        } catch (InterruptedException e) {
            Herald.err(e.getMessage());
        }

    }

    private void collectingLinks() throws IOException {
        int page = 1;

        while (true) {
            final List<PageItem> list = getPage(page++);
            if (list == null) {
                Herald.err("Items list is null, something bad happened");
                break;
            }

            if (list.isEmpty()) {
                Herald.info("Successfully finished collecting image links.");
                break;
            }

            int counter = 0;
            for (PageItem item : list) {
                if (ratingFilter.isValid(item.getRating())) {
                    queueFile.addLink(item.getFileUrl());
                    counter++;
                }
            }

            Herald.info(counter + " link added to queue.");
            doWait(3000L);
        }
    }


    private List<PageItem> getPage(int pageNumber) {
        List<PageItem> items = new ArrayList<>();
        URL konaUrl = null;
        try {
            konaUrl = new URL(makeUrl(pageNumber));
            final Reader reader;
            if (konaUrl.toString().contains("https://")) {
                final HttpsURLConnection con = (HttpsURLConnection) konaUrl.openConnection();
                reader = new InputStreamReader(con.getInputStream());
            } else {
                reader = new InputStreamReader(konaUrl.openStream());
            }
            items = gson.fromJson(reader, new TypeToken<List<PageItem>>() {
            }.getType());
            reader.close();
        } catch (IOException e) {
            Herald.err("Unable get page " + pageNumber + "By URL: " + konaUrl + " Waiting for repeat.");
            doWait(Downloader.REPEAT_TIMEOUT);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }

        return items;
    }

    private void doWait(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            Herald.err("Page waiter failed: ", e);
        }
    }

}

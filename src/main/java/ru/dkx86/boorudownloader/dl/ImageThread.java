package ru.dkx86.boorudownloader.dl;

import ru.dkx86.boorudownloader.Herald;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;

public class ImageThread extends Thread {

    private final QueueFile queueFile;

    public ImageThread(String name, QueueFile queueFile) {
        super(name);
        this.queueFile = queueFile;
    }

    @Override
    public void run() {
        while (true) {
            var nextLink = queueFile.getNextLink();
            if (nextLink == null) break;

            if (downloadFile(nextLink.getPath(), nextLink.getUrl()))
                Herald.say("Downloaded file: " + nextLink.getPath());

            try {
                synchronized (queueFile) {
                    queueFile.notify();
                    queueFile.wait(2000L);
                }
            } catch (InterruptedException e) {
                Herald.err(e.getMessage());
            }
        }
    }

    private boolean downloadFile(Path destinationFile, URL url) {
        try {
            ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(destinationFile.toFile());
            FileChannel fileChannel = fileOutputStream.getChannel();
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            return true;
        } catch (IOException e) {
            Herald.err("Cannot download file '" + url + "' ERROR: " + e.getMessage());
        }

        return false;
    }


}

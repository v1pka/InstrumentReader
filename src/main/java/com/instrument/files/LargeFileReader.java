package com.instrument.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.concurrent.BlockingQueue;

/**
 * Created by ipopkov on 06/06/15.
 */
public class LargeFileReader implements Runnable {

    private final File file;
    private final BlockingQueue<String> linesRead;
    public volatile boolean isCompleted = false;
    private final Long QUEUE_LIMIT = 100000L;

    public LargeFileReader(File file, BlockingQueue<String> linesRead) {
        this.file = file;
        this.linesRead = linesRead;
    }

    @Override
    public void run() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {

                linesRead.put(line);

                //Prevent OoM in queue
                if (linesRead.size() >= QUEUE_LIMIT) {
                    synchronized (QUEUE_LIMIT) {
                        QUEUE_LIMIT.wait(50);
                    }
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        isCompleted = true;
    }
}

package pl.gesieniec.mpw_server.service;

import pl.gesieniec.mpw_server.model.Disc;
import pl.gesieniec.mpw_server.model.QueuedUser;
import pl.gesieniec.mpw_server.task.SaveFileTask;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.stereotype.Service;

@Service
public class StoreService {

    private final static int NUMBER_OF_DISCS = 5;
    private ExecutorService pool;
    private BlockingQueue<Disc> discs = new LinkedBlockingQueue<>(5);

    public StoreService() {
        pool = Executors.newFixedThreadPool(NUMBER_OF_DISCS);
        for (int i = 0; i < NUMBER_OF_DISCS; i++) {
            discs.add(new Disc(i));
        }
    }


    public void storeFile(QueuedUser queuedUser) throws IOException, InterruptedException {
        Thread.sleep(2000);

        //kolejka ze wszyrtkim taskami
        // lista active users


        SaveFileTask saveFileTask = new SaveFileTask(discs, queuedUser);
        pool.submit(new SaveFileTask()).cancel()

//        queuedUser.getFile().transferTo(Paths.get("src/main/resources/storage/disc1", queuedUser.getFile().getOriginalFilename()));
//        updateCsvFile(queuedUser.getFile().getOriginalFilename(), queuedUser.getUser(), 1);

    }



    private void handleStoringData() {


    }


}

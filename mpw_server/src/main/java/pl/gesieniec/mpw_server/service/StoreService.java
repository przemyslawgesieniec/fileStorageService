package pl.gesieniec.mpw_server.service;

import pl.gesieniec.mpw_server.model.Disc;
import pl.gesieniec.mpw_server.model.QueuedUserRequest;
import pl.gesieniec.mpw_server.task.SaveFileTask;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoreService {

    @Autowired
    private TaskDispatcherService taskDispatcherService;

    private BlockingQueue<Disc> discs = new LinkedBlockingQueue<>(5);

    public StoreService() {

    }


    public void storeFile(QueuedUserRequest queuedUserRequest) throws IOException, InterruptedException {

        //kolejka ze wszyrtkim taskami
        // lista active users

        taskDispatcherService.submitNewTaskRequest(queuedUserRequest);

//        SaveFileTask saveFileTask = new SaveFileTask(discs, queuedUserRequest);
//        pool.submit(new SaveFileTask()).cancel()

//        queuedUserRequest.getFile().transferTo(Paths.get("src/main/resources/storage/disc1", queuedUserRequest.getFile().getOriginalFilename()));
//        updateCsvFile(queuedUserRequest.getFile().getOriginalFilename(), queuedUserRequest.getUser(), 1);

    }



    private void handleStoringData() {


    }


}
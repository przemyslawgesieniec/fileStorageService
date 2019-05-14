package pl.gesieniec.mpw_server.service;

import pl.gesieniec.mpw_server.model.Disc;
import pl.gesieniec.mpw_server.model.QueuedUserRequest;
import pl.gesieniec.mpw_server.task.SaveFileTask;
import pl.gesieniec.mpw_server.task.Task;
import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import org.springframework.stereotype.Service;

@Service
public class TaskDispatcherService {

    private final static int NUMBER_OF_DISCS = 5;

    private BlockingQueue<Task> tasks;
    //    private Map<String, Integer> activeUsers = new ConcurrentHashMap<>();
    private BlockingQueue<Disc> discs = new LinkedBlockingQueue<>(5);
    private ExecutorService pool;


    public TaskDispatcherService() {
        pool = Executors.newFixedThreadPool(NUMBER_OF_DISCS);
        for (int i = 0; i < NUMBER_OF_DISCS; i++) {
            discs.add(new Disc(i));
        }

        final Comparator<Task> taskPriority = Comparator.comparing(Task::getRequestPriority).reversed();
        tasks = new PriorityBlockingQueue<>(30, taskPriority);

    }

    public void submitNewTaskRequest(QueuedUserRequest queuedUserRequest) {

        tasks.add(new SaveFileTask(queuedUserRequest));
    }

    public Disc dispatchDiscForTask() {

    }
}

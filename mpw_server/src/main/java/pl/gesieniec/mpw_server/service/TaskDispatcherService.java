package pl.gesieniec.mpw_server.service;

import pl.gesieniec.mpw_server.model.QueuedUserRequest;
import pl.gesieniec.mpw_server.task.SaveFileTask;
import pl.gesieniec.mpw_server.task.Task;
import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskDispatcherService {

    private final static int NUMBER_OF_DISCS = 5;
    private static final int EXECUTION_OVER = 0;

    private BlockingQueue<Task> taskQueue;
    private ExecutorService pool;

    @Autowired
    private StoreService storeService;


    public TaskDispatcherService() {
        pool = Executors.newFixedThreadPool(NUMBER_OF_DISCS);

        final Comparator<Task> taskPriority = Comparator.comparing(Task::getRequestPriority).reversed();
        taskQueue = new PriorityBlockingQueue<>(30, taskPriority);
        executeTasks();

    }

    public void submitNewTaskRequest(QueuedUserRequest queuedUserRequest) {

        final int resourceAllocationTime = calculateTaskResourceAllocationTime();
        final Long requestPriority = calculateTaskPriority();

        final SaveFileTask saveFileTask = new SaveFileTask(storeService, queuedUserRequest, resourceAllocationTime, requestPriority);

        taskQueue.add(saveFileTask);
    }

    private void executeTasks() {

        new Thread(()->{
            while (true) {
                if (!taskQueue.isEmpty()) {
                    final Task polledTask = taskQueue.poll();
                    System.out.println("new task in queue: " + polledTask.getUserRequestDetails().getUser());
                    final int remainingExecutionTime = calculateRemainingExecutionTime(polledTask);
                    polledTask.getUserRequestDetails().setFileSavingTime(remainingExecutionTime);
                    pool.submit(polledTask);

//                    if (remainingExecutionTime > 0) {
//                        //todo modify priority
//                        //todo update csv file
//                        taskQueue.add(polledTask);
//                    }
//                    else{
////                    TODO notify client (add to some kind of "done" collection
////                     so that user can track if the file had been stored
//                    }
                }
        }
        }).start();

    }


    private int calculateTaskResourceAllocationTime() {

        //TODO algorithm
        return 10;
    }

    private Long calculateTaskPriority() {

        //TODO algorithm
        return 1L;
    }

    private int calculateRemainingExecutionTime(Task task) {

        final int taskExecutionTimeLeft = task.getUserRequestDetails().getFileSavingTime();
        final int allowedExecutionTime = task.getAllowedExecutionTime();
        final int remainingTime = taskExecutionTimeLeft - allowedExecutionTime;
        return remainingTime > 0 ? remainingTime : EXECUTION_OVER;
    }


}

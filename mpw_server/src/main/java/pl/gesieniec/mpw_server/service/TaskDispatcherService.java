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
    private static final int EXECUTION_OVER = 0;

    private BlockingQueue<Task> taskQueue;
    private final BlockingQueue<Disc> discs = new LinkedBlockingQueue<>(5);
    private ExecutorService pool;


    public TaskDispatcherService() {
        pool = Executors.newFixedThreadPool(NUMBER_OF_DISCS);
        for (int i = 0; i < NUMBER_OF_DISCS; i++) {
            discs.add(new Disc(i));
        }

        final Comparator<Task> taskPriority = Comparator.comparing(Task::getRequestPriority).reversed();
        taskQueue = new PriorityBlockingQueue<>(30, taskPriority);

        executeTasks();

    }

    public void submitNewTaskRequest(QueuedUserRequest queuedUserRequest) {

        final int resourceAllocationTime = calculateTaskResourceAllocationTime();
        final Long requestPriority = calculateTaskPriority();

        final SaveFileTask saveFileTask = new SaveFileTask(queuedUserRequest, resourceAllocationTime, requestPriority);

        taskQueue.add(saveFileTask);
    }

    private void executeTasks() {

        while (true) {
            if (!taskQueue.isEmpty()) {
                final Task polledTask = taskQueue.poll();
                final int remainingExecutionTime = calculateRemainingExecutionTime(polledTask);
                polledTask.getUserRequestDetails().setExecutionTimeLeft(remainingExecutionTime);
                pool.submit(polledTask);

                if (remainingExecutionTime > 0) {
                    taskQueue.add(polledTask);
                }
            }
        }
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

        final int taskExecutionTimeLeft = task.getUserRequestDetails().getExecutionTimeLeft();
        final int allowedExecutionTime = task.getAllowedExecutionTime();
        final int remainingTime = taskExecutionTimeLeft - allowedExecutionTime;
        return remainingTime > 0 ? remainingTime : EXECUTION_OVER;
    }

//np> prioryted wyzszy im nizsza jego wartosc. biore timestampa w longu.
// im mniejszy plik to dziele tę wartość na 2,3,4,5 itp. Im wiecej



}

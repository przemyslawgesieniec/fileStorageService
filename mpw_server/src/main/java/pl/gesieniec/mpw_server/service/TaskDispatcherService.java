package pl.gesieniec.mpw_server.service;

import lombok.Getter;
import pl.gesieniec.mpw_server.model.QueuedUserRequest;
import pl.gesieniec.mpw_server.task.SaveFileTask;
import pl.gesieniec.mpw_server.task.Task;
import java.lang.management.ManagementFactory;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskDispatcherService {

    private final static int NUMBER_OF_DISCS = 5;
    private static Map<String, Integer> userRequestsCounter;
    private static long timeReferenceValue = 0L;

    @Getter
    private BlockingQueue<Task> taskQueue;
    private ExecutorService pool;

    @Autowired
    private StoreService storeService;


    public TaskDispatcherService() {
        pool = Executors.newFixedThreadPool(NUMBER_OF_DISCS);

        userRequestsCounter = new ConcurrentHashMap<>();

        final Comparator<Task> taskPriority = Comparator.comparing(Task::getRequestPriority);
        taskQueue = new PriorityBlockingQueue<>(30, taskPriority);
        executeTasks();

    }

//    public List<String> findAllFileNamesForUser(final String user){
//
//
//    }

    public void submitNewTaskRequest(QueuedUserRequest queuedUserRequest) {

        final Long taskPriority = calculateTaskPriority(queuedUserRequest);
        final SaveFileTask saveFileTask = new SaveFileTask(storeService, queuedUserRequest, taskPriority);

        taskQueue.add(saveFileTask);
    }

    private void executeTasks() {

        new Thread(() -> {
            while (true) {
                if (!taskQueue.isEmpty()) {
                    final Task polledTask = taskQueue.poll();
                    System.out.println("new task in queue: " + polledTask.getUserRequestDetails().getUser());
                    pool.submit(polledTask);
                }
//                else {
////                    resetPriorityFactors();
////                }
            }
        }).start();

    }

//    private void resetPriorityFactors() {
//
//        System.out.println("REFERENCE FACTORS RESET");
//        userRequestsCounter.clear();
//        timeReferenceValue = ManagementFactory.getRuntimeMXBean().getUptime()/1000;
//    }


    private Long calculateTaskPriority(QueuedUserRequest queuedUserRequest) {

        final long uptimeInSeconds = getTaskArrivalTimeReference();

        final long numberOfUsersRequest = getNumberOfUsersRequest(queuedUserRequest.getUser());
        final long calculatedFileSizePriorityFactor = calculateFileSizePriorityFactor(queuedUserRequest.getFileSavingTime());

        System.out.println("numberOfUsersRequest: " + numberOfUsersRequest);
        return uptimeInSeconds + numberOfUsersRequest*numberOfUsersRequest + calculatedFileSizePriorityFactor;
    }

    private int calculateFileSizePriorityFactor(int savingTime) {

        if (savingTime < 10) {
            return 0;
        } else if (savingTime < 20) {
            return 10;
        } else {
            return 20;
        }
    }

    private int getNumberOfUsersRequest( final String userName){

        final int numberOfUserRequests = 1 + Optional.ofNullable(userRequestsCounter
                .get(userName))
                .orElse(0);
        userRequestsCounter.put(userName,numberOfUserRequests);

        return numberOfUserRequests;
    }

    private long getTaskArrivalTimeReference(){
        return ManagementFactory.getRuntimeMXBean().getUptime()/1000 - timeReferenceValue;
    }

}

package pl.gesieniec.mpw_server.service;

import lombok.Getter;
import pl.gesieniec.mpw_server.model.QueuedUserDownloadRequest;
import pl.gesieniec.mpw_server.model.QueuedUserUploadRequest;
import pl.gesieniec.mpw_server.task.SaveFileTask;
import pl.gesieniec.mpw_server.task.Task;
import java.lang.management.ManagementFactory;
import java.util.Comparator;
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
    private BlockingQueue<Task> uploadQueue;
    private BlockingQueue<Task> downloadQueue;

    private ExecutorService uploadPool;
    private ExecutorService downloadPool;

    @Autowired
    private StoreService storeService;

    @Autowired
    private DownloadService downloadService;


    public TaskDispatcherService() {
        uploadPool = Executors.newFixedThreadPool(NUMBER_OF_DISCS);
        downloadPool = Executors.newFixedThreadPool(NUMBER_OF_DISCS);

        userRequestsCounter = new ConcurrentHashMap<>();

        final Comparator<Task> taskPriority = Comparator.comparing(Task::getRequestPriority);
        uploadQueue = new PriorityBlockingQueue<>(30, taskPriority);
        downloadQueue = new PriorityBlockingQueue<>(30, taskPriority);
        executeTasks();

    }

    public void submitNewUploadRequest(final QueuedUserUploadRequest queuedUserRequest) {

        final Long taskPriority = calculateTaskPriority(queuedUserRequest.getUser(), queuedUserRequest.getFileProcessingTime());
        System.out.println("TaskDispatcherService:UPLOAD:::File " + queuedUserRequest.getUserFileData().getServerFileName() + "of user: " + queuedUserRequest.getUser() + " has priority: " + taskPriority);
        final SaveFileTask saveFileTask = new SaveFileTask(storeService, queuedUserRequest, taskPriority);
        uploadQueue.add(saveFileTask);
    }

    public void submitNewDownloadRequest(final QueuedUserDownloadRequest queuedUserRequest) {

        queuedUserRequest.getFilesProcessingTime().forEach((serverFileName, processingTime) -> {
            final Long taskPriority = calculateTaskPriority(queuedUserRequest.getUser(), processingTime);
            System.out.println("TaskDispatcherService:DOWNLOAD:::File " + serverFileName + "of user: " + queuedUserRequest.getUser() + " has priority: " + taskPriority);

//            downloadQueue.add()
        });


    }

    private void executeTasks() {

        new Thread(() -> {
            while (true) {
                if (!uploadQueue.isEmpty()) {
                    final Task polledTask = uploadQueue.poll();
                    System.out.println("TaskDispatcherService::::new task of user " + polledTask.getUserRequestDetails().getUser() + " in queue: ");
                    uploadPool.submit(polledTask);
                }
            }
        }).start();

    }

    private Long calculateTaskPriority(final String userName, final int processingTime) {

        final long uptimeInSeconds = getTaskArrivalTimeReference();

        final long numberOfUsersRequest = getNumberOfUsersRequest(userName);
        final long calculatedFileSizePriorityFactor = calculateFileSizePriorityFactor(processingTime);

        return uptimeInSeconds + 2 * numberOfUsersRequest * numberOfUsersRequest + calculatedFileSizePriorityFactor;
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

    private int getNumberOfUsersRequest(final String userName) {

        final int numberOfUserRequests = 1 + Optional.ofNullable(userRequestsCounter
                .get(userName))
                .orElse(0);
        userRequestsCounter.put(userName, numberOfUserRequests);

        return numberOfUserRequests;
    }

    private long getTaskArrivalTimeReference() {
        return ManagementFactory.getRuntimeMXBean().getUptime() / 1000 - timeReferenceValue;
    }

}

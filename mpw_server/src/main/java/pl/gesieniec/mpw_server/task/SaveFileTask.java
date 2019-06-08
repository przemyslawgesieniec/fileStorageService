package pl.gesieniec.mpw_server.task;

import pl.gesieniec.mpw_server.model.QueuedUserRequest;
import pl.gesieniec.mpw_server.service.StoreService;

public class SaveFileTask implements Task {

    private StoreService storeService;
    private QueuedUserRequest queuedUserRequest;
    private int allowedExecutionTime;
    private Long requestPriority;

    public SaveFileTask(StoreService storeService, QueuedUserRequest queuedUserRequest, int allowedExecutionTime, Long requestPriority) {
        this.queuedUserRequest = queuedUserRequest;
        this.allowedExecutionTime = allowedExecutionTime;
        this.requestPriority = requestPriority;
        this.storeService = storeService;
    }

    @Override
    public void run() {
        System.out.println("send for storing");
        storeService.storeFile(queuedUserRequest);
    }

    @Override
    public Long getRequestPriority() {

        return requestPriority;
    }

    @Override
    public QueuedUserRequest getUserRequestDetails() {
        return queuedUserRequest;
    }

    @Override
    public int getAllowedExecutionTime() {
        return allowedExecutionTime;
    }


}

package pl.gesieniec.mpw_server.task;

import pl.gesieniec.mpw_server.model.QueuedUserRequest;
import pl.gesieniec.mpw_server.service.StoreService;

import org.springframework.beans.factory.annotation.Autowired;


public class SaveFileTask implements Task {

    @Autowired
    StoreService storeService;

    private QueuedUserRequest queuedUserRequest;
    private int allowedExecutionTime;
    private Long requestPriority;

    public SaveFileTask(QueuedUserRequest queuedUserRequest, int allowedExecutionTime, Long requestPriority) {
        this.queuedUserRequest = queuedUserRequest;
        this.allowedExecutionTime = allowedExecutionTime;
        this.requestPriority = requestPriority;
    }

    @Override
    public void run() {

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

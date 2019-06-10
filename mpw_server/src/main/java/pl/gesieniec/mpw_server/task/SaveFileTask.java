package pl.gesieniec.mpw_server.task;

import pl.gesieniec.mpw_server.model.QueuedUserRequest;
import pl.gesieniec.mpw_server.service.StoreService;

public class SaveFileTask implements Task {

    private StoreService storeService;
    private QueuedUserRequest queuedUserRequest;
    private Long requestPriority;

    public SaveFileTask(StoreService storeService, QueuedUserRequest queuedUserRequest, Long requestPriority) {
        this.queuedUserRequest = queuedUserRequest;
        this.requestPriority = requestPriority;
        this.storeService = storeService;
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


}

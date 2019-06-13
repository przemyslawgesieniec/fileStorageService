package pl.gesieniec.mpw_server.task;

import pl.gesieniec.mpw_server.model.QueuedUserRequest;
import pl.gesieniec.mpw_server.model.QueuedUserUploadRequest;
import pl.gesieniec.mpw_server.model.UserFileData;
import pl.gesieniec.mpw_server.service.StoreService;

public class SaveFileTask implements Task {

    private StoreService storeService;
    private QueuedUserUploadRequest queuedUserRequest;
    private Long requestPriority;

    public SaveFileTask(StoreService storeService, QueuedUserUploadRequest queuedUserRequest, Long requestPriority) {
        this.queuedUserRequest = queuedUserRequest;
        this.requestPriority = requestPriority;
        this.storeService = storeService;
    }


    @Override
    public Long getRequestPriority() {
        return requestPriority;
    }

    @Override
    public QueuedUserRequest getQueuedUserRequest() {
        return queuedUserRequest;
    }


    @Override
    public UserFileData call() throws Exception {
        storeService.storeFile(queuedUserRequest);
        return null;
    }
}

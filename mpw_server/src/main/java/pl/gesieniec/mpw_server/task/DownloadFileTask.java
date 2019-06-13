package pl.gesieniec.mpw_server.task;

import pl.gesieniec.mpw_server.model.QueuedUserDownloadRequest;
import pl.gesieniec.mpw_server.model.QueuedUserRequest;
import pl.gesieniec.mpw_server.model.UserFileData;
import pl.gesieniec.mpw_server.service.DownloadService;

public class DownloadFileTask implements Task {


    private QueuedUserDownloadRequest queuedUserDownloadRequest;
    private Long requestPriority;
    private DownloadService downloadService;

    public DownloadFileTask(final QueuedUserDownloadRequest queuedUserDownloadRequest,
                            final Long requestPriority, DownloadService downloadService) {
        this.queuedUserDownloadRequest = queuedUserDownloadRequest;
        this.requestPriority = requestPriority;
        this.downloadService = downloadService;
    }

    @Override
    public Long getRequestPriority() {
        return requestPriority;
    }

    @Override
    public QueuedUserRequest getQueuedUserRequest() {
        return queuedUserDownloadRequest;
    }

    @Override
    public UserFileData call() throws Exception {
        return downloadService.readRequestedFile(queuedUserDownloadRequest);
    }
}

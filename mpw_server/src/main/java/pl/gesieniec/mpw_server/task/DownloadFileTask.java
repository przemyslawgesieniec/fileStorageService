package pl.gesieniec.mpw_server.task;

import pl.gesieniec.mpw_server.model.QueuedUserDownloadRequest;
import pl.gesieniec.mpw_server.model.QueuedUserRequest;
import pl.gesieniec.mpw_server.service.DownloadService;

public class DownloadFileTask implements Task {


    private DownloadService downloadService;
    private QueuedUserDownloadRequest queuedUserDownloadRequest;
    private Long requestPriority;

    public DownloadFileTask(final DownloadService downloadService,
                            final QueuedUserDownloadRequest queuedUserDownloadRequest,
                            final Long requestPriority) {
        this.downloadService = downloadService;
        this.queuedUserDownloadRequest = queuedUserDownloadRequest;
        this.requestPriority = requestPriority;
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
    public void run() {

    }
}

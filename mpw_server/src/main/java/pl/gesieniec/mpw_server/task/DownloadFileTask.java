package pl.gesieniec.mpw_server.task;

import pl.gesieniec.mpw_server.model.QueuedUserUploadRequest;
import pl.gesieniec.mpw_server.service.DownloadService;

public class DownloadFileTask implements Task {


    private DownloadService downloadService;

    @Override
    public Long getRequestPriority() {
        return null;
    }

    @Override
    public QueuedUserUploadRequest getUserRequestDetails() {
        return null;
    }

    @Override
    public void run() {

    }
}

package pl.gesieniec.mpw_server.task;

import pl.gesieniec.mpw_server.model.QueuedUserUploadRequest;

public interface Task extends Runnable {

    Long getRequestPriority();
    QueuedUserUploadRequest getUserRequestDetails();
}

package pl.gesieniec.mpw_server.task;

import pl.gesieniec.mpw_server.model.QueuedUserRequest;

public interface Task extends Runnable {

    Long getRequestPriority();
    QueuedUserRequest getQueuedUserRequest();
}

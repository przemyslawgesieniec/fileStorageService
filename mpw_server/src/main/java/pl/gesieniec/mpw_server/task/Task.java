package pl.gesieniec.mpw_server.task;

import pl.gesieniec.mpw_server.model.QueuedUserRequest;
import pl.gesieniec.mpw_server.model.UserFileData;
import java.util.concurrent.Callable;

//public interface Task extends Runnable {
public interface Task extends Callable<UserFileData>  {

    Long getRequestPriority();
    QueuedUserRequest getQueuedUserRequest();
}

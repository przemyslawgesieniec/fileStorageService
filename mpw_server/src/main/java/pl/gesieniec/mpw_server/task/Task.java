package pl.gesieniec.mpw_server.task;

public interface Task extends Runnable {

    Long getRequestPriority();
}

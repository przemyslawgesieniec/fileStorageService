package pl.gesieniec.mpw_server.task;

import lombok.Getter;
import org.apache.logging.log4j.util.PropertySource;
import pl.gesieniec.mpw_server.model.Disc;
import pl.gesieniec.mpw_server.model.QueuedUserRequest;
import pl.gesieniec.mpw_server.service.TaskDispatcherService;

import java.time.Instant;
import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;

@Getter
public class SaveFileTask implements Task  {

    @Autowired
    TaskDispatcherService taskDispatcherService;

    private QueuedUserRequest queuedUserRequest;
    private Disc disc;

    public SaveFileTask(QueuedUserRequest queuedUserRequest) {
        this.queuedUserRequest = queuedUserRequest;
    }



    @Override
    public void run() {

        try {
            final Disc disc = taskDispatcherService.dispatchDiscForTask();
            discs.add(disc);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public Long getRequestPriority(){

        final long requestInQueueTime = Instant.now().toEpochMilli() - queuedUserRequest.getRequestTimestamp();
        return requestInQueueTime/queuedUserRequest.getExecutionTimeLeft();
    }

}

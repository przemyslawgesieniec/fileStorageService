package pl.gesieniec.mpw_server.service;

import org.junit.jupiter.api.Test;
import pl.gesieniec.mpw_server.model.QueuedUserUploadRequest;
import pl.gesieniec.mpw_server.model.UserFileData;
import pl.gesieniec.mpw_server.task.Task;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskDispatcherServiceTest {

    //todo: BEFORE TESTING DISABLE 'executeTask()' method

    @Test
    void shouldAddLastRequestFromTheSecondUserInTheMiddleOfTheQueue() {
        //given
        TaskDispatcherService taskDispatcherService = new TaskDispatcherService();
        final List<QueuedUserUploadRequest> queuedUserRequestList = prepareTestData();

        //when
        queuedUserRequestList.forEach(taskDispatcherService::submitNewUploadRequest);

        //then
        final BlockingQueue<Task> taskQueueSnapshot = taskDispatcherService.getUploadQueue();
        final List<String> priorityOrderedUsers = taskQueueSnapshot
                .stream()
                .map(e -> e.getQueuedUserRequest().getUser())
                .collect(Collectors.toList());
        priorityOrderedUsers.forEach(System.out::println);
        assertTrue(priorityOrderedUsers.indexOf("User2") < priorityOrderedUsers.size() - 1);

    }

    public List<QueuedUserUploadRequest> prepareTestData() {
        List<QueuedUserUploadRequest> queuedUserRequestList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            UserFileData userFileData = new UserFileData("file.txt", 10000, "example content");
            QueuedUserUploadRequest queuedUserRequest = new QueuedUserUploadRequest("User1", userFileData);
            queuedUserRequestList.add(queuedUserRequest);
        }

        UserFileData userFileData = new UserFileData("file.txt", 10000, "example content");
        QueuedUserUploadRequest queuedUserRequest = new QueuedUserUploadRequest("User2", userFileData);
        queuedUserRequestList.add(queuedUserRequest);

        return queuedUserRequestList;
    }

}
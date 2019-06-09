package pl.gesieniec.mpw_server.service;

import org.junit.jupiter.api.Test;
import pl.gesieniec.mpw_server.model.QueuedUserRequest;
import pl.gesieniec.mpw_server.model.UserFileData;
import pl.gesieniec.mpw_server.task.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;
import static org.junit.Assert.assertThat;


import static org.junit.jupiter.api.Assertions.*;

class TaskDispatcherServiceTest {

    //todo: BEFORE TESTING DISABLE 'executeTask()' method

    @Test
    void shouldAddLastRequestFromTheSecondUserInTheMiddleOfTheQueue(){
        //given
        TaskDispatcherService taskDispatcherService = new TaskDispatcherService();
        final List<QueuedUserRequest> queuedUserRequestList = prepareTestData();

        //when
        queuedUserRequestList.forEach(taskDispatcherService::submitNewTaskRequest);

        //then
        final BlockingQueue<Task> taskQueueSnapshot = taskDispatcherService.getTaskQueue();
        final List<String> priorityOrderedUsers = taskQueueSnapshot.stream().map(e->e.getUserRequestDetails().getUser()).collect(Collectors.toList());
        priorityOrderedUsers.forEach(System.out::println);
        assertTrue(priorityOrderedUsers.indexOf("User2") < priorityOrderedUsers.size()-1);

    }

    public List<QueuedUserRequest> prepareTestData(){
        List<QueuedUserRequest> queuedUserRequestList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            UserFileData userFileData = new UserFileData("file.txt",10000, "example content");
            QueuedUserRequest queuedUserRequest = new QueuedUserRequest("User1", userFileData);
            queuedUserRequestList.add(queuedUserRequest);
        }

        UserFileData userFileData = new UserFileData("file.txt",10000, "example content");
        QueuedUserRequest queuedUserRequest = new QueuedUserRequest("User2", userFileData);
        queuedUserRequestList.add(queuedUserRequest);

        return queuedUserRequestList;
    }

}
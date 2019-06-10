package pl.gesieniec.mpw_server.model;

import lombok.Getter;
import java.util.Random;
import java.util.UUID;

@Getter
public class QueuedUserRequest {

    private String user;
    private UUID requestID;
    private UserFileData userFileData;
    private int fileSavingTime;


    public QueuedUserRequest(String user, UserFileData userFileData) {

        this.requestID = UUID.randomUUID();
        this.user = user;
        this.fileSavingTime = (new Random().nextInt(22) + 8) * 1000; //8s - 30s
        this.userFileData = userFileData;
    }
}

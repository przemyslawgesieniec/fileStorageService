package pl.gesieniec.mpw_server.model;

import lombok.Getter;
import java.util.Random;
import java.util.UUID;

@Getter
public class QueuedUserUploadRequest implements QueuedUserRequest{

    private String user;
    private UUID requestID;
    private UserFileData userFileData;
    private int fileProcessingTime;


    public QueuedUserUploadRequest(String user, UserFileData userFileData) {

        this.requestID = UUID.randomUUID();
        this.user = user;
        this.fileProcessingTime = (new Random().nextInt(22) + 8) * 1000; //8s - 30s
        this.userFileData = userFileData;
    }

    @Override
    public String getFileName() {
        return userFileData.getOriginalFileName();
    }
}

package pl.gesieniec.mpw_server.model;

import lombok.Getter;
import java.util.Random;
import java.util.UUID;

@Getter
public class QueuedUserDownloadRequest implements QueuedUserRequest {

    private String user;
    private UUID requestID;
    private String fileServerName;
    private int fileProcessingTime;

    public QueuedUserDownloadRequest(final String user,
                                     final String fileServerName) {

        this.requestID = UUID.randomUUID();
        this.user = user;
        this.fileServerName = fileServerName;
        this.fileProcessingTime = (new Random().nextInt(22) + 8) * 1000; //8s - 30s
    }

    @Override
    public int getFileProcessingTime() {
        return fileProcessingTime;
    }

    @Override
    public String getFileName() {
        return fileServerName;
    }

}

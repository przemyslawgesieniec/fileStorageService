package pl.gesieniec.mpw_server.model;

import lombok.Getter;
import lombok.Setter;
import java.time.Instant;
import java.util.Random;
import java.util.UUID;

@Getter
public class QueuedUserRequest {

    private String user;
    private Long requestTimestamp;
    private UUID requestID;
    private UserFileData userFileData;

    @Setter
    private int fileSavingTime;

    @Setter
    private boolean isBoundToDisc;


    public QueuedUserRequest(String user, UserFileData userFileData) {

        this.requestID = UUID.randomUUID();
        this.user = user;
        this.requestTimestamp = Instant.now().toEpochMilli();
        this.fileSavingTime = (new Random().nextInt(9) + 20) * 1000; //9s - 30s
        this.isBoundToDisc = false;
        this.userFileData = userFileData;
    }
}

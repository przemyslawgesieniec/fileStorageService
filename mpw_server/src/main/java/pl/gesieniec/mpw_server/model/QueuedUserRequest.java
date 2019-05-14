package pl.gesieniec.mpw_server.model;

import lombok.Getter;
import lombok.Setter;
import java.time.Instant;
import java.util.Random;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

@Getter
public class QueuedUserRequest {

    private MultipartFile file;
    private String user;
    private Long requestTimestamp;
    private UUID requestID;

    @Setter
    private int executionTimeLeft;

    @Setter
    private boolean isBoundToDisc;


    public QueuedUserRequest(MultipartFile file, String user) {
        this.requestID = UUID.randomUUID();
        this.file = file;
        this.user = user;
        this.requestTimestamp = Instant.now().toEpochMilli();
        this.executionTimeLeft = (new Random().nextInt(9) + 30) * 1000; //9s - 30s
        this.isBoundToDisc = false;
    }


}

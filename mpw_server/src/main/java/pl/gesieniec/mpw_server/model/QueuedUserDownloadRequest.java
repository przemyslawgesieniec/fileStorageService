package pl.gesieniec.mpw_server.model;

import lombok.Getter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Getter
public class QueuedUserDownloadRequest {

    private String user;
    private UUID requestID;
    private List<String> userFilesDownloadList;
    private Map<String, Integer> filesProcessingTime;

    public QueuedUserDownloadRequest(final String user,
                                     final List<String> userFilesDownloadList) {

        filesProcessingTime = new HashMap<>();
        this.requestID = UUID.randomUUID();
        this.user = user;
        userFilesDownloadList.forEach(e -> filesProcessingTime.put(e, (new Random().nextInt(22) + 8) * 1000));
        this.userFilesDownloadList = userFilesDownloadList;
    }

}

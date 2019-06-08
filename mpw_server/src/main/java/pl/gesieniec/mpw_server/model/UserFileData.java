package pl.gesieniec.mpw_server.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserFileData {

    public UserFileData(String originalFileName, long fileSize, String content) {
        this.originalFileName = originalFileName;
        this.serverFileName = UUID.randomUUID().toString() + originalFileName;
        this.fileSize = fileSize;
        this.content = content;
    }

    private String originalFileName;
    private String serverFileName;
    private long fileSize;
    private String content;
}

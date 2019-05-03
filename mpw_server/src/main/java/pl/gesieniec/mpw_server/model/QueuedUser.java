package pl.gesieniec.mpw_server.model;

import lombok.Getter;
import lombok.Setter;
import java.util.Random;

import org.springframework.web.multipart.MultipartFile;

@Getter
public class QueuedUser {

    private MultipartFile file;
    private String user;
    @Setter
    private int executionTimeLeft;

    public QueuedUser(MultipartFile file, String user) {
        this.file = file;
        this.user = user;
        executionTimeLeft = (new Random().nextInt(9) + 30) * 1000; //9s - 30s
    }
}

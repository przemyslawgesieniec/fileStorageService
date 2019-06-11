package pl.gesieniec.mpw_server.model;

import lombok.Getter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

@Getter
public class Disc {

    private String path;
    private int discNumber;

    public Disc(int discNumber) {
        this.discNumber = discNumber;
        path = "storage/disc" + discNumber + "/";
    }

    public void save(final QueuedUserUploadRequest userRequest) {

        stubSavingTime(userRequest.getFileProcessingTime());
        saveFile(userRequest.getUserFileData());
        System.out.println("Disc::::file of user: "+userRequest.getUser() + "saved properly");
        updateCsvFile(userRequest.getUserFileData().getOriginalFileName(), userRequest.getUserFileData().getServerFileName(), userRequest.getUser());

    }

    public void read(final QueuedUserUploadRequest queuedUserRequest){

    }

    private synchronized void updateCsvFile(final String originalFileName, final String serverFileName, final String username) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path + "d.csv", true))) {
            writer.append(serverFileName).append(",").append(username).append(",").append(originalFileName);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveFile(final UserFileData userFileData) {

        String filename = StringUtils.cleanPath(userFileData.getServerFileName());
        Path filepath = Paths.get(path + filename);

        try (BufferedWriter writer = Files.newBufferedWriter(filepath)) {
            writer.write(userFileData.getContent());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void stubSavingTime(final int savingTime) {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        System.out.println("Disc::::saving in progress");
        try {
            Thread.sleep(savingTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.stop();
        System.out.println("Disc::::file saved in: " + stopWatch.getTotalTimeMillis()/1000 + "s");
    }
}

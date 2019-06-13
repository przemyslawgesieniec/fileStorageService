package pl.gesieniec.mpw_server.model;

import lombok.Getter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

@Getter
public class Disc {

    private String path;
    private String directoryPath;
    private String csvFilePath;
    private int discNumber;

    public Disc(int discNumber) {
        this.discNumber = discNumber;
        directoryPath = "storage/disc" + discNumber;
        path = directoryPath + "/";
        csvFilePath = path + "d.csv";
    }

    public void save(final QueuedUserUploadRequest userRequest) {

        stubProcessingTime(userRequest.getFileProcessingTime());
        saveFile(userRequest.getUserFileData());
        System.out.println("DISC: file of user: " + userRequest.getUser() + " saved properly");
        updateCsvFile(userRequest.getUserFileData().getOriginalFileName(), userRequest.getUserFileData().getServerFileName(), userRequest.getUser());

    }

    public UserFileData read(final QueuedUserRequest userRequest) {

        stubProcessingTime(userRequest.getFileProcessingTime());
        String fileContent = getFileContent(userRequest.getFileName());
        System.out.println("DISC: file "+userRequest.getFileName() +" of user: " + userRequest.getUser() + " downloaded properly");
        return new UserFileData(userRequest.getFileProcessingTime(), fileContent, userRequest.getFileName());
    }

    private String getFileContent(String fileServerName) {

        File directory = new File(directoryPath);

        final String fileContent = String.join("\n", Arrays.stream(Objects.requireNonNull(directory.listFiles()))
                .filter(file -> file.getName().equals(fileServerName))
                .findFirst().map(file -> {
                    try {
                        return Files.readAllLines(Paths.get(file.toURI()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .get());


        return fileContent;
    }

    private synchronized void updateCsvFile(final String originalFileName, final String serverFileName, final String username) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath, true))) {
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

    private void stubProcessingTime(final int savingTime) {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        System.out.println("DISC: processing file");
        try {
            Thread.sleep(savingTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.stop();
        System.out.println("DISC: file processed in: " + stopWatch.getTotalTimeMillis() / 1000 + "s");
    }

    public synchronized boolean isStoringUserFile(String fileName) {

        try (Stream<String> lines = Files.lines(Paths.get(csvFilePath))) {
            return lines.anyMatch(line -> line.contains(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}

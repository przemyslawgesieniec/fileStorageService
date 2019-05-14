package pl.gesieniec.mpw_server.model;

import lombok.Getter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Getter
public class Disc {

    private String path;
    private int discNumber;
    private BlockingQueue<QueuedUserRequest> userTasks = new LinkedBlockingQueue<>();

    public Disc(int discNumber) {
        this.discNumber = discNumber;
        path = "src/main/resources/storage/disc" + discNumber;
    }

    public void save(final int savingTime) {
        try {
            final QueuedUserRequest queuedUserRequest = userTasks.take();
            final boolean isFullyProcessed = performSaveForAllowedTime(queuedUserRequest, savingTime);

            if (isFullyProcessed) {
                queuedUserRequest.getFile().transferTo(Paths.get(path, queuedUserRequest.getFile().getOriginalFilename()));
                updateCsvFile(queuedUserRequest.getFile().getOriginalFilename(), queuedUserRequest.getUser());
            } else {
                queuedUserRequest.setBoundToDisc(true);
                userTasks.add(queuedUserRequest);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void updateCsvFile(final String fileName, final String username) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(path + "/d.csv", true));
            writer.append(fileName + "," + username);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean performSaveForAllowedTime(QueuedUserRequest queuedUserRequest, final int savingTime) throws InterruptedException {

        final int executionTimeLeft = queuedUserRequest.getExecutionTimeLeft();
        if (executionTimeLeft < savingTime) {
            Thread.sleep(executionTimeLeft);
            return true;
        }
        queuedUserRequest.setExecutionTimeLeft(executionTimeLeft - savingTime);
        Thread.sleep(savingTime);
        return false;
    }
}

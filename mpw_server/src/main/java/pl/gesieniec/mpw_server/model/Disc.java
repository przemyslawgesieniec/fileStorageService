package pl.gesieniec.mpw_server.model;

import lombok.Getter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

@Getter
public class Disc {

    private String path;
    private int discNumber;

    public Disc(int discNumber) {
        this.discNumber = discNumber;
        path = "src/main/resources/storage/disc" + discNumber;
    }

    public void save(QueuedUser queuedUser) {
        try {
            int savingTime = 5000;
            Thread.sleep(savingTime);
            queuedUser.setExecutionTimeLeft(queuedUser.getExecutionTimeLeft()-savingTime);
            queuedUser.getFile().transferTo(Paths.get(path, queuedUser.getFile().getOriginalFilename()));
            updateCsvFile(queuedUser.getFile().getOriginalFilename(), queuedUser.getUser());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateCsvFile(String fileName, String username) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(path + "/d.csv", true));
            writer.append(fileName + "," + username);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

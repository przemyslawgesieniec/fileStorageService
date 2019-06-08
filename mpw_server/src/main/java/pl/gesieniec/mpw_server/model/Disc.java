package pl.gesieniec.mpw_server.model;

import lombok.Getter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class Disc {

    private String path;
    private int discNumber;

    public Disc(int discNumber) {
        this.discNumber = discNumber;
        path = "storage/disc" + discNumber + "/";
    }

    public void save(final QueuedUserRequest userRequest) {

//        updateCsvFile(userRequest.getUserFileMetadata().getOriginalFileName(), userRequest.getUserFileMetadata().getServerFileName(), userRequest.getUser());
        stubSavingTime(userRequest.getFileSavingTime());
        saveFile(userRequest.getUserFileData());

    }

    private void updateCsvFile(String originalFileName, final String serverFileName, final String username) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path + "d.csv", true))) {
            writer.append(serverFileName).append(",").append(username).append(",").append(originalFileName);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveFile(UserFileData userFileData) {

        String filename = StringUtils.cleanPath(userFileData.getServerFileName());
        Path filepath = Paths.get(path+filename);



        try (BufferedWriter writer = Files.newBufferedWriter(filepath)) {
            writer.write(userFileData.getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }

//        Files.crea
//
//        try (InputStream inputStream = userFileData.getContent()) {
//            Files.copy(inputStream, filepath.resolve(filename),
//                    StandardCopyOption.REPLACE_EXISTING);
//        } catch (IOException e1) {
//            e1.printStackTrace();
//        }
    }

////
//        try (OutputStream os = Files.newOutputStream(filepath)) {
//            os.write(multipartFile.getBytes());
//        } catch (IOException e) {
//            System.err.println("cannot save multipart file to given location");
//            e.printStackTrace();
//        }


    private void stubSavingTime(int savingTime) {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        System.out.println("saving in progress");
        try {
            Thread.sleep(savingTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.stop();
        System.out.println("file saved in: " + stopWatch.getTotalTimeMillis() + "ms");
    }
}

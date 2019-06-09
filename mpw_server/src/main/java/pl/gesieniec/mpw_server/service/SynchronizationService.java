package pl.gesieniec.mpw_server.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

@Service
public class SynchronizationService {

    private List<String> csvFilesPaths;

    public SynchronizationService() {

        csvFilesPaths = new ArrayList<>();

        for (int i = 1; i < 6; i++) {
            csvFilesPaths.add("storage/disc" + i + "/d.csv");
        }
    }

    public synchronized List<String> getAllUserStoredFiles(final String userName){

        final List<String> fileNamesForUser = new ArrayList<>();

        csvFilesPaths.forEach(path->{

            try (Stream<String> stream = Files.lines(Paths.get(path))) {

                final List<String> collect = stream
                        .filter(e -> userFile(e, userName))
                        .map(SynchronizationService::getFileName)
                        .collect(Collectors.toList());

                fileNamesForUser.addAll(collect);
            } catch (IOException e) {
                e.printStackTrace();
            }});

        return fileNamesForUser;
    }

    private static boolean userFile(String line, String user) {
        final String userName = Arrays.asList(line.split(",")).get(1);
        return userName.equals(user);
    }

    private static String getFileName(String e) {
        return Arrays.asList(e.split(",")).get(0);
    }
}

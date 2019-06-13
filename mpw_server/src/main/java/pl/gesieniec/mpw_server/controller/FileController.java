package pl.gesieniec.mpw_server.controller;

import pl.gesieniec.mpw_server.model.QueuedUserDownloadRequest;
import pl.gesieniec.mpw_server.model.QueuedUserUploadRequest;
import pl.gesieniec.mpw_server.model.UserFileData;
import pl.gesieniec.mpw_server.service.SynchronizationService;
import pl.gesieniec.mpw_server.service.TaskDispatcherService;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.ResponseEntity.ok;

@RestController
public class FileController {

    @Autowired
    private TaskDispatcherService taskDispatcherService;

    @Autowired
    private SynchronizationService synchronizationService;

    @PostMapping("/upload")
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("user") String user)
            throws IOException {

        UserFileData userFileData = new UserFileData(file.getOriginalFilename(), file.getSize(), new String(file.getBytes()));
        QueuedUserUploadRequest queuedUserRequest = new QueuedUserUploadRequest(user, userFileData);
        taskDispatcherService.submitNewUploadRequest(queuedUserRequest);
        return ok().body(userFileData);
    }

    @GetMapping("/sync")
    public List<String> getListOfRemotelyStoredFiles(@RequestParam("user") final String user) {
        System.out.println("FILE CONTROLLER: user "+ user + " sync");

        final List<String> allUserStoredFiles = synchronizationService.getAllUserStoredFiles(user);
        return allUserStoredFiles;
    }


    @GetMapping("/request/download")
    public ResponseEntity requestDownloadingFiles(@RequestParam("user") final String user, @RequestParam("filesNames") final List<String> filesNames) {


        filesNames.forEach(e -> {
            final String trimmedString = e.replaceAll("\"", "");
            QueuedUserDownloadRequest queuedUserDownloadRequest = new QueuedUserDownloadRequest(user, trimmedString);
            taskDispatcherService.submitNewDownloadRequest(queuedUserDownloadRequest);
        });

        return ok().body("processing");
    }

    @GetMapping("/download")
    public ResponseEntity<List<UserFileData>> downloadAnyOfRequested(@RequestParam("filesNames") final List<String> filesNames) throws InterruptedException {

        final List<UserFileData> userFileData = taskDispatcherService.tryToDownload(filesNames);
        return ok(userFileData);
    }

}

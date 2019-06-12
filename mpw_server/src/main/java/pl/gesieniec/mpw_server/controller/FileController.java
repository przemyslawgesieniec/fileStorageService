package pl.gesieniec.mpw_server.controller;

import pl.gesieniec.mpw_server.model.QueuedUserDownloadRequest;
import pl.gesieniec.mpw_server.model.QueuedUserUploadRequest;
import pl.gesieniec.mpw_server.model.UserFileData;
import pl.gesieniec.mpw_server.service.SynchronizationService;
import pl.gesieniec.mpw_server.service.TaskDispatcherService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
        return ResponseEntity.ok().body(userFileData);
    }

    @GetMapping("/sync")
    public List<String> getListOfRemotelyStoredFiles(@RequestParam("user") final String user) {

        final List<String> allUserStoredFiles = synchronizationService.getAllUserStoredFiles(user);
        return allUserStoredFiles;
    }


    @GetMapping("/request/download")
    public String requestDownloadingFiles(@RequestParam("user") final String user, @RequestParam("filesNames") final List<String> filesNames) {

        filesNames.forEach(e -> {
            QueuedUserDownloadRequest queuedUserDownloadRequest = new QueuedUserDownloadRequest(user, e);
            taskDispatcherService.submitNewDownloadRequest(queuedUserDownloadRequest);
        });

        return "processing";

    }

    @GetMapping("/download")
    public List<MultipartFile> downloadAnyOfRequested(@RequestParam("user") final String user, @RequestParam("filesNames") final List<String> filesNames) {


        final List<UserFileData> userFileData = taskDispatcherService.tryToDownload(filesNames, user);
        final List<MultipartFile> userRequestedFiles = new ArrayList<>();

        userFileData.forEach(ufd ->
                userRequestedFiles.add(new MockMultipartFile(ufd.getServerFileName(), ufd.getContent().getBytes())));


        return userRequestedFiles;
    }


    @GetMapping("/health")
    public String healthCheck() {
        return "ok";
    }

}

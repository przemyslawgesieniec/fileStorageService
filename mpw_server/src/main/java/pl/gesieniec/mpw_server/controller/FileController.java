package pl.gesieniec.mpw_server.controller;

import pl.gesieniec.mpw_server.model.QueuedUserRequest;
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
        QueuedUserRequest queuedUserRequest = new QueuedUserRequest(user, userFileData);
        taskDispatcherService.submitNewTaskRequest(queuedUserRequest);
        return ResponseEntity.ok().body(userFileData);
    }

    @GetMapping("/sync")
    public List<String> getListOfRemotelyStoredFiles(@RequestParam("user") final String user){

        final List<String> allUserStoredFiles = synchronizationService.getAllUserStoredFiles(user);
        return allUserStoredFiles;
    }



    @GetMapping("/health")
    public String healthCheck() {
        return "ok";
    }

}

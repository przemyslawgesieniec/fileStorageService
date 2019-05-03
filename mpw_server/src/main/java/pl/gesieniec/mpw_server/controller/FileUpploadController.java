package pl.gesieniec.mpw_server.controller;

import pl.gesieniec.mpw_server.service.StoreService;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUpploadController {

    @Autowired
    private StoreService storeService;

    @PostMapping("/upload")
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("user") String user)
            throws InterruptedException, IOException {

        System.out.println("przyszło");
        storeService.storeFile(file, user);
        System.out.println("zapisalo" + file.getOriginalFilename());
        return ResponseEntity.ok().body(file.getName());
    }

}

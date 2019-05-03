package pl.gesieniec.mpw_server.service;

import java.io.IOException;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StoreService {


    public void storeFile(MultipartFile file, String user) throws InterruptedException, IOException {

        Thread.sleep(8000);

        file.transferTo(Paths.get("src/main/resources/storage/disc1", file.getOriginalFilename()));

    }
}

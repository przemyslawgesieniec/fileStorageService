package pl.gesieniec.mpw_server.service;

import pl.gesieniec.mpw_server.model.Disc;
import pl.gesieniec.mpw_server.model.QueuedUserDownloadRequest;
import pl.gesieniec.mpw_server.model.UserFileData;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.stereotype.Service;

@Service
public class DownloadService {

    //method get queue snapshot

    private Queue<Disc> discs = new ConcurrentLinkedQueue<Disc>();

    public DownloadService() {

        for (int i = 0; i < 5; i++) {
            discs.add(new Disc(i + 1));
        }
    }

    public UserFileData readRequestedFile(QueuedUserDownloadRequest queuedUserDownloadRequest){

        final String fileName = queuedUserDownloadRequest.getFileServerName();
        return discs.stream()
                .filter(disc -> disc.isStoringUserFile(fileName))
                .findFirst()
                .map(disc -> disc.read(queuedUserDownloadRequest))
                .get();

    }



}

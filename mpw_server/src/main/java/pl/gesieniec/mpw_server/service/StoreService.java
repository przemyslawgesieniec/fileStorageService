package pl.gesieniec.mpw_server.service;

import pl.gesieniec.mpw_server.model.Disc;
import pl.gesieniec.mpw_server.model.QueuedUserUploadRequest;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.stereotype.Service;

@Service
public class StoreService {

    private BlockingQueue<Disc> discs = new LinkedBlockingQueue<>(5);

    public StoreService() {

        for (int i = 0; i < 5; i++) {
            discs.add(new Disc(i + 1));
        }
    }

    public void storeFile(QueuedUserUploadRequest queuedUserRequest) {

        System.out.println("Store Service::::: available discs: " + discs.size());
        final Disc polledDisc = discs.poll();
        System.out.println("Store Service::::: polled disc:" + polledDisc.getDiscNumber());
        polledDisc.save(queuedUserRequest);
        discs.add(polledDisc);
    }

}

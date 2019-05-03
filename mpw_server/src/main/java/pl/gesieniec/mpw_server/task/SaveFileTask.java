package pl.gesieniec.mpw_server.task;

import pl.gesieniec.mpw_server.model.Disc;
import pl.gesieniec.mpw_server.model.QueuedUser;
import java.util.concurrent.BlockingQueue;

public class SaveFileTask implements Runnable {

    private BlockingQueue<Disc> discs;
    private QueuedUser queuedUser;

    public SaveFileTask(BlockingQueue<Disc> discs, QueuedUser queuedUser) {
        this.discs = discs;
        this.queuedUser = queuedUser;
    }

    @Override
    public void run() {

        try {
            final Disc disc = discs.take();
            disc.save(queuedUser,5000);
            discs.add(disc);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

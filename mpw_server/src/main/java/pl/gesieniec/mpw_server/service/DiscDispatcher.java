package pl.gesieniec.mpw_server.service;

import pl.gesieniec.mpw_server.model.Disc;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class DiscDispatcher {

    List<Disc> discs = new ArrayList<>();

    public DiscDispatcher() {

        for (int i = 0; i < 5; i++) {
            discs.add(new Disc(i));
        }
    }
}

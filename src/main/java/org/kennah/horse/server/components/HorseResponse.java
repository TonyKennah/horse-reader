package org.kennah.horse.server.components;

import java.util.List;
import org.kennah.horse.server.model.Race;

public class HorseResponse {

    private final long id;
    private List<Race> races;

    public HorseResponse(long id, List<Race> races) {
        this.id = id;
        this.races = races;
    }

    public long getId() {
        return id;
    }
    
    public List<Race> getRaces() {
        return races;
    }
}
package org.kennah.horse.server.model;

import java.util.List;

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
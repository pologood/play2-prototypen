package services;

import models.Playground;

public class PlaygroundServiceImpl implements PlaygroundService {

	@Override
	public Playground findByName(String name) {

		Playground playground = Playground.findByName(name);

		return playground;
	}

}

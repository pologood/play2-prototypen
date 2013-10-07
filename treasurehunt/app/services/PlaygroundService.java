package services;

import models.Playground;

public interface PlaygroundService {

	Playground findByName(String name);

}

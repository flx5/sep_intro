package de.unipassau.prassefe.sepintro.model.migrations;

import de.unipassau.prassefe.sepintro.model.config.Config;

public interface Migration {
	long getId();
	void up(Config config);
	void down(Config config);
}

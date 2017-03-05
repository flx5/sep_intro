package de.unipassau.prassefe.sepintro.model.migrations;

import de.unipassau.prassefe.sepintro.model.config.AbstractConfig;

public interface Migration {
	long getId();
	void up(AbstractConfig config);
	void down(AbstractConfig config);
}

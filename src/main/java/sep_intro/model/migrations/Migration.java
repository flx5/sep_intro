package sep_intro.model.migrations;

import sep_intro.model.config.Config;

public interface Migration {
	long getId();
	void up(Config config);
	void down(Config config);
}

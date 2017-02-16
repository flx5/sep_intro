package sep_intro.model.migrations;

public interface Migration {
	long getId();
	void up();
	void down();
}

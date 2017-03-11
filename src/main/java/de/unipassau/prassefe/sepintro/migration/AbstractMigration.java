package de.unipassau.prassefe.sepintro.migration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractMigration implements Migration {
	private static final Pattern nameRegex = Pattern.compile("^M([0-9]{10})_.*$");

	private final long id;

	public AbstractMigration() {
		String className = getClass().getSimpleName();

		Matcher matcher = nameRegex.matcher(className);

		if (!matcher.matches()) {
			throw new MigrationException("Migration class name is invalid!");
		}

		String idString = matcher.group(1);
		this.id = Long.parseLong(idString);
	}

	@Override
	public final long getId() {
		return id;
	}
}

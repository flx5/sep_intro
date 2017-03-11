package de.unipassau.prassefe.sepintro.util;

import java.util.Optional;

public class EnumUtil {
	private EnumUtil() {
		// Utility class
	}

	public static <T extends Enum<?>> Optional<T> lookup(Class<T> enumType, String name) {
		for (T entry : enumType.getEnumConstants()) {
			if (entry.name().equalsIgnoreCase(name)) {
				return Optional.of(entry);
			}
		}
		return Optional.empty();
	}
}

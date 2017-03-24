package de.unipassau.prassefe.sepintro.util;

import java.util.Optional;

/**
 * Utility functions for enumerations.
 * @author Felix Prasse <prassefe@fim.uni-passau.de>
 */
public class EnumUtil {
	private EnumUtil() {
		// Utility class
	}

        /**
         * Get enumeration entry by its name. Ignores casing.
         * @param <T> Enumeration type.
         * @param enumType Enumeration class.
         * @param name The entry name.
         * @return The found entry, if any.
         */
	public static <T extends Enum<?>> Optional<T> lookup(Class<T> enumType, String name) {
		for (T entry : enumType.getEnumConstants()) {
			if (entry.name().equalsIgnoreCase(name)) {
				return Optional.of(entry);
			}
		}
		return Optional.empty();
	}
}

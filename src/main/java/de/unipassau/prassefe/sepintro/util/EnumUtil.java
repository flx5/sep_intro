package de.unipassau.prassefe.sepintro.util;

public class EnumUtil {
	private EnumUtil() {
		// Utility class
	}

	public static <T extends Enum<?>> T lookup(Class<T> enumType, String name) {
		for (T entry : enumType.getEnumConstants()) {
			if (entry.name().equalsIgnoreCase(name)) {
				return entry;
			}
		}
		return null;
	}
}

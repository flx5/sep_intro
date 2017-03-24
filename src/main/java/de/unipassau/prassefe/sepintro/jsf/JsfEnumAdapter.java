package de.unipassau.prassefe.sepintro.jsf;

import de.unipassau.prassefe.sepintro.util.EnumUtil;

/**
 * Adapter connecting Jsf and Java Enums
 *
 * @author Felix Prasse <prassefe@fim.uni-passau.de>
 * @param <T>
 */
public abstract class JsfEnumAdapter<T extends Enum<?>> {

    private final Class<T> enumClass;

    /**
     * Create a new enumeration adapter.
     * @param enumClazz The enum this adapter wraps.
     */
    public JsfEnumAdapter(final Class<T> enumClazz) {
        this.enumClass = enumClazz;
    }

    /**
     * Get enumeration entry by it's name
     *
     * @param name The name of the entry
     * @return The entry
     * @throws IllegalArgumentException when the entry does not exist.
     */
    public final T lookup(final String name) throws IllegalArgumentException {
        return EnumUtil.lookup(enumClass, name).orElseThrow(() -> new IllegalArgumentException(name));
    }
}

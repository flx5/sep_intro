package de.unipassau.prassefe.sepintro.jsf;

import de.unipassau.prassefe.sepintro.util.EnumUtil;

public abstract class JsfEnumAdapter<T extends Enum<?>> {
	private final Class<T> enumClass;
	
	public JsfEnumAdapter(Class<T> enumClass) {
		this.enumClass = enumClass;
	}
	
	public T lookup(String name) {
		return EnumUtil.lookup(enumClass, name).orElseThrow(() -> new IllegalArgumentException(name));
	}
}

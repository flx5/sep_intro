package de.unipassau.prassefe.sepintro.util.functional;

@FunctionalInterface
public interface ThrowingConsumer<T, E extends Throwable> {
	void accept(final T elem) throws E;
}
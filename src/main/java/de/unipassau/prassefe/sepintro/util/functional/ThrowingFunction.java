package de.unipassau.prassefe.sepintro.util.functional;

@FunctionalInterface
public interface ThrowingFunction<T, R, E extends Throwable> {
	R apply(T t) throws E;
}

package de.unipassau.prassefe.sepintro.model.repository.fake;

/**
 * Integer identifier generator.
 * @author Felix Prasse <prassefe@fim.uni-passau.de>
 */
public class IntIdGenerator implements IdGenerator<Integer> {

	private int current;
	
	@Override
	public synchronized Integer next() {
		return ++current;
	}

}

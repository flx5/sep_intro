package de.unipassau.prassefe.sepintro.model.repository.fake;

public class IntIdGenerator implements IdGenerator<Integer> {

	private int current;
	
	@Override
	public synchronized Integer next() {
		return ++current;
	}

}

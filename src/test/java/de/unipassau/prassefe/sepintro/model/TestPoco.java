package de.unipassau.prassefe.sepintro.model;

public class TestPoco {
	private int id;
	private long value;

	public TestPoco(long value) {
		this(0, value);
	}
	
	public TestPoco(int id, long value) {
		this.id = id;
		this.value = value;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	public boolean equals(TestPoco other) {
		if (this == other) {
			return true;
		}

		if (other == null) {
			return false;
		}

		return id == other.id && value == other.value;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof TestPoco && equals((TestPoco) obj);
	}
}

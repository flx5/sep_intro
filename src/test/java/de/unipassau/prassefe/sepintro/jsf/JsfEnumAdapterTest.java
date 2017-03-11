package de.unipassau.prassefe.sepintro.jsf;

import static org.junit.Assert.*;

import org.junit.Test;

import de.unipassau.prassefe.sepintro.TestEnum;

public class JsfEnumAdapterTest {

	private static class TestAdapter extends JsfEnumAdapter<TestEnum> {
		public TestAdapter() {
			super(TestEnum.class);
		}
	}
	
	@Test
	public final void testLookup() {
		TestAdapter test = new TestAdapter();
		assertEquals(TestEnum.OK, test.lookup("OK"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void testInvalid() {
		TestAdapter test = new TestAdapter();
		test.lookup("42");
	}

}
